package com.app.studentManagerment.dao;


import com.app.studentManagerment.dto.Course_TeacherDto;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.Slot;
import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findFirstByOrderByIdDesc();


    @Query("""
            SELECT tea
            FROM Teacher tea LEFT JOIN Account a ON a.id = tea.account.id
            WHERE ((:type='' or :type like 'msgv') and (:searchTerm IS NULL OR tea.msgv LIKE CONCAT('%', :searchTerm, '%'))
                OR ((:type='' or :type like 'name') and (:searchTerm IS NULL OR tea.name LIKE CONCAT('%', :searchTerm, '%')))
            OR ((:type='' or :type like 'dob') and (:searchTerm IS NULL OR tea.dob LIKE CONCAT('%', :searchTerm, '%')))
            OR ((:type='' or :type like 'address') and (:searchTerm IS NULL OR tea.address LIKE CONCAT('%', :searchTerm, '%')))
            OR ((:type='' or :type like 'email') and (:searchTerm IS NULL OR a.email LIKE CONCAT('%', :searchTerm, '%')))
            OR ((:type='' or :type like 'course') and (:searchTerm IS NULL OR EXISTS (select 1 from Course c where c.name like concat('%',:searchTerm,'%') and c in (select tc from tea.course tc)))))
            ORDER BY
            CASE
            WHEN :type = 'msgv' THEN tea.msgv
            WHEN :type = 'name' THEN tea.name
            WHEN :type = 'dob' THEN tea.dob
            WHEN :type = 'address' THEN tea.address
            WHEN :type = 'email' THEN a.email
            WHEN :type = 'course' THEN tea.name END ASC""")
    Page<Teacher> search(@Param("searchTerm") String searchTerm,
                         @Param("type") String type, Pageable pageable);

    Teacher findByMsgv(String msgvUpdate);

    @Query("""
            SELECT t from Teacher t
               """)
    Teacher getTeacherCanTakeClasses(@Param("courseName") String courseName, @Param("semesterId") Long semesterId, @Param("slotOfClass") List<Slot> slotOfClass);

}
