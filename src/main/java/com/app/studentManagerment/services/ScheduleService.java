package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Schedule;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    String autoGenerateSchedule(String semesterName);

    Page<Schedule> allSchedule();
}
