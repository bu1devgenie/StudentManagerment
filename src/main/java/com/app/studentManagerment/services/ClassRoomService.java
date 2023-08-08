package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.dto.TimeClassDto;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassRoomService {
    public Page<ClassRoomDto> searchClassRoom(String semester_name, String msgv, String courseName, String currentSlot, boolean learning);

    public Page<ClassRoomDto> getAll();

    public ClassRoom newClassRoom(Semester semester, Teacher teacher, Course course, int currentSlot, boolean Learning, List<Student> studentList);

    public ClassRoom updateClassRoom(String className, String msgv, String courseName, int currentSlot, List<Student> students, boolean shutDown);

    public boolean deleteClassRoom(String className);

    public boolean ShutdownClassRoom(String className, boolean isShutdown);

    List<TimeClassDto> getTimeClass(String className);

    List<String> getClassRoomForRegister(String courseName);
}
