package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.enumPack.enumGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;

public interface StudentService {
	Student findStudentByEmail(String email);

	Student findById(long id);

	Student findByMssv(String mssv);

	Student addStudent(int current_semester,
	                   String name,
	                   LocalDate dob,
	                   String address,
	                   MultipartFile avatarFile, enumGender enumGender, String email) throws GeneralSecurityException, IOException;

	StudentDto updateStudent(String mssv,
	                         int current_semester,
	                         String mail,
	                         String Name,
	                         LocalDate dob,
	                         String address,
	                         MultipartFile avatarFile, enumGender enumGender) throws Exception;

	boolean deleteStudent(String mssv);

	String getMSSV();

	Page<StudentDto> search(String mssv, Integer semester, String name, LocalDate dob, String email, Pageable pageable);

	List<StudentDto> getAllStudentWithCurrentSemester(int currentSemester);

	Integer totalStudentWithCurrentSemester(int currentSemester);
}
