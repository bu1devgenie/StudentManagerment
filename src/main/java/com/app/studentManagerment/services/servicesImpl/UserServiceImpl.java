package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.UserRepository;
import com.app.studentManagerment.dto.UserDto;
import com.app.studentManagerment.dto.mapper.UserListMapper;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.user.User;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {
	private final GoogleService googleService;
	private final UserRepository userRepository;
	private final UserListMapper userListMapper;
	private final AccountRepository accountRepository;

	public UserServiceImpl(GoogleService googleService, UserRepository userRepository, UserListMapper userListMapper, AccountRepository accountRepository) {
		this.googleService = googleService;
		this.userRepository = userRepository;
		this.userListMapper = userListMapper;
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDto addUser(String name, LocalDate dob, String address, MultipartFile avatarFile) {
		// Tạo đối tượng Teacher và lưu vào database
		User user = new User();
		user.setName(name.replace("\\s+", " "));
		user.setDob(dob);
		user.setAddress(address.trim());
		userRepository.save(user);
		if (avatarFile != null) {
			addImage(user, avatarFile);
		}
		return userListMapper.UserToUserDto(user);

	}


	@Override
	public UserDto updateUser(String ms, String name, String address, LocalDate dob, MultipartFile avatar, String email) throws Exception {
		User user = userRepository.findUserByMs(ms);
		if (user != null) {
			if (email != null) {
				Account account = accountRepository.findByEmail(email);
				user.setAccount(account);
			}
			if (name != null) {
				user.setName(name);
			}
			if (dob != null) {
				user.setDob(dob);
			}
			if (address != null) {
				user.setAddress(address);
			}
			user = userRepository.save(user);
			if (avatar != null) {
				addImage(user, avatar);
			}
			return userListMapper.UserToUserDto(user);
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteUser(String ms) {
		User user = userRepository.findUserByMs(ms);
		if (user != null) {
			userRepository.delete(user);
			deleteImage(user);
			return true;
		}
		return false;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByAccount_Email(email);
	}

	@Override
	public Page<UserDto> searchUser(String ms, String name, String email, LocalDate dob, Pageable pageable) {
		Page<User> users = userRepository.search(ms, name, dob, email, pageable);
		return users.map(userListMapper::UserToUserDto);
	}

	@Async
	public void addImage(User user, MultipartFile avatar) {
		try {
			if (user.getAvatar() != null) {
				// tìm avatar cũ của teacher
				//  xóa nó đi
				if (user.getAvatar() != null && user.getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = user.getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
			}
			// thêm avatar mới vào
			String foderName = "SchoolManager/User";
			String avatarFileName = user.getMs() + "--" + user.getName().replace(" ", "") + ".jpg";
			String filedId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
			String livelink = googleService.getLiveLink(filedId);
			user.setAvatar(livelink);
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Async
	public void deleteImage(User user) {
		try {
			if (user.getAvatar() != null) {
				// tìm avatar cũ của teacher
				//  xóa nó đi
				if (user.getAvatar() != null && user.getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = user.getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
