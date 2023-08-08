package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.UserDto;
import com.app.studentManagerment.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserRestController {
	private UserService userService;

	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/addUser")
	public synchronized UserDto addUser(
			@RequestParam(name = "name") String name,
			@RequestParam(name = "dob") LocalDate dob,
			@RequestParam(name = "address") String address,
			@RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile) throws IOException, GeneralSecurityException {
//      =========================
		return userService.addUser(name, dob, address, avatarFile);
	}

	@PostMapping("/searchUser")
	public Page<UserDto> searchUser(
			@RequestParam(name = "ms", required = false) String ms,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "dob", required = false) LocalDate dob,
			@RequestParam(name = "targetPageNumber") Integer targetPageNumber
	) {
		if (targetPageNumber < 0) {
			return null;
		}

		Pageable pageable = PageRequest.of(targetPageNumber, 10);
		return userService.searchUser(ms, name, email, dob, pageable);
	}

	@PutMapping("/updateUser")
	public synchronized UserDto updateTeacher(
			@RequestParam(name = "ms") String ms,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "dob", required = false) LocalDate dob,
			@RequestParam(name = "avatar", required = false) MultipartFile avatar,
			@RequestParam(name = "email", required = false) String email) throws Exception {
		return userService.updateUser(ms, name, address, dob, avatar, email);
	}

	@DeleteMapping("/deleteUser")
	public synchronized boolean deleteUser(@RequestParam(name = "ms") String ms) {
		return userService.deleteUser(ms);
	}

}
