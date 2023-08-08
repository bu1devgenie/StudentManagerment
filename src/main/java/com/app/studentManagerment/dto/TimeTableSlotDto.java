package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.Attendence.Atten;

import java.time.LocalDate;

public class TimeTableSlotDto {
	private String roomName;
	private String className;
	private LocalDate sesionDate;
	private int slotOfDay;
	private String teacherName;
	private Atten attendence;

	public TimeTableSlotDto() {
	}

	public TimeTableSlotDto(String roomName, String className, LocalDate sesionDate, int slotOfDay, String teacherName, Atten attendence) {
		this.roomName = roomName;
		this.className = className;
		this.sesionDate = sesionDate;
		this.slotOfDay = slotOfDay;
		this.teacherName = teacherName;
		this.attendence = attendence;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Atten getAttendence() {
		return attendence;
	}

	public void setAttendence(Atten attendence) {
		this.attendence = attendence;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	@Override
	public String toString() {
		return "TimeTableSlotDto{" +
		       "roomName='" + roomName + '\'' +
		       ", attendence=" + attendence +
		       ", className='" + className + '\'' +
		       ", sesionDate=" + sesionDate +
		       ", slotOfDay=" + slotOfDay +
		       ", teacherName='" + teacherName + '\'' +
		       '}';
	}
}
