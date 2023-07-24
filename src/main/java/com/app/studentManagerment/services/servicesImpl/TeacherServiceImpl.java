package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.CourseRepository;
import com.app.studentManagerment.dao.TeacherRepository;
import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.dto.mapper.TeacherListMapper;
import com.app.studentManagerment.entity.*;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.enumPack.enumGender;
import com.app.studentManagerment.enumPack.enumRole;
import com.app.studentManagerment.services.AccountService;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
	private TeacherRepository teacherRepository;
	private GoogleService googleService;
	private CourseRepository courseRepository;
	private TeacherListMapper teacherListMapper;
	private AccountRepository accountRepository;
	private ClassroomRepository classroomRepository;
	private AccountService accountService;

	@Autowired
	public TeacherServiceImpl(TeacherRepository teacherRepository, GoogleService googleService, CourseRepository courseRepository, TeacherListMapper teacherListMapper, AccountRepository accountRepository, ClassroomRepository classroomRepository, AccountService accountService) {
		this.teacherRepository = teacherRepository;
		this.googleService = googleService;
		this.courseRepository = courseRepository;
		this.teacherListMapper = teacherListMapper;
		this.accountRepository = accountRepository;
		this.classroomRepository = classroomRepository;
		this.accountService = accountService;
	}


	@Override
	public Page<TeacherDto> search(String searchText, String type, Pageable pageable) {
		Page<Teacher> teachers = teacherRepository.search(searchText, type, pageable);
		Page<TeacherDto> teacherDtos = teachers.map(teacher -> teacherListMapper.teacherToTeacherDTO(teacher));
		return teacherDtos;
	}

	@Override
	public Teacher getTeacherByMsgv(String msgv) {
		return teacherRepository.findByMsgv(msgv);
	}

	@Override
	public String getMSGV() {
		if (teacherRepository.count() > 0) {
			Teacher teacher = teacherRepository.findFirstByOrderByIdDesc();
			String msgv = teacher.getMsgv().split("-")[2];

			int numberofMgsv = 0;
			try {
				numberofMgsv = Integer.parseInt(msgv) + 1;
			} catch (NumberFormatException e) {
				return null;
			}
			String formattedNum = String.format("%05d", numberofMgsv);
			return "FPT-Teacher-" + formattedNum;
		} else {
			return "FPT-Teacher-" + "00001";
		}
	}

	@Override
	public TeacherDto addTeacher(List<String> course, String name, LocalDate dob, String address, MultipartFile avatar, enumGender enumGender) {
		String msgv = getMSGV();
		Teacher theTeacher = new Teacher();
		theTeacher.setMsgv(msgv.trim());
		theTeacher.setName(name.replace("\\s+", " "));
		theTeacher.setDob(dob);
		theTeacher.setAddress(address.trim());
		List<Course> courses = new ArrayList<>();
		if (! courses.isEmpty()) {
			for (String s : course) {
				courses.add(courseRepository.findByName(s));
			}
			theTeacher.setCourse(courses);
		} else {
			theTeacher.setCourse(null);
		}
		Account a = accountService.autoCreateAccount(name, theTeacher.getMsgv(), enumRole.Teacher);
		if (a != null) {
			theTeacher.setAccount(a);
		}
		theTeacher = teacherRepository.save(theTeacher);
		addImage(theTeacher, avatar);
		return teacherListMapper.teacherToTeacherDTO(theTeacher);

	}


	@Override
	public TeacherDto updateTeacher(String msgvUpdate, String name, String address, LocalDate dob, MultipartFile avatar, List<String> courses, String email, enumGender enumGender) throws Exception {
		Teacher teacher = teacherRepository.findByMsgv(msgvUpdate);
		if (teacher != null) {
			if (name != null) {
				teacher.setName(name);
			}
			if (address != null) {
				teacher.setAddress(address);
			}
			if (dob != null) {
				teacher.setDob(dob);
			}
			if (courses != null) {
				if (teacher.getCourse() != null) {
					for (String c : courses) {
						teacher.getCourse().add(courseRepository.findByName(c));
					}
				} else {
					List<Course> newCourses = new ArrayList<>();
					for (String s : courses) {
						newCourses.add(courseRepository.findByName(s));
					}
					teacher.setCourse(newCourses);
				}
			}
			if (email != null) {
				Account account = accountRepository.findByEmail(email);
				if (account != null) {
					if (!accountRepository.emailIsConnected(account.getEmail())) {
						teacher.setAccount(account);
					} else {
						teacher.setAccount(null);
					}
				}
			}
			teacher = teacherRepository.save(teacher);
			if (avatar != null) {
				addImage(teacher, avatar);
			}
			return teacherListMapper.teacherToTeacherDTO(teacher);
		}
		return null;
	}

	@Override
	public boolean deleteTeacher(String msgv) {
		Teacher teacher = teacherRepository.findByMsgv(msgv);
		if (teacher != null) {
			List<ClassRoom> classRoomsOfTeacher = classroomRepository.findByTeacher(teacher);
			if (! classRoomsOfTeacher.isEmpty()) {
				for (ClassRoom classRoom : classRoomsOfTeacher) {
					classRoom.setTeacher(null);
				}
			}
			teacherRepository.delete(teacher);
			return true;
		}
		return false;
	}

	@Override
	public List<Teacher> getAllTeacherCanTakeClasses(long semesterId, long courseId, int[] dayOfWeak, int[] slot_of_day) {
		return null;
	}

	@Override
	public Teacher findTeacherByEmail(String email) {
		return teacherRepository.findTeacherByAccount_Email(email);
	}

	@Async
	public void addImage(Teacher theTeacher, MultipartFile avatar) {
		try {
			if (theTeacher.getAvatar() != null) {
				// tìm avatar cũ của teacher
				//  xóa nó đi
				if (theTeacher.getAvatar() != null && theTeacher.getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = theTeacher.getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
				// thêm avatar mới vào
				String foderName = "SchoolManager/Teacher";
				String avatarFileName = theTeacher.getMsgv() + "--" + theTeacher.getName().replace(" ", "") + ".jpg";

				String filedId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
				String livelink = googleService.getLiveLink(filedId);
				theTeacher.setAvatar(livelink);
				teacherRepository.save(theTeacher);
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}