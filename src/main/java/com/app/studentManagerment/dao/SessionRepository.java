package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
