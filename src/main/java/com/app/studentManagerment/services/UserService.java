package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.UserDto;
import com.app.studentManagerment.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface UserService {
	UserDto addUser(String name, LocalDate dob, String address, MultipartFile avatarFile);


	UserDto updateUser(String ms, String name, String address, LocalDate dob, MultipartFile avatar, String email) throws Exception;

	boolean deleteUser(String ms);

	User findUserByEmail(String email);

	Page<UserDto> searchUser(String ms, String name, String email, LocalDate dob, Pageable pageable);
}
