package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.TimeTableSlotDto;
import com.app.studentManagerment.entity.Requests;
import com.app.studentManagerment.enumPack.enumRole;
import com.app.studentManagerment.enumPack.enumStatus;
import com.app.studentManagerment.services.RequestService;
import com.app.studentManagerment.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleRestController {
	private final ScheduleService scheduleService;
	private final RequestService requestService;

	public ScheduleRestController(ScheduleService scheduleService, RequestService requestService) {
		this.scheduleService = scheduleService;
		this.requestService = requestService;
	}

	@PostMapping("/autoGenerateSchedule")
	public Requests autoGenerateSchedule(@RequestParam("SemesterName") String semesterName) {
		Requests request = requestService.findRequestWithDescrip("generateSchedule-" + semesterName);
		if (request != null && request.getStatus() == enumStatus.DONE) {
			return request;
		} else {
			Requests requests = requestService.addRequest("generateSchedule-" + semesterName);
			scheduleService.autoGenerateSchedule(semesterName, requests);
			return requests;
		}
	}

	@PostMapping("/weeklyTimetable")
	public List<TimeTableSlotDto> weeklyTimetable(@RequestParam("startWeek") LocalDate startWeek,
	                                              @RequestParam("endWeek") LocalDate endWeek,
	                                              @RequestParam("roles") List<enumRole> roles,
	                                              @RequestParam("ms") String ms
	) {
		return scheduleService.getScheduleOfWeek(startWeek, endWeek, roles, ms);
	}
}
