package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.dto.TimeClassDto;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.services.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassRoomRestController {
	private ClassRoomService classRoomService;
	private SemesterService semesterService;
	private TeacherService teacherService;
	private CourseService coursesService;
	private StudentService studentService;

	public ClassRoomRestController(ClassRoomService classRoomService, SemesterService semesterService, TeacherService teacherService, CourseService coursesService, StudentService studentService) {
		this.classRoomService = classRoomService;
		this.semesterService = semesterService;
		this.teacherService = teacherService;
		this.coursesService = coursesService;
		this.studentService = studentService;
	}


	@GetMapping("/findAll")
	public Page<ClassRoomDto> findAll() {
		return classRoomService.getAll();
	}


	@PostMapping("/searchClassRoom")
	public Page<ClassRoomDto> searchStudent(@RequestParam(name = "semester_name", required = false) String semester_name,
	                                        @RequestParam(name = "msgv", required = false) String msgv,
	                                        @RequestParam(name = "courseName", required = false) String courseName,
	                                        @RequestParam(name = "currentSlot", required = false) String currentSlot,
	                                        @RequestParam(name = "learning", required = false) boolean learning
	) {
		return classRoomService.searchClassRoom(semester_name, msgv, courseName, currentSlot, learning);
	}

	@PostMapping("/newClassRoom")
	public synchronized ClassRoom newClassRoom(@RequestParam(name = "semester_name", required = false) String semester_name,
	                                           @RequestParam(name = "msgv", required = false) String msgv,
	                                           @RequestParam(name = "courseName", required = false) String courseName,
	                                           @RequestParam(name = "currentSlot", required = false) String currentSlot,
	                                           @RequestParam(name = "learning", required = false) boolean learning,
	                                           @RequestParam(name = "students") List<String> students) {
		Semester semester = semesterService.getSemesterByName(semester_name);
		Teacher teacher = teacherService.getTeacherByMsgv(msgv);
		Course course = coursesService.findCourseByCourseName(courseName);
		List<Student> studentList = null;
		for (String mssv : students) {
			Student s = studentService.findByMssv(mssv);
			if (s != null) {
				studentList.add(s);
			}
		}
		return classRoomService.newClassRoom(semester, teacher, course, 0, learning, studentList);
	}

	@PutMapping("/updateClassRoom")
	public synchronized ClassRoom updateClassRoom(@RequestParam(name = "className", required = false) String className,
	                                              @RequestParam(name = "msgv", required = false) String msgv,
	                                              @RequestParam(name = "courseName", required = false) String courseName,
	                                              @RequestParam(name = "currentSlot", required = false) int currentSlot,
	                                              @RequestParam(name = "learning", required = false) boolean learning,
	                                              @RequestParam(name = "students") List<String> students) {
		List<Student> studentList = null;
		for (String mssv : students) {
			Student s = studentService.findByMssv(mssv);
			if (s != null) {
				studentList.add(s);
			}
		}
		return classRoomService.updateClassRoom(className, msgv, courseName, currentSlot, studentList, learning);
	}

	@DeleteMapping("/shutdownClassRoom")
	public synchronized boolean shutdownClassRoom(@RequestParam(name = "className", required = false) String className,
	                                              @RequestParam(name = "learning", required = false) boolean learning) {
		return classRoomService.ShutdownClassRoom(className, learning);
	}

	@PostMapping("/getTimeClass")
	public List<TimeClassDto> getTimeClass(@RequestParam(name = "className", required = false) String className
	) {
		return classRoomService.getTimeClass(className);
	}

	@PostMapping("/getClassRoomWithCourse")
	public List<String> getClassRoomWithCourse(@RequestParam(name = "courseName") String courseName
	) {
		return classRoomService.getClassRoomForRegister(courseName);
	}
}
