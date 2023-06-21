package com.app.studentManagerment.dto;

public class CourseDto {
    private Long id;
    private String name;
    private int courseSemester;
    private int totalSlot;

    public CourseDto(Long id, String name, int courseSemester, int totalSlot) {
        this.id = id;
        this.name = name;
        this.courseSemester = courseSemester;
        this.totalSlot = totalSlot;
    }

    public CourseDto() {
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

    @Override
    public String toString() {
        return "CourseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseSemester=" + courseSemester +
                ", totalSlot=" + totalSlot +
                '}';
    }
}
