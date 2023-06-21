package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.Semester_StudentDto;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;

public interface StudentService {


    Student findById(long id);

    Student addStudent(int current_semester,
                       String firstName,
                       String lastName,
                       LocalDate dob,
                       String address,
                       MultipartFile avatarFile) throws GeneralSecurityException, IOException;

    StudentDto updateStudent(String mssv,
                             int current_semester,
                             String mail,
                             String Name,
                             LocalDate dob,
                             String address,
                             MultipartFile avatarFile) throws Exception;

    boolean deleteStudent(String mssv);

    String getMSSV();

    Page<StudentDto> search(String searchTerm, String type, Pageable pageable);

    List<StudentDto> getAllStudentWithCurrentSemester(int currentSemester);

    Integer totalStudentWithCurrentSemester(int currentSemester);
}
