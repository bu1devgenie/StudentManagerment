package com.app.studentManagerment.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "slot")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "slotOfDay")
    private int slotOfDay;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "start_time")
    private LocalTime start_time;

    @Column(name = "end_time")
    private LocalTime end_time;

    @Column(name = "day_of_week")
    private int dayOfWeak;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(8) DEFAULT 'available'")
    private SlotStatus status;

    public enum SlotStatus {
        AVAILABLE, RESERVED
    }


    public Slot() {
    }

    public Slot(long id, int slotOfDay, Room room, LocalTime start_time, LocalTime end_time, int dayOfWeak, SlotStatus status) {
        this.id = id;
        this.slotOfDay = slotOfDay;
        this.room = room;
        this.start_time = start_time;
        this.end_time = end_time;
        this.dayOfWeak = dayOfWeak;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSlotOfDay() {
        return slotOfDay;
    }

    public void setSlotOfDay(int slotOfDay) {
        this.slotOfDay = slotOfDay;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public int getDayOfWeak() {
        return dayOfWeak;
    }

    public void setDayOfWeak(int dayOfWeak) {
        this.dayOfWeak = dayOfWeak;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", slotOfDay=" + slotOfDay +
                ", room=" + room +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", dayOfWeak=" + dayOfWeak +
                ", status=" + status +
                '}';
    }
}
