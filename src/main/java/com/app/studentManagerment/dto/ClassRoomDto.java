package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import jakarta.persistence.*;

import java.util.List;

public class ClassRoomDto {
    private Long id;
    private String name;

    private Semester semester;

    private TeacherDto teacher;

    private Course course;

    private int currentSlot;

    private boolean Learning;

    private List<StudentDto> students;

    public ClassRoomDto(Long id, String name, Semester semester, TeacherDto teacher, Course course, int currentSlot, boolean learning, List<StudentDto> students) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.teacher = teacher;
        this.course = course;
        this.currentSlot = currentSlot;
        Learning = learning;
        this.students = students;
    }

    public ClassRoomDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    public boolean isLearning() {
        return Learning;
    }

    public void setLearning(boolean learning) {
        Learning = learning;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "ClassRoomDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                ", teacher=" + teacher +
                ", course=" + course +
                ", currentSlot=" + currentSlot +
                ", Learning=" + Learning +
                ", students=" + students +
                '}';
    }
}
