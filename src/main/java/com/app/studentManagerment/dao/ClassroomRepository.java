package com.app.studentManagerment.dao;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<ClassRoom, Long> {
    List<ClassRoom> findByTeacher(Teacher teacher);

    List<ClassRoom> findByStudents(Student student);

    List<ClassRoom> findByCourse(Course course);

    ClassRoom findByName(String className);

    int countByCourse(Course course);

    @Query("""
            SELECT cr FROM ClassRoom cr
                      LEFT JOIN cr.semester se
                      LEFT JOIN cr.teacher tea\s
                      WHERE (:semesterName IS NULL OR cr.semester.name = :semesterName)
                      AND (:msgv IS NULL OR tea.msgv LIKE :msgv)
                      AND (:courseName IS NULL OR cr.course.name = :courseName)
                      AND (:currentSlot IS NULL OR :currentSlot like concat('',cr.currentSlot,''))
                      AND (:learning IS NULL OR cr.Learning = :learning)
            """)
    Page<ClassRoom> searchClassRoom(
            @Param("semesterName") String semesterName,
            @Param("msgv") String msgv,
            @Param("courseName") String courseName,
            @Param("currentSlot") String currentSlot,
            @Param("learning") boolean learning,
            Pageable pageable);

    @Query("""
            select clr from ClassRoom clr
            """)
    Page<ClassRoom> getAllDto(Pageable pageable);

}
