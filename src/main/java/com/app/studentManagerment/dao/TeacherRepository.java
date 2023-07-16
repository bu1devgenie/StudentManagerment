package com.app.studentManagerment.dao;


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

    Teacher findByMsgv(String msgv);

    @Query(value = """
            SELECT t.*,
                          (SELECT count(teacher_id)
                           FROM teacher
                           JOIN classroom c ON teacher.id = c.teacher_id
                           WHERE c.semester_id = :semesterId
                             AND teacher_id = t.id ) AS numberClassTeaching
                        FROM teacher t
                        WHERE
                            (SELECT COUNT(DISTINCTROW a.slot_of_day, b.slot_of_day, a.dayofw, b.dayofw)
                             FROM
                               (SELECT DISTINCTROW s.slot_of_day,
                                                   dayofweek(s.sesion_date) AS dayofw
                                FROM classroom c
                                JOIN SESSION s ON c.id = s.classroom_id
                                WHERE c.teacher_id = t.id ) a
                             JOIN
                               (SELECT DISTINCTROW s.slot_of_day,
                                                   dayofweek(s.sesion_date) AS dayofw
                                FROM SESSION s
                                WHERE classroom_id = :classroomId ) b ON a.dayofw = b.dayofw
                             WHERE a.slot_of_day = b.slot_of_day )= 0
                          AND
                            (SELECT count(c.id)
                             FROM teacher
                             JOIN classroom c ON teacher.id = c.teacher_id
                             WHERE c.semester_id = :semesterId and teacher.id=t.id)< 4
                          AND :courseId in
                            (SELECT tc.course_id
                             FROM teacher
                             JOIN teacher_course tc ON teacher.id = tc.teacher_id
                             JOIN course c ON tc.course_id = c.id
                             WHERE teacher.id = t.id )
                        ORDER BY numberClassTeaching ASC
                        limit 1
             """, nativeQuery = true)
    Teacher getTeacherTakeClass(@Param("semesterId") long semesterId, @Param("classroomId") long classroomId, @Param("courseId") long courseId);


    @Query("""
            select distinct t.account.email from Teacher t
            join ClassRoom c
            where c.semester.id=:semesterId
            """)
    List<String> allEmailOfTeacherTeakedClass(@Param("semesterId") long semesterId);

    @Query(value = """
            select distinct a.email
                        from teacher t
                        join classroom c on t.id = c.teacher_id
                        join account a on a.id = t.account_id
                        where c.semester_id=:id and t.account_id is not null
                        group by t.id
            """, nativeQuery = true)
    List<String> getAllEmailTeacherHaveClass(@Param("id") Long id);
}
