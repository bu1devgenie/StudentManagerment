package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query("""
			select c.name from Course c where c.name is not null order by c.name asc
			""")
	List<String> getAllNameOfCourse();

	Course findByName(String name);

	Course findById(long id);

	List<Course> findAllByCourseSemester(int courseSemester);

	@Query("""
			SELECT c
						FROM Course c
						WHERE :courseName IS NULL OR c.name LIKE CONCAT('%', :courseName, '%')
			""")
	List<Course> searchByName(@Param("courseName") String courseName);
}
