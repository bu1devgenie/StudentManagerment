package com.app.studentManagerment.dao;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.entity.user.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
	@Query("""
			SELECT new com.app.studentManagerment.dto.StudentDto(stu.id, stu.mssv, stu.currentSemester, stu.name, stu.dob, stu.address, stu.avatar, a.email, stu.enumGender)
			FROM Student stu
			LEFT JOIN Account a ON a.email = stu.account.email
			WHERE ( :mssv like '' or :mssv IS NULL OR stu.mssv LIKE CONCAT('%', :mssv, '%'))
			  AND (
			    CASE
			      WHEN (:semester BETWEEN 1 AND 10) THEN (stu.currentSemester = :semester)
			      WHEN (:semester IS NULL) THEN true
			      WHEN (:semester <= 0 OR :semester > 10) THEN true
			      ELSE false
			    END
			  )
			  AND ( :name like '' or :name IS NULL OR stu.name LIKE CONCAT('%', :name, '%'))
			  AND ( :dob like '' or :dob IS NULL OR stu.dob = :dob)
			  AND ( :email like '' or :email IS NULL OR a.email LIKE CONCAT('%', :email, '%'))
						
			                """)
	Page<StudentDto> search(@Param("mssv") String mssv,
	                        @Param("semester") Integer semester,
	                        @Param("name") String name,
	                        @Param("dob") LocalDate dob,
	                        @Param("email") String email,
	                        Pageable pageable);

	Student findFirstByOrderByIdDesc();

	Student findByMssv(String mssv);


	List<Student> findAllByCurrentSemester(int currentSemester);

	Integer countStudentByCurrentSemester(int currentSemester);

	Student findStudentByAccount_Email(String email);
}
