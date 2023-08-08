package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.StudentRepository;
import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.mapper.StudentListMapper;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.enumPack.enumGender;
import com.app.studentManagerment.services.AccountService;
import com.app.studentManagerment.services.GoogleService;
import com.app.studentManagerment.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final GoogleService googleService;
	private final StudentListMapper studentListMapper;
	private final AccountRepository accountRepository;
	private final ClassroomRepository classroomRepository;
	private final AccountService accountService;


	public StudentServiceImpl(StudentRepository studentRepository, GoogleService googleService, StudentListMapper studentListMapper, AccountRepository accountRepository, ClassroomRepository classroomRepository, AccountService accountService) {
		this.studentRepository = studentRepository;
		this.googleService = googleService;
		this.studentListMapper = studentListMapper;
		this.accountRepository = accountRepository;
		this.classroomRepository = classroomRepository;
		this.accountService = accountService;
	}


	@Override
	public Student findStudentByEmail(String email) {
		return studentRepository.findStudentByAccount_Email(email);
	}

	@Override
	public Student findById(long id) {
		Optional<Student> result = studentRepository.findById(id);
		Student student = null;
		if (result.isPresent()) {
			student = result.get();
		} else {
			throw new RuntimeException("Did not find Student");
		}
		return student;

	}

	@Override
	public Student findByMssv(String mssv) {
		return studentRepository.findByMssv(mssv);
	}

	@Override
	public Student addStudent(int current_semester, String name, LocalDate dob, String address, MultipartFile avatarFile, enumGender enumGender, String email) {
		String mssv = getMSSV();
		// Tạo đối tượng Student và lưu vào database
		Student theStudent = new Student();
		theStudent.setMssv(mssv.trim());
		theStudent.setCurrentSemester(current_semester);
		theStudent.setName(name);
		theStudent.setDob(dob);
		theStudent.setAddress(address.trim());
		theStudent.setGender(enumGender);
		if (email != null) {
			if (! accountRepository.emailIsConnected(email)) {
				theStudent.setAccount(accountRepository.findByEmail(email));
			}
		}
		theStudent = studentRepository.save(theStudent);
		if (avatarFile != null) {
			addImage(theStudent, avatarFile);
		}
		return theStudent;
	}

	@Override
	public StudentDto updateStudent(String mssv, int current_semester, String mail, String name, LocalDate dob, String address, MultipartFile avatarFile, enumGender enumGender) {
		Student theStudent = studentRepository.findByMssv(mssv);
		if (theStudent != null) {
			if (current_semester > 0) {
				theStudent.setCurrentSemester(current_semester);
			}
			if (mail != null) {
				Account account = accountRepository.findByEmail(mail);
				theStudent.setAccount(account);
			}
			if (name != null) {
				theStudent.setName(name);
			}
			if (dob != null) {
				theStudent.setDob(dob);
			}
			if (address != null) {
				theStudent.setAddress(address);
			}
			if (enumGender != null) {
				theStudent.setGender(enumGender);
			}

		} else {
			return null;
		}
		theStudent = studentRepository.save(theStudent);
		if (! avatarFile.isEmpty()) {
			addImage(theStudent, avatarFile);
		}
		return studentListMapper.StudentToStudentDto(theStudent);
	}

	@Override
	public boolean deleteStudent(String mssv) {
		Student student = studentRepository.findByMssv(mssv);
		if (student != null) {
			if (! student.getAvatar().isEmpty()) {
				deleteImage(student);
			}
			List<ClassRoom> classRoomsOfStudent = classroomRepository.findByStudents(student);
			if (! classRoomsOfStudent.isEmpty()) {
				for (ClassRoom c : classRoomsOfStudent) {
					c.getStudents().remove(student);
				}
			}
			studentRepository.delete(student);
			return true;
		}
		return false;
	}

	@Override
	public String getMSSV() {
		if (studentRepository.count() > 0) {
			Student student = studentRepository.findFirstByOrderByIdDesc();
			String mssv = student.getMssv().split("-")[2];

			int numberofMssv = 0;
			try {
				numberofMssv = Integer.parseInt(mssv) + 1;
			} catch (NumberFormatException e) {
				return null;
			}
			String formattedNum = String.format("%05d", numberofMssv);
			return "FPT-Student-" + formattedNum;
		} else {
			return "FPT-Student-" + "00001";
		}
	}

	@Override
	public Page<StudentDto> search(String mssv, Integer semester, String name, LocalDate dob, String email, Pageable pageable) {
		Page<StudentDto> studentDtos = studentRepository.search(mssv, semester, name, dob, email, pageable);
		return studentDtos;
	}

	@Override
	public List<StudentDto> getAllStudentWithCurrentSemester(int currentSemester) {
		List<StudentDto> studentDtos = studentRepository.findAllByCurrentSemester(currentSemester).stream().map(studentListMapper::StudentToStudentDto).toList();
		return studentDtos;
	}

	@Override
	public Integer totalStudentWithCurrentSemester(int currentSemester) {
		return studentRepository.countStudentByCurrentSemester(currentSemester);
	}

	@Async
	public void addImage(Student student, MultipartFile avatar) {
		try {
			if (student.getAvatar() != null) {
				// tìm avatar cũ của teacher
				//  xóa nó đi
				if (student.getAvatar() != null && student.getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = student.getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
			}
			// thêm avatar mới vào
			String foderName = "SchoolManager/Student";
			String avatarFileName = student.getMssv() + "--" + student.getName().replace(" ", "") + ".jpg";
			String filedId = googleService.uploadFile(avatar, foderName, "anyone", "reader", avatarFileName);
			String livelink = googleService.getLiveLink(filedId);
			student.setAvatar(livelink);
			studentRepository.save(student);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Async
	public void deleteImage(Student student) {
		try {
			if (student.getAvatar() != null) {
				// tìm avatar cũ của teacher
				//  xóa nó đi
				if (student.getAvatar() != null && student.getAvatar().contains("https://drive.google.com/uc?id=")) {
					String fileId = student.getAvatar().substring(31);
					googleService.deleteFileOrFolder(fileId);
				}
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
