package com.app.studentManagerment.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;
    @OneToMany
    @JoinColumn(name = "slot_id")
    private List<Slot> slot;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;


    public Schedule() {
    }

    public Schedule(Long id, ClassRoom classRoom, List<Slot> slot, Semester semester) {
        this.id = id;
        this.classRoom = classRoom;
        this.slot = slot;
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public List<Slot> getSlot() {
        return slot;
    }

    public void setSlot(List<Slot> slot) {
        this.slot = slot;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", classRoom=" + classRoom +
                ", slot=" + slot +
                ", semester=" + semester +
                '}';
    }
}
