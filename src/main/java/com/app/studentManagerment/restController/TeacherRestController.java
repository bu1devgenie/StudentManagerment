package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.enumPack.enumGender;
import com.app.studentManagerment.services.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherRestController {
	private final TeacherService teacherService;

	public TeacherRestController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping("/getMSGV")
	public String getMSGV() {
		return teacherService.getMSGV();
	}

	@PostMapping("/addTeacher")
	public TeacherDto addTeacher(@RequestParam(name = "course", required = false) List<String> courses,
	                             @RequestParam(name = "name") String name,
	                             @RequestParam(name = "dob") LocalDate dob,
	                             @RequestParam(name = "address") String address,
	                             @RequestParam(name = "avatarFile", required = false) MultipartFile avatar,
	                             @RequestParam(name = "gender") String gender,
	                             @RequestParam(name = "email", required = false) String email) {
		enumGender gender1 = enumGender.MALE;
		switch (gender) {
			case "FEMALE" -> gender1 = enumGender.FEMALE;
			case "OTHER" -> gender1 = enumGender.OTHER;
		}
		return teacherService.addTeacher(courses, name, dob, address, avatar, gender1, email);
	}

	@PostMapping("/searchTeacher")
	public Page<TeacherDto> searchTeacher(@RequestParam(name = "msgv", required = false) String msgv,
	                                      @RequestParam(name = "name", required = false) String name,
	                                      @RequestParam(name = "dob", required = false) LocalDate dob,
	                                      @RequestParam(name = "address", required = false) String address,
	                                      @RequestParam(name = "email", required = false) String email,
	                                      @RequestParam(name = "course", required = false) List<String> courses,
	                                      @RequestParam(name = "targetPageNumber") Integer targetPageNumber) {
		if (targetPageNumber < 0) {
			return null;
		}
		Pageable pageable = PageRequest.of(targetPageNumber, 10);
		return teacherService.search(msgv, name, dob, address, email, courses, pageable);
	}

	@PutMapping("/updateTeacher")
	public synchronized TeacherDto updateTeacher(@RequestParam(name = "msgvUpdate") String msgvUpdate,
	                                             @RequestParam(name = "name", required = false) String name,
	                                             @RequestParam(name = "address", required = false) String address,
	                                             @RequestParam(name = "dob", required = false) LocalDate dob,
	                                             @RequestParam(name = "avatar", required = false) MultipartFile avatar,
	                                             @RequestParam(name = "course", required = false) List<String> courses,
	                                             @RequestParam(name = "email", required = false) String email,
	                                             @RequestParam(name = "gender") String gender) throws Exception {
		enumGender gender1 = enumGender.MALE;
		switch (gender) {
			case "FEMALE" -> gender1 = enumGender.FEMALE;
			case "OTHER" -> gender1 = enumGender.OTHER;
		}
		return teacherService.updateTeacher(msgvUpdate, name, address, dob, avatar, courses, email, gender1);
	}

	@DeleteMapping("/deleteTeacher")
	public synchronized boolean deleteTeacher(@RequestParam(name = "msgv") String msgv) {
		return teacherService.deleteTeacher(msgv);
	}
}
