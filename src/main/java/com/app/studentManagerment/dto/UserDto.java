package com.app.studentManagerment.dto;

import com.app.studentManagerment.enumPack.enumGender;

import java.time.LocalDate;

public class UserDto {
    private long id;
    private String name;
    private LocalDate dob;
    private String address;
    private String avatar;
    private String email;
    private enumGender enumGender;

    public UserDto() {
    }

    public UserDto(long id, String name, LocalDate dob, String address, String avatar, String email, enumGender enumGender) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.avatar = avatar;
        this.email = email;
        this.enumGender = enumGender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public enumGender getGender() {
        return enumGender;
    }

    public void setGender(enumGender enumGender) {
        this.enumGender = enumGender;
    }

    @Override
    public String toString() {
        return "UserDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", dob=" + dob +
               ", address='" + address + '\'' +
               ", avatar='" + avatar + '\'' +
               ", email='" + email + '\'' +
               ", gender=" + enumGender +
               '}';
    }
}
