package com.app.studentManagerment.entity;

import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.entity.user.Teacher;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "classroom")

public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "current_slot")
    private int currentSlot;

    @Column(name = "isLearning")
    private boolean Learning;
    @ManyToMany
    @JoinTable(name = "student_classroom",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    public ClassRoom() {
    }

    public ClassRoom(Long id, String name, Semester semester, Teacher teacher, Course course, int currentSlot, boolean learning, List<Student> students) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.teacher = teacher;
        this.course = course;
        this.currentSlot = currentSlot;
        Learning = learning;
        this.students = students;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
