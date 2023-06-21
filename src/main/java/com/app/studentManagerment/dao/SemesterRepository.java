package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findByName(String name);
}
