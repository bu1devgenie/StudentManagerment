package com.app.studentManagerment.dto;

import java.time.LocalDate;

public class UserDto {
    private long id;
    private String name;
    private LocalDate dob;
    private String address;
    private String avatar;
    private String email;

    public UserDto() {
    }

    public UserDto(long id, String name, LocalDate dob, String address, String avatar, String email) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.avatar = avatar;
        this.email = email;
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

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
