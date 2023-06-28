package com.app.studentManagerment.entity.user;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.enumPack.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "avatar", nullable = false)
    private String avatar;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public User() {
    }

    public User(long id, String name, String address, LocalDate dob, String avatar, Account account, Gender gender) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.avatar = avatar;
        this.account = account;
        this.gender = gender;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", dob=" + dob +
               ", avatar='" + avatar + '\'' +
               ", account=" + account +
               ", gender=" + gender +
               '}';
    }
}
