package com.app.studentManagerment.entity;

import com.app.studentManagerment.entity.user.Student;
import jakarta.persistence.*;

@Entity
@Table(name = "attendence")
public class Attendence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Enumerated(EnumType.STRING)
    private Atten atten;

    public enum Atten {
        absent,
        attended,
        not_yet
    }

    public Attendence() {
    }

    public Attendence(Long id, Student student, Session session, Atten atten) {
        this.id = id;
        this.student = student;
        this.session = session;
        this.atten = atten;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Atten getAtten() {
        return atten;
    }

    public void setAtten(Atten atten) {
        this.atten = atten;
    }

    @Override
    public String toString() {
        return "Attendence{" +
                "id=" + id +
                ", student=" + student +
                ", session=" + session +
                ", atten=" + atten +
                '}';
    }
}
