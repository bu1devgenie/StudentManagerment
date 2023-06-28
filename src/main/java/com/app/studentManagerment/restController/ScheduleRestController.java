package com.app.studentManagerment.restController;

import com.app.studentManagerment.services.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleRestController {
    private ScheduleService scheduleService;

    public ScheduleRestController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/autoGenerateSchedule")
    public String autoGenerateSchedule(@RequestParam("SemesterName") String semesterName) {

        return scheduleService.autoGenerateSchedule(semesterName);
    }
}
