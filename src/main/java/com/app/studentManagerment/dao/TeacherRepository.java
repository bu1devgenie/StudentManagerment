package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Teacher findFirstByOrderByIdDesc();

	@Query(value = """
			   SELECT t.id, t.address, t.avatar, t.dob, t.enum_gender, t.msgv, t.name, t.email
			   FROM teacher t
			   LEFT JOIN account a ON a.email = t.email
			   LEFT JOIN teacher_course tc ON t.id = tc.teacher_id
			   WHERE (:msgv IS NULL OR t.msgv LIKE CONCAT('%', :msgv, '%'))
			       AND (:name IS NULL OR t.name LIKE CONCAT('%', :name, '%'))
			       AND (:dob IS NULL OR t.dob = :dob)
			       AND (:address IS NULL OR t.address LIKE CONCAT('%', :address, '%'))
			       AND ((:email IS NULL OR :email = '') OR (a.email LIKE CONCAT('%', :email, '%')))
			       AND (:courseNameList IS NULL OR (tc.course_id IN ( SELECT c.id FROM Course c WHERE c.name IN :courseNameList )))
			   GROUP BY t.id
			""", nativeQuery = true)
	Page<Teacher> search(@Param("msgv") String msgv,
	                     @Param("name") String name,
	                     @Param("dob") LocalDate dob,
	                     @Param("address") String address,
	                     @Param("email") String email,
	                     @Param("courseNameList") List<String> courseNameList,
	                     Pageable pageable);

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
	Teacher getTeacherTakeClass(@Param("semesterId") long semesterId,
	                            @Param("classroomId") long classroomId,
	                            @Param("courseId") long courseId);


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
						            join account a on a.email = t.email
						            where c.semester_id=:semesterId and t.email is not null
						            group by t.id
			""", nativeQuery = true)
	List<String> getAllEmailTeacherHaveClass(@Param("semesterId") Long semesterId);


	Teacher findTeacherByAccount_Email(@Param("email") String email);
}
