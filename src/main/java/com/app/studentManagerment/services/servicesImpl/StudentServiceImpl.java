package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.StudentRepository;
import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.mapper.StudentListMapper;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.dto.Semester_StudentDto;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.StudentService;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final GoogleService googleService;
    private final StudentListMapper studentListMapper;
    private final AccountRepository accountRepository;
    private final ClassroomRepository classroomRepository;

    public StudentServiceImpl(StudentRepository studentRepository, GoogleService googleService, StudentListMapper studentListMapper, AccountRepository accountRepository, ClassroomRepository classroomRepository) {
        this.studentRepository = studentRepository;
        this.googleService = googleService;
        this.studentListMapper = studentListMapper;
        this.accountRepository = accountRepository;
        this.classroomRepository = classroomRepository;
    }


    @Override
    public Student findById(long id) {
        Optional<Student> result = studentRepository.findById(id);
        Student student = null;
        if (result.isPresent()) {
            student = result.get();
        } else {
            throw new RuntimeException("Did not find Student");
        }
        return student;

    }

    @Override
    public Student addStudent(int current_semester, String firstName, String lastName, LocalDate dob, String address, MultipartFile avatarFile) throws GeneralSecurityException, IOException {
        String mssv = getMSSV();
        String name = firstName + " " + lastName;
        String foderName = "StudentManager/Student";
        String avatarFileName = mssv + "--" + name.replace(" ", "") + ".jpg";

        // Tạo một CompletableFuture để tạo link từ file avatar
        CompletableFuture<String> futureLink = CompletableFuture.supplyAsync(() -> {
            try {
                String filedId = googleService.uploadFile(avatarFile, foderName, "anyone", "reader", avatarFileName);
                return googleService.getLiveLink(filedId);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        // Tạo đối tượng Student và lưu vào database
        Student theStudent = new Student();
        theStudent.setMssv(mssv.trim());
        theStudent.setCurrentSemester(current_semester);
        theStudent.setName(firstName.trim() + " " + lastName.trim());
        theStudent.setDob(dob);
        theStudent.setAddress(address.trim());

        // Chờ đợi đến khi link được tạo xong rồi mới set vào Student
        try {
            String livelink = futureLink.get();
            theStudent.setAvatar(livelink);
        } catch (InterruptedException | ExecutionException | java.util.concurrent.ExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return studentRepository.save(theStudent);
    }

    @Override
    public StudentDto updateStudent(String mssv, int current_semester, String mail, String Name, LocalDate dob, String address, MultipartFile avatarFile) throws Exception {
        Student theStudent = studentRepository.findByMssv(mssv);
        if (theStudent != null) {
            if (current_semester > 0) {
                theStudent.setCurrentSemester(current_semester);
            }
            if (mail != null) {
                Account account = accountRepository.findByEmail(mail);
                theStudent.setAccount(account);
            }
            if (Name != null) {
                theStudent.setName(Name);
            }
            if (dob != null) {
                theStudent.setDob(dob);
            }
            if (address != null) {
                theStudent.setAddress(address);
            }
            if (avatarFile != null) {
                // tìm avatar cũ của student
                //  xóa nó đi
                if (theStudent.getAvatar() != null && theStudent.getAvatar().contains("https://drive.google.com/uc?id=")) {
                    String fileId = theStudent.getAvatar().substring(31);
                    googleService.deleteFileOrFolder(fileId);
                }
                // thêm avatar mới vào
                String foderName = "StudentManager/Student";
                String avatarFileName = mssv + "--" + Name.replace(" ", "") + ".jpg";
                CompletableFuture<String> futureLink = CompletableFuture.supplyAsync(() -> {
                    try {
                        String filedId = googleService.uploadFile(avatarFile, foderName, "anyone", "reader", avatarFileName);
                        return googleService.getLiveLink(filedId);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
                // set vào thuộc tính để thêm vào db
                try {
                    String livelink = futureLink.get();
                    theStudent.setAvatar(livelink);
                } catch (InterruptedException | ExecutionException | java.util.concurrent.ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            return null;
        }
        studentRepository.save(theStudent);
        return studentListMapper.StudentToStudentDto(theStudent);
    }

    @Override
    public boolean deleteStudent(String mssv) {
        Student student = studentRepository.findByMssv(mssv);
        if (student != null) {
            List<ClassRoom> classRoomsOfStudent = classroomRepository.findByStudents(student);
            if (!classRoomsOfStudent.isEmpty()) {
                for (ClassRoom c : classRoomsOfStudent) {
                    c.getStudents().remove(student);
                }
            }
            studentRepository.delete(student);
            return true;
        }

        return false;
    }


    @Override
    public String getMSSV() {
        if (studentRepository.count() > 0) {
            Student student = studentRepository.findFirstByOrderByIdDesc();
            String mssv = student.getMssv().split("-")[2];

            int numberofMssv = 0;
            try {
                numberofMssv = Integer.parseInt(mssv) + 1;
            } catch (NumberFormatException e) {
                return null;
            }
            String formattedNum = String.format("%05d", numberofMssv);
            return "FPT-Student-" + formattedNum;
        } else {
            return "FPT-Student-" + "00001";
        }
    }

    @Override
    public Page<StudentDto> search(String searchTerm, String type, Pageable pageable) {
        Page<StudentDto> studentDtos = studentRepository.search(searchTerm, type, pageable);
        return studentDtos;
    }

    @Override
    public List<StudentDto> getAllStudentWithCurrentSemester(int currentSemester) {
        List<StudentDto> studentDtos = studentRepository.findAllByCurrentSemester(currentSemester).stream().map(studentListMapper::StudentToStudentDto).toList();

        return studentDtos;
    }

    @Override
    public Integer totalStudentWithCurrentSemester(int currentSemester) {
        return studentRepository.countStudentByCurrentSemester(currentSemester);
    }


}
