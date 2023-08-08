package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
	List<String> getAllNameOfCourse();

	List<Course> getAllCourse();

	Course updateCourse( String name,Integer total_slot, Integer course_semester,boolean activity);

	Course findCourseByCourseName(String name);

	Boolean shutdownCourse(String name);

	Course addCourse(String name, int totalSlot, int courseSemester, boolean activity);

	List<Course> findCourseWithCurrentSemester(int currentSemester);

	Page<Course> searchCourse(String courseName, Integer total_slot, Integer course_semester, Boolean activity, Pageable pageable);

	Boolean checkCourse(String courseName);
}
