package com.app.studentManagerment.restController;

import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.services.CourseService;
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

	@GetMapping("/getAllCourse")
	public List<Course> getAllCourse() {
		return courseService.getAllCourse();
	}

	@PostMapping("/addCourse")
	public Course addCourse(@RequestParam(name = "name", required = false) String name,
	                        @RequestParam(name = "total_slot", required = false) int total_slot,
	                        @RequestParam(name = "course_semester", required = false) int course_semester,
	                        @RequestParam(name = "activity", required = false) boolean activity) {
		return courseService.addCourse(name, total_slot, course_semester, activity);

	}

	@PutMapping("/updateCourse")
	public Course updateCourse(@RequestParam(name = "id") long id,
	                           @RequestParam(name = "name", required = false) String name,
	                           @RequestParam(name = "total_slot", required = false) int total_slot,
	                           @RequestParam(name = "course_semester", required = false) int course_semester) {

		return courseService.updateCourse(id, name, total_slot, course_semester);

	}

	@DeleteMapping("/shutdownCourse")
	public Boolean shutdownCourse(@RequestParam(name = "id") long id) {
		return courseService.shutdownCourse(id);

	}

	@PostMapping("/searchCourse")
	public List<Course> searchByName(@RequestParam(name = "courseName") String courseName) {
		return courseService.searchByName(courseName);
	}

}
