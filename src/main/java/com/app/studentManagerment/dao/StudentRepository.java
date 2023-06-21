package com.app.studentManagerment.dao;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.Semester_StudentDto;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            SELECT new com.app.studentManagerment.dto.StudentDto(stu.id, stu.mssv, stu.currentSemester, stu.name, stu.dob, stu.address, stu.avatar, a.email)
            FROM Student stu LEFT JOIN Account a ON a.id = stu.account.id
                        WHERE ((:type='' or :type like 'mssv') and (:searchTerm IS NULL OR stu.mssv LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'name') and (:searchTerm IS NULL OR stu.name LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'dob') and (:searchTerm IS NULL OR stu.dob LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'address') and (:searchTerm IS NULL OR stu.address LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'semester') and (:searchTerm IS NULL OR stu.currentSemester like concat('%',:searchTerm,'%') ))
                        OR ((:type='' or :type like 'email') and (:searchTerm IS NULL OR a.email LIKE CONCAT('%', :searchTerm, '%')))
                        ORDER BY
                          CASE
                            WHEN :type = 'mssv' THEN stu.mssv
                            WHEN :type = 'name' THEN stu.name
                            WHEN :type = 'dob' THEN stu.dob
                            WHEN :type = 'address' THEN stu.address
                            WHEN :type = 'semester' THEN stu.currentSemester
                            WHEN :type = 'email' THEN a.email END ASC""")
    Page<StudentDto> search(@Param("searchTerm") String searchTerm,
                            @Param("type") String type, Pageable pageable);

    Student findFirstByOrderByIdDesc();

    Student findByMssv(String mssv);


    List<Student> findAllByCurrentSemester(int currentSemester);

    Integer countStudentByCurrentSemester(int currentSemester);

}
