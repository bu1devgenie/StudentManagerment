package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Requests;

public interface ScheduleService {
    void autoGenerateSchedule(String semesterName, Requests requests);
}
