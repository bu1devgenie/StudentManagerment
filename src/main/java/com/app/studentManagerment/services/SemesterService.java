package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Semester;

public interface SemesterService {

	Semester getSemesterByName(String semesterName);
}
