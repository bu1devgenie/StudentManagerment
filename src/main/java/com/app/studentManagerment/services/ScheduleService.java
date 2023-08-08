package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.TimeTableSlotDto;
import com.app.studentManagerment.entity.Requests;
import com.app.studentManagerment.enumPack.enumRole;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
	void autoGenerateSchedule(String semesterName, Requests requests);

	List<TimeTableSlotDto> getScheduleOfWeek(LocalDate startWeek, LocalDate endWeek, List<enumRole> roles,String ms);
}
