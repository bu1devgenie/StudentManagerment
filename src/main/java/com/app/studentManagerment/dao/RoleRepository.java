package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(enumRole r);
}
