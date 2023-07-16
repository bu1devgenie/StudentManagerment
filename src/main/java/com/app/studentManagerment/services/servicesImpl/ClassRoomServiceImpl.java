package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.*;
import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.dto.mapper.ClassRoomListMapper;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.services.ClassRoomService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final ClassRoomListMapper classRoomListMapper;

    public ClassRoomServiceImpl(ClassroomRepository classroomRepository, TeacherRepository teacherRepository, CourseRepository courseRepository, StudentRepository studentRepository, SemesterRepository semesterRepository, ClassRoomListMapper classRoomListMapper) {
        this.classroomRepository = classroomRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.semesterRepository = semesterRepository;
        this.classRoomListMapper = classRoomListMapper;
    }

    @Override
    public Page<ClassRoomDto> searchClassRoom(String semester_name,
                                              String msgv,
                                              String courseName,
                                              String currentSlot,
                                              boolean learning) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClassRoom> classRoom = classroomRepository.searchClassRoom(semester_name, msgv, courseName, currentSlot, learning, pageable);
//        Page<ClassRoom> classRoom = classroomRepository.searchClassRoom( msgv, pageable);
        Page<ClassRoomDto> classRoomDtos = classRoom.map(classRoomListMapper::classRoomToclassRoomDto);
        return classRoomDtos;
    }

    @Override
    public Page<ClassRoomDto> getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClassRoom> classRoom = classroomRepository.getAllDto(pageable);
        Page<ClassRoomDto> classRoomDtos = classRoom.map(classRoomListMapper::classRoomToclassRoomDto);
        return classRoomDtos;
    }

    @Override
    public ClassRoom newClassRoom(Semester semester, Teacher teacher, Course course, int currentSlot, boolean Learning, List<Student> studentList) {
        ClassRoom newClassRoom = new ClassRoom();
        String className = course.getName() + "-" + (classroomRepository.countByCourse(course) + 1) + "-" + semester.getName();
        newClassRoom.setName(className);
        newClassRoom.setTeacher(teacher);
        newClassRoom.setCourse(course);
        newClassRoom.setCurrentSlot(currentSlot);
        newClassRoom.setLearning(Learning);
        newClassRoom.setStudents(studentList);
        newClassRoom.setSemester(semester);
        return classroomRepository.save(newClassRoom);
    }

    @Override
    public ClassRoom updateClassRoom(String className, String msgv, String courseName, int currentSlot, List<Student> students, boolean shutDown) {
        ClassRoom classRoom = classroomRepository.findByName(className);
        if (classRoom != null) {
            if (!msgv.isEmpty()) {
                Teacher teacher = teacherRepository.findByMsgv(msgv);
                if (teacher != null) {
                    classRoom.setTeacher(teacher);
                }
            }
            if (!courseName.isEmpty()) {
                Course course = courseRepository.findByName(courseName);
                if (course != null) {
                    classRoom.setCourse(course);
                }
            }
            if (currentSlot > 25 && currentSlot < 35) {
                classRoom.setCurrentSlot(currentSlot);
            }
            if (!students.isEmpty()) {
                classRoom.setStudents(students);
            }
            if (classRoom.isLearning() != shutDown) {
                classRoom.setLearning(shutDown);
            }
            return classroomRepository.save(classRoom);
        }
        return null;
    }

    @Override
    public boolean deleteClassRoom(String className) {
        ClassRoom classRoom = classroomRepository.findByName(className);
        if (classRoom != null) {
            classroomRepository.delete(classRoom);
            return true;
        }
        return false;
    }

    @Override
    public boolean ShutdownClassRoom(String className, boolean isShutdown) {
        ClassRoom classRoom = classroomRepository.findByName(className);
        if (classRoom != null) {
            classRoom.setLearning(isShutdown);
            classroomRepository.save(classRoom);
            return true;
        }
        return false;
    }
}
