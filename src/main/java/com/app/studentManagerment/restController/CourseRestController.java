package com.app.studentManagerment.restController;

import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.services.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseRestController {
	private CourseService courseService;

	public CourseRestController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/getAllCourseName")
	public List<String> getAllCourseName() {
		return courseService.getAllNameOfCourse();
	}


	@PostMapping("/addCourse")
	public Course addCourse(@RequestParam(name = "courseName") String name,
	                        @RequestParam(name = "total_slot") Integer total_slot,
	                        @RequestParam(name = "course_semester") Integer course_semester,
	                        @RequestParam(name = "activity") boolean activity) {
		return courseService.addCourse(name, total_slot, course_semester, activity);

	}

	@PutMapping("/updateCourse")
	public Course updateCourse(@RequestParam(name = "courseName") String name,
	                           @RequestParam(name = "total_slot") Integer total_slot,
	                           @RequestParam(name = "course_semester") Integer course_semester,
	                           @RequestParam(name = "activity") Boolean activity) {

		return courseService.updateCourse(name, total_slot, course_semester, activity);

	}

	@PostMapping("/shutdownCourse")
	public Boolean shutdownCourse(@RequestParam(name = "name") String name) {
		return courseService.shutdownCourse(name);
	}

	@PostMapping("/searchCourse")
	public Page<Course> searchByName(@RequestParam(name = "courseName", required = false) String courseName,
	                                 @RequestParam(name = "total_slot", required = false) Integer total_slot,
	                                 @RequestParam(name = "courseSemester", required = false) Integer course_semester,
	                                 @RequestParam(name = "activity", required = false) Boolean activity,
	                                 @RequestParam(name = "targetPageNumber") Integer targetPageNumber
	) {
		if (targetPageNumber < 0) {
			return null;
		}
		Pageable pageable = PageRequest.of(targetPageNumber, 20);
		return courseService.searchCourse(courseName, total_slot, course_semester, activity, pageable);
	}

	@PostMapping("/checkCourse")
	public Boolean checkCourse(@RequestParam(name = "courseName", required = false) String courseName
	) {
		boolean a=courseService.checkCourse(courseName);
		return courseService.checkCourse(courseName);
	}

}
