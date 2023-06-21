package com.app.studentManagerment.restController;

import com.app.studentManagerment.entity.Schedule;
import com.app.studentManagerment.services.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleRestController {
    private ScheduleService scheduleService;

    public ScheduleRestController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/generateSchedule")
    public String generateSchedule() {
        scheduleService.autoGenerateSchedule("Summer-2023");
        return null;
    }

    @GetMapping("/allSchedule")
    public Page<Schedule> allSchedule() {
        return scheduleService.allSchedule();
    }

}
