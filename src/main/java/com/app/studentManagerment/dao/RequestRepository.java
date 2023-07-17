package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Requests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Requests, Long> {
	Requests findRequestsByDescription(String description);
}
