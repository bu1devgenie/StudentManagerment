package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Attendence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendenceRepository extends JpaRepository<Attendence, Long> {

}
