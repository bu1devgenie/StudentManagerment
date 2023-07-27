package com.app.studentManagerment.entity.user;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.enumPack.enumGender;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "teacher")

public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "msgv", nullable = false,unique = true)
    private String msgv;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "avatar", nullable = true)
    private String avatar;
    @Enumerated(EnumType.STRING)
    private enumGender enumGender;




    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    @ManyToMany()
    @JoinTable(name = "teacher_course",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> course;

    @OneToOne
    @JoinColumn(name = "email")
    private Account account;

    public Teacher() {
    }

    public Teacher(String msgv, String name, String address, LocalDate dob, String avatar, enumGender enumGender, List<Course> course, Account account) {
        this.msgv = msgv;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.avatar = avatar;
        this.enumGender = enumGender;
        this.course = course;
        this.account = account;
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

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public enumGender getGender() {
        return enumGender;
    }

    public void setGender(enumGender enumGender) {
        this.enumGender = enumGender;
    }

    @Override
    public String toString() {
        return "Teacher{" +
               "id=" + id +
               ", msgv='" + msgv + '\'' +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", dob=" + dob +
               ", avatar='" + avatar + '\'' +
               ", gender=" + enumGender +
               ", course=" + course +
               ", account=" + account +
               '}';
    }
}
