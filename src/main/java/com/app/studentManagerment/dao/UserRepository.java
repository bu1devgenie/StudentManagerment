package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("""
			select us
			FROM User us
			LEFT JOIN Account a ON a.email = us.account.email
			WHERE ( :ms like '' or :ms IS NULL OR us.ms LIKE CONCAT('%', :ms, '%'))
			  AND ( :name like '' or :name IS NULL OR us.name LIKE CONCAT('%', :name, '%'))
			  AND ( :dob like '' or :dob IS NULL OR us.dob = :dob)
			  AND ( :email like '' or :email IS NULL OR a.email LIKE CONCAT('%', :email, '%'))
			""")
	Page<User> search(@Param("ms") String ms,
	                  @Param("name") String name,
	                  @Param("dob") LocalDate dob,
	                  @Param("email") String email,
	                  Pageable pageable);

	User findUserByAccount_Email(String email);

	User findUserByMs(String ms);
}
