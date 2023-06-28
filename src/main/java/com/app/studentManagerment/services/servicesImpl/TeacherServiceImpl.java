package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.CourseRepository;
import com.app.studentManagerment.dao.TeacherRepository;
import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.dto.mapper.TeacherListMapper;
import com.app.studentManagerment.entity.*;
import com.app.studentManagerment.dto.Course_TeacherDto;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.TeacherService;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class TeacherServiceImpl implements TeacherService {
    private TeacherRepository teacherRepository;
    private GoogleService googleService;
    private CourseRepository courseRepository;
    private TeacherListMapper teacherListMapper;
    private AccountRepository accountRepository;
    private ClassroomRepository classroomRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, GoogleService googleService, CourseRepository courseRepository, TeacherListMapper teacherListMapper, AccountRepository accountRepository, ClassroomRepository classroomRepository) {
        this.teacherRepository = teacherRepository;
        this.googleService = googleService;
        this.courseRepository = courseRepository;
        this.teacherListMapper = teacherListMapper;
        this.accountRepository = accountRepository;
        this.classroomRepository = classroomRepository;
    }


    @Override
    public Page<TeacherDto> search(String searchText, String type, Pageable pageable) {
        Page<Teacher> teachers = teacherRepository.search(searchText, type, pageable);
        Page<TeacherDto> teacherDtos = teachers.map(teacher -> teacherListMapper.teacherToTeacherDTO(teacher));
        return teacherDtos;
    }

    @Override
    public String getMSGV() {
        if (teacherRepository.count() > 0) {
            Teacher teacher = teacherRepository.findFirstByOrderByIdDesc();
            String msgv = teacher.getMsgv().split("-")[2];

            int numberofMgsv = 0;
            try {
                numberofMgsv = Integer.parseInt(msgv) + 1;
            } catch (NumberFormatException e) {
                return null;
            }
            String formattedNum = String.format("%05d", numberofMgsv);
            return "FPT-Teacher-" + formattedNum;
        } else {
            return "FPT-Teacher-" + "00001";
        }
    }

    @Override
    public Teacher addTeacher(List<String> course, String name, LocalDate dob, String address, MultipartFile avatar) {
        String msgv = getMSGV();
        String foderName = "StudentManager/Teacher";
        String avatarFileName = msgv + "--" + name.replace(" ", "") + ".jpg";

        CompletableFuture<String> futureLink = CompletableFuture.completedFuture(null);
        if (avatar != null) {
            // Nếu avatar khác null, tạo một CompletableFuture để tạo link từ file avatar
            futureLink = CompletableFuture.supplyAsync(() -> {
                try {
                    String fileId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
                    return googleService.getLiveLink(fileId);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            });
        }

        // Tạo đối tượng Teacher và lưu vào database
        Teacher theTeacher = new Teacher();
        theTeacher.setMsgv(msgv.trim());
        theTeacher.setName(name.replace("\\s+", " "));
        theTeacher.setDob(dob);
        theTeacher.setAddress(address.trim());
        List<Course> courses = new ArrayList<>();
        if (!courses.isEmpty()) {
            for (String s : course) {
                courses.add(courseRepository.findByName(s));
            }
            theTeacher.setCourse(courses);
        } else {
            theTeacher.setCourse(null);
        }

        // Chỉ chờ kết quả tạo link nếu avatar khác null
        String liveLink = null;
        if (avatar != null) {
            try {
                liveLink = futureLink.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (java.util.concurrent.ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        theTeacher.setAvatar(liveLink);

        return teacherRepository.save(theTeacher);
    }


    @Override
    public TeacherDto updateTeacher(String msgvUpdate, String name, String address, LocalDate dob, MultipartFile avatar, List<String> courses, String email) throws Exception {
        Teacher teacher = teacherRepository.findByMsgv(msgvUpdate);
        if (teacher != null) {
            if (name != null) {
                teacher.setName(name);
            }
            if (address != null) {
                teacher.setAddress(address);
            }
            if (dob != null) {
                teacher.setDob(dob);
            }
            if (avatar != null) {
                // tìm avatar cũ của teacher
                //  xóa nó đi
                if (teacher.getAvatar() != null && teacher.getAvatar().contains("https://drive.google.com/uc?id=")) {
                    String fileId = teacher.getAvatar().substring(31);
                    googleService.deleteFileOrFolder(fileId);
                }
                // thêm avatar mới vào
                String foderName = "StudentManager/Student";
                String avatarFileName = msgvUpdate + "--" + name.replace(" ", "") + ".jpg";
                CompletableFuture<String> futureLink = CompletableFuture.supplyAsync(() -> {
                    try {
                        String filedId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
                        return googleService.getLiveLink(filedId);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
                // set vào thuộc tính để thêm vào db
                try {
                    String livelink = futureLink.get();
                    teacher.setAvatar(livelink);
                } catch (InterruptedException | ExecutionException | java.util.concurrent.ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (courses != null) {
                if (teacher.getCourse() != null) {
                    for (String c : courses) {
                        teacher.getCourse().add(courseRepository.findByName(c));
                    }
                } else {
                    List<Course> newCourses = new ArrayList<>();
                    for (String s : courses) {
                        newCourses.add(courseRepository.findByName(s));
                    }
                    teacher.setCourse(newCourses);
                }
            }
            if (email != null) {
                Account account = accountRepository.findByEmail(email);
                if (account != null) {
                    if (accountRepository.emailIsConnected(account.getEmail())) {
                        teacher.setAccount(account);
                    } else {
                        teacher.setAccount(null);
                    }
                }
            }
            teacherRepository.save(teacher);
            return teacherListMapper.teacherToTeacherDTO(teacher);
        }
        return null;
    }

    @Override
    public boolean deleteTeacher(String msgv) {
        Teacher teacher = teacherRepository.findByMsgv(msgv);
        if (teacher != null) {
            List<ClassRoom> classRoomsOfTeacher = classroomRepository.findByTeacher(teacher);
            if (!classRoomsOfTeacher.isEmpty()) {
                for (ClassRoom classRoom : classRoomsOfTeacher) {
                    classRoom.setTeacher(null);
                }
            }
            teacherRepository.delete(teacher);
            return true;
        }
        return false;
    }

    @Override
    public List<Teacher> getAllTeacherCanTakeClasses(long semesterId, long courseId, int[] dayOfWeak, int[] slot_of_day) {
        return null;
    }


}
