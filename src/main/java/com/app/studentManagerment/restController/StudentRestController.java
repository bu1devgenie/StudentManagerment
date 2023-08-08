package com.app.studentManagerment.restController;


import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.enumPack.enumGender;
import com.app.studentManagerment.services.HelperService;
import com.app.studentManagerment.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@RestController
@RequestMapping("/student")
public class StudentRestController {

	private final StudentService studentServices;


	public StudentRestController(StudentService studentServices, HelperService helperService) {
		this.studentServices = studentServices;
	}

	@GetMapping("/getMSSV")
	public synchronized String getMSSV() {
		return studentServices.getMSSV();
	}


	@PostMapping("/addStudent")
	public synchronized Student addStudent(
			@RequestParam(name = "current_semester") int current_semester,
			@RequestParam(name = "name") String name,
			@RequestParam(name = "dob") LocalDate dob,
			@RequestParam(name = "address") String address,
			@RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
			@RequestParam(name = "gender") String gender,
			@RequestParam(name = "email", required = false) String mail) throws IOException, GeneralSecurityException {
//      =========================
		enumGender gender1 = enumGender.MALE;
		switch (gender) {
			case "FEMALE" -> gender1 = enumGender.FEMALE;
			case "OTHER" -> gender1 = enumGender.OTHER;
		}
		return studentServices.addStudent(current_semester, name, dob, address, avatarFile, gender1, mail);
	}

	@PostMapping("/searchStudent")
	public Page<StudentDto> searchStudent(@RequestParam(name = "mssv", required = false) String mssv,
	                                      @RequestParam(name = "semester", required = false) Integer semester,
	                                      @RequestParam(name = "name", required = false) String name,
	                                      @RequestParam(name = "email", required = false) String email,
	                                      @RequestParam(name = "dob", required = false) String dob,
	                                      @RequestParam(name = "targetPageNumber") Integer targetPageNumber) {
		if (targetPageNumber < 0) {
			return null;
		}
		Pageable pageable = PageRequest.of(targetPageNumber, 20);
		LocalDate dobDate = null;
		if (dob != null && ! dob.isEmpty()) {
			dobDate = LocalDate.parse(dob);
		}

		return studentServices.search(mssv, semester, name, dobDate, email, pageable);
	}

	@DeleteMapping("/deleteStudent")
	public synchronized boolean deleteStudent(@RequestParam(name = "mssv") String mssv) {
		return studentServices.deleteStudent(mssv);
	}

	@PutMapping("/updateStudent")
	public synchronized StudentDto updateStudent(@RequestParam(name = "mssvUpdate", required = true) String mssv,
	                                             @RequestParam(name = "semester", required = false) Integer current_semester,
	                                             @RequestParam(name = "email", required = false) String email,
	                                             @RequestParam(name = "name", required = false) String name,
	                                             @RequestParam(name = "dob", required = false) String dob,
	                                             @RequestParam(name = "address", required = false) String address,
	                                             @RequestParam(name = "avatar", required = false) MultipartFile avatarFile,
	                                             @RequestParam(name = "gender", required = false) String gender) throws Exception {
		enumGender gender1 = enumGender.MALE;
		switch (gender) {
			case "FEMALE" -> gender1 = enumGender.FEMALE;
			case "OTHER" -> gender1 = enumGender.OTHER;
		}
		LocalDate dobDate = null;
		if (dob != null && ! dob.isEmpty()) {
			dobDate = LocalDate.parse(dob);
		}
		return studentServices.updateStudent(mssv, current_semester, email, name, dobDate, address, avatarFile, gender1);
	}
}
