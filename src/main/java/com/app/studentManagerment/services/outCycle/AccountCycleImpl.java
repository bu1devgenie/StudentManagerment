package com.app.studentManagerment.services.outCycle;

import com.app.studentManagerment.dto.AccountResponseDto;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.entity.user.User;
import com.app.studentManagerment.enumPack.enumRole;
import com.app.studentManagerment.services.AccountService;
import com.app.studentManagerment.services.StudentService;
import com.app.studentManagerment.services.TeacherService;
import com.app.studentManagerment.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountCycleImpl implements AccountCycle {
	private final AccountService accountService;
	private final StudentService studentService;
	private final TeacherService teacherService;
	private final UserService userService;

	public AccountCycleImpl(AccountService accountService, StudentService studentService, TeacherService teacherService, UserService userService) {
		this.accountService = accountService;
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.userService = userService;
	}


	@Override
	public AccountResponseDto ACCOUNT_RESPONSE_DTO(String email) {
		AccountResponseDto dto = new AccountResponseDto();
		dto.setEmail(email);
		List<Role> roles = accountService.getRoles(email);
		List<enumRole> rolesList = roles.stream().map(Role::getName).toList();

		User user = null;
		if (rolesList.contains(enumRole.Student)) {
			Student student = studentService.findStudentByEmail(email);
			dto.setName(student.getName());
			dto.setMs(student.getMssv());
			dto.setAvatar(student.getAvatar());
		} else if (rolesList.contains(enumRole.Teacher)) {
			Teacher teacher = teacherService.findTeacherByEmail(email);
			dto.setName(teacher.getName());
			dto.setMs(teacher.getMsgv());
			dto.setAvatar(teacher.getAvatar());
		} else if (rolesList.contains(enumRole.Hr) || rolesList.contains(enumRole.Admin) || rolesList.contains(enumRole.Principal)) {
			user = userService.findUserByEmail(email);
			dto.setName(user.getName());
			dto.setMs(user.getMs());
			dto.setAvatar(user.getAvatar());
		}
		return user != null ? dto : null;
	}
}
