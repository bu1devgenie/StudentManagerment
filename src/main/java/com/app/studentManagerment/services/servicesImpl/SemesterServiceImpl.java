package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.SemesterRepository;
import com.app.studentManagerment.entity.Semester;
import com.app.studentManagerment.services.SemesterService;
import org.springframework.stereotype.Service;

@Service
public class SemesterServiceImpl implements SemesterService {
	private SemesterRepository semesterRepository;

	public SemesterServiceImpl(SemesterRepository semesterRepository) {
		this.semesterRepository = semesterRepository;
	}

	@Override
	public Semester getSemesterByName(String semesterName) {
		return semesterRepository.findByName(semesterName);
	}
}
