package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.enumPack.Gender;

import java.time.LocalDate;


public class StudentDto {
    private long id;
    private String mssv;
    private int currentSemester;
    private String name;
    private LocalDate dob;
    private String address;
    private String avatar;
    private String email;
    private Gender gender;

    public StudentDto() {
    }

    public StudentDto(long id, String mssv, int currentSemester, String name, LocalDate dob, String address, String avatar, String email, Gender gender) {
        this.id = id;
        this.mssv = mssv;
        this.currentSemester = currentSemester;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.avatar = avatar;
        this.email = email;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "StudentDto{" +
               "id=" + id +
               ", mssv='" + mssv + '\'' +
               ", currentSemester=" + currentSemester +
               ", name='" + name + '\'' +
               ", dob=" + dob +
               ", address='" + address + '\'' +
               ", avatar='" + avatar + '\'' +
               ", email='" + email + '\'' +
               ", gender=" + gender +
               '}';
    }
}
