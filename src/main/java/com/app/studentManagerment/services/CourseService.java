package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Course;

import java.util.List;

public interface CourseService {
    List<String> getAllNameOfCourse();
    List<Course> getAllCourse();

    Course updateCourse(long id, String name, int totalSlot, int courseSemester);


    Boolean shutdownCourse(long id);

    Course addCourse(String name, int totalSlot, int courseSemester, boolean activity);

    List<Course> findCourseWithCurrentSemester(int currentSemester);

}
