package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.UserRepository;
import com.app.studentManagerment.dto.UserDto;
import com.app.studentManagerment.dto.mapper.UserListMapper;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.user.User;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.UserService;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class UserServiceImpl implements UserService {
	private GoogleService googleService;
	private UserRepository userRepository;
	private UserListMapper userListMapper;
	private AccountRepository accountRepository;


	public UserServiceImpl(GoogleService googleService, UserRepository userRepository, UserListMapper userListMapper, AccountRepository accountRepository) {
		this.googleService = googleService;
		this.userRepository = userRepository;
		this.userListMapper = userListMapper;
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDto addUser(String name, LocalDate dob, String address, MultipartFile avatarFile) {
		String foderName = "StudentManager/Teacher";
		String avatarFileName = "FPT-User--" + name.replace(" ", "") + ".jpg";

		CompletableFuture<String> futureLink = CompletableFuture.completedFuture(null);
		if (avatarFile != null) {
			// Nếu avatar khác null, tạo một CompletableFuture để tạo link từ file avatar
			futureLink = CompletableFuture.supplyAsync(() -> {
				try {
					String fileId = googleService.uploadFile(avatarFile, foderName, "anyone", "reader", avatarFileName);
					return googleService.getLiveLink(fileId);
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			});
		}

		// Tạo đối tượng Teacher và lưu vào database
		User user = new User();
		user.setName(name.replace("\\s+", " "));
		user.setDob(dob);
		user.setAddress(address.trim());
		List<Course> courses = new ArrayList<>();


		// Chỉ chờ kết quả tạo link nếu avatar khác null
		String liveLink = null;
		if (avatarFile != null) {
			try {
				liveLink = futureLink.get();
			} catch (InterruptedException | ExecutionException ex) {
				throw new RuntimeException(ex);
			} catch (java.util.concurrent.ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		user.setAvatar(liveLink);
		userRepository.save(user);
		return userListMapper.UserToUserDto(user);

	}

	@Override
	public Page<UserDto> searchUser(String searchText, String type, Pageable pageable) {
		Page<User> users = userRepository.search(searchText, type, pageable);
		Page<UserDto> userDtos = users.map(user -> userListMapper.UserToUserDto(user));
		return userDtos;
	}

	@Override
	public UserDto updateUser(Long id, String name, String address, LocalDate dob, MultipartFile avatar, String email) throws Exception {
		Optional<User> theUser = userRepository.findById(id);
		if (theUser != null) {
			if (email != null) {
				Account account = accountRepository.findByEmail(email);
				theUser.get().setAccount(account);
			}
			if (name != null) {
				theUser.get().setName(name);
			}
			if (dob != null) {
				theUser.get().setDob(dob);
			}
			if (address != null) {
				theUser.get().setAddress(address);
			}
			if (avatar != null) {
				// tìm avatar cũ của student
				//  xóa nó đi
				if (theUser.get().getAvatar() != null && theUser.get().getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = theUser.get().getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
				// thêm avatar mới vào
				String foderName = "StudentManager/User";
				String avatarFileName = "PFT-User--" + name.replace(" ", "") + ".jpg";
				CompletableFuture<String> futureLink = CompletableFuture.supplyAsync(() -> {
					try {
						String filedId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
						return googleService.getLiveLink(filedId);
					} catch (Exception e) {
						throw new CompletionException(e);
					}
				});
				// set vào thuộc tính để thêm vào db
				try {
					String livelink = futureLink.get();
					theUser.get().setAvatar(livelink);
				} catch (InterruptedException | ExecutionException | java.util.concurrent.ExecutionException ex) {
					throw new RuntimeException(ex);
				}
			}
		} else {
			return null;
		}
		userRepository.save(theUser.get());
		return userListMapper.UserToUserDto(theUser.get());
	}

	@Override
	public boolean deleteUser(long id) {
		Optional<User> user = userRepository.findById(id);
		if (user != null) {
			userRepository.delete(user.get());
			return true;
		}
		return false;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByAccount_Email(email);
	}
}
