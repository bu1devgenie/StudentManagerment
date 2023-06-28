package com.app.studentManagerment.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;

    @Column
    private LocalDate sesionDate;

    @Column
    private int slotOfDay;

    public Session() {
    }

    public Session(Long id, Room room, ClassRoom classRoom, LocalDate sesionDate, int slotOfDay) {
        this.id = id;
        this.room = room;
        this.classRoom = classRoom;
        this.sesionDate = sesionDate;
        this.slotOfDay = slotOfDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public LocalDate getSesionDate() {
        return sesionDate;
    }

    public void setSesionDate(LocalDate sesionDate) {
        this.sesionDate = sesionDate;
    }

    public int getSlotOfDay() {
        return slotOfDay;
    }

    public void setSlotOfDay(int slotOfDay) {
        this.slotOfDay = slotOfDay;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", room=" + room +
                ", classRoom=" + classRoom +
                ", sesionDate=" + sesionDate +
                ", slotOfDay=" + slotOfDay +
                '}';
    }
}
