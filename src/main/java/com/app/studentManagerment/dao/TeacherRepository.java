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

    Teacher findByMsgv(String msgvUpdate);

    @Query(value = """
            select *,(
                    SELECT COALESCE(COUNT(*), 0) as numberClassOfTeacher
                    FROM teacher t2
                    JOIN classroom c ON t2.id = c.teacher_id
                    WHERE c.semester_id = :semesterId AND t2.id = t.id
                    GROUP BY t2.id
                    UNION ALL
                    SELECT 0
                    WHERE NOT EXISTS (
                        SELECT *
                        FROM teacher t3
                        JOIN classroom c ON t3.id = c.teacher_id
                        WHERE c.semester_id = :semesterId AND t3.id = t.id
                    )
                ) as num_classes
                    from teacher t
                     join teacher_course tc on t.id = tc.teacher_id
                     where
                         tc.course_id=:courseId and
                                        t.id not in
                                        (select distinct c.teacher_id from slot
                                        join schedule s on s.id = slot.slot_id
                                        join classroom c on c.id = s.classroom_id
                                        where day_of_week in :dayOfWeak
                                        and slot.slot_of_day in :slot_of_day
                                        and teacher_id is not null
                                        )
                        and
                        (SELECT COALESCE(COUNT(*), 0) as numberClassOfTeacher
                                FROM teacher t2
                                JOIN classroom c ON t2.id = c.teacher_id
                                WHERE c.semester_id = :semesterId AND t2.id = t.id
                                GROUP BY t2.id
                                UNION ALL
                                SELECT 0
                                WHERE NOT EXISTS (
                                    SELECT *
                                    FROM teacher t3
                                    JOIN classroom c ON t3.id = c.teacher_id
                                WHERE c.semester_id = :semesterId AND t3.id = t.id
                        ))<=4
            order by  num_classes
              """, nativeQuery = true)
    List<Teacher> getTeacherCanTakeClasses(@Param("semesterId")long semesterId,
                                           @Param("courseId")long courseId,
                                           @Param("dayOfWeak") int[] dayOfWeak,
                                           @Param("slot_of_day") int[] slot_of_day
                                           );

}
