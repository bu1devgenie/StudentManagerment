package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(enumRole r);

	@Query("""
			      SELECT r FROM Role r WHERE NOT (r.name in :diffRoles)
			"""
	)
	List<Role> getAllForAccount(@Param("diffRoles") List<enumRole> diffRoles);
}
