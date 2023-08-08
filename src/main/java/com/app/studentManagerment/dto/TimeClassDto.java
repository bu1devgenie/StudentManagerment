package com.app.studentManagerment.dto;

public class TimeClassDto {
	private String Day;
	private int slotOfDay;
	private String teacherName;
	private String className;
	private String roomName;

	public TimeClassDto() {
	}

	public TimeClassDto(String day, int slotOfDay, String teacherName, String className, String roomName) {
		Day = day;
		this.slotOfDay = slotOfDay;
		this.teacherName = teacherName;
		this.className = className;
		this.roomName = roomName;
	}

	public String getDay() {
		return Day;
	}

	public void setDay(String day) {
		Day = day;
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@Override
	public String toString() {
		return Day + " :Slot: " + slotOfDay + " - Room: "+roomName+" - Lecture: "+teacherName;
	}
}
