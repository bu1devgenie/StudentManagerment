package com.app.studentManagerment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false,unique = true)
    private String name;
    @Column(name = "courseSemester", nullable = false)
    private int courseSemester;

    @Column(name = "totalSlot", nullable = false)
    private int totalSlot;

    @Column(name = "activity", nullable = false)
    private boolean activity;


    public Course() {
    }

    public Course(Long id, String name, int courseSemester, int totalSlot, boolean activity) {
        this.id = id;
        this.name = name;
        this.courseSemester = courseSemester;
        this.totalSlot = totalSlot;
        this.activity = activity;
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

    public int getCourseSemester() {
        return courseSemester;
    }

    public void setCourseSemester(int courseSemester) {
        this.courseSemester = courseSemester;
    }

    public int getTotalSlot() {
        return totalSlot;
    }

    public void setTotalSlot(int totalSlot) {
        this.totalSlot = totalSlot;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }


    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseSemester=" + courseSemester +
                ", totalSlot=" + totalSlot +
                ", activity=" + activity +
                '}';
    }
}
