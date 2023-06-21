package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.Course;

import java.time.LocalDate;
import java.util.List;

public class TeacherDto {
    private long id;
    private String msgv;
    private String name;
    private String address;
    private LocalDate dob;
    private String avatar;
    private List<CourseDto> course;
    private String email;

    public TeacherDto() {
    }

    public TeacherDto(long id, String msgv, String name, String address, LocalDate dob, String avatar, List<CourseDto> course, String email) {
        this.id = id;
        this.msgv = msgv;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.avatar = avatar;
        this.course = course;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgv() {
        return msgv;
    }

    public void setMsgv(String msgv) {
        this.msgv = msgv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<CourseDto> getCourse() {
        return course;
    }

    public void setCourse(List<CourseDto> course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "TeacherDto{" +
                "id=" + id +
                ", msgv='" + msgv + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dob=" + dob +
                ", avatar='" + avatar + '\'' +
                ", course=" + course +
                ", email='" + email + '\'' +
                '}';
    }
}
