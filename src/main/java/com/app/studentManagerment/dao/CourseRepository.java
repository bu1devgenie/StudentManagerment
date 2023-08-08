package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
						WHERE (:courseName like '' or :courseName IS NULL OR c.name LIKE CONCAT('%', :courseName, '%'))
						and ( :totalSlot is null or c.totalSlot=:totalSlot)
						and ( :courseSemester is null or c.courseSemester=:courseSemester)
						and  (:activity is null or c.activity=:activity)
			""")
	Page<Course> searchCourse(@Param("courseName") String courseName,
	                          @Param("totalSlot") Integer totalSlot,
	                          @Param("courseSemester") Integer courseSemester,
	                          @Param("activity") Boolean activity,
	                          Pageable pageable);
}
