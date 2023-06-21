package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.user.Teacher;

import java.util.List;

public class Course_TeacherDto {
    private Course course;
    private List<Teacher> teacherList;

    public Course_TeacherDto() {
    }

    public Course_TeacherDto(Course course, List<Teacher> teacherList) {
        this.course = course;
        this.teacherList = teacherList;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    @Override
    public String toString() {
        return "Course_Teacher{" +
                "course=" + course +
                ", teacherList=" + teacherList +
                '}';
    }
}
