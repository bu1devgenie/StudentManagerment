package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.CourseRepository;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImplement implements CourseService {

	private CourseRepository courseRepository;
	private ClassroomRepository classroomRepository;

	@Autowired
	public CourseServiceImplement(CourseRepository courseRepository, ClassroomRepository classroomRepository) {
		this.courseRepository = courseRepository;
		this.classroomRepository = classroomRepository;
	}

	@Override
	public List<String> getAllNameOfCourse() {
		return courseRepository.getAllNameOfCourse();
	}

	@Override
	public List<Course> getAllCourse() {
		return courseRepository.findAll();
	}

	@Override
	public Course updateCourse(String name, Integer total_slot, Integer course_semester, boolean activity) {
		Course course = courseRepository.findByName(name);
		List<ClassRoom> classRooms = classroomRepository.findByCourse(course);
		for (ClassRoom classRoom : classRooms) {
			if (classRoom.isLearning()) {
				return course;
			}
		}
		if (course == null) {
			return null;
		}
		if (! name.isEmpty()) {
			course.setName(name);
		}
		if (total_slot > 0) {
			course.setTotalSlot(total_slot);
		}
		if (course_semester > 0 && course_semester <= 10) {
			course.setCourseSemester(course_semester);
		}
		course.setActivity(activity);
		return courseRepository.save(course);
	}

	@Override
	public Course findCourseByCourseName(String name) {
		return courseRepository.findByName(name);
	}

	@Override
	public Boolean shutdownCourse(String name) {
		Course course = courseRepository.findByName(name);
		if (course != null && course.isActivity()) {
			List<ClassRoom> classRooms = classroomRepository.findByCourse(course);
			for (ClassRoom classRoom : classRooms) {
				if (classRoom.isLearning()) {
					return false;
				}
			}
			course.setActivity(false);
			return courseRepository.save(course).isActivity();
		}
		return false;
	}

	@Override
	public Course addCourse(String name, int totalSlot, int courseSemester, boolean activity) {
		Course course = courseRepository.findByName(name);
		if (course != null) {
			return null;
		}
		course = new Course();
		course.setName(name);
		course.setTotalSlot(totalSlot);
		course.setActivity(activity);
		course.setCourseSemester(courseSemester);
		return courseRepository.save(course);
	}

	@Override
	public List<Course> findCourseWithCurrentSemester(int currentSemester) {
		return courseRepository.findAllByCourseSemester(currentSemester);
	}

	@Override
	public Page<Course> searchCourse(String courseName, Integer total_slot, Integer course_semester, Boolean activity, Pageable pageable) {

		return courseRepository.searchCourse(courseName, total_slot, course_semester, activity, pageable);
	}

	@Override
	public Boolean checkCourse(String courseName) {
		Course course = courseRepository.findByName(courseName);
		if (course != null) {
			return true;
		}
		return false;
	}


}
