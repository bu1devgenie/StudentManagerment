package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("""
			         select a.email from Account a where a.email not in (select account.email from Student 
			         where account.email is not null )
			         and    a.email not in (select account.email from Teacher where account.email is not null )
			         and    a.email not in (select account.email from User where account.email is not null )
			         and :email IS NULL OR a.email LIKE CONCAT('%', :email, '%')
			""")
	List<String> searchEmailNoConnected(@Param("email") String email);

	@Query("""
			select a from Account a where a.email=:email
			""")
	Account findByEmail(@Param("email") String email);

	@Query("""
			SELECT CASE WHEN EXISTS (
			    SELECT 1
			    FROM Account a
			    JOIN Teacher tea ON tea.account.email = a.email
			    JOIN User u ON u.account.email = a.email
			    JOIN Student stu ON stu.account.email = a.email
			    WHERE a.email LIKE :email
			)
			THEN TRUE
			ELSE FALSE
			END AS account_exists
			""")
	Boolean emailIsConnected(String email);

	@Query("""
			SELECT a.role from Account a where a.email=:email
			""")
	List<Role> allRoleByEmail(String email);

	@Query("""
			SELECT a from Account a where (:email like '' or 
											:email is null or
											 a.email like concat('%',:email,'%') )
			""")
	Page<Account> searchWithEmail(@Param("email") String email, Pageable pageable);
}
