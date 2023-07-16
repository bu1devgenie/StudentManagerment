package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.studentManagerment.enumPack.enumRole;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
                     select a.email from Account a where a.id not in (select account.id from Student where account.id is not null )
                     and    a.id not in (select account.id from Teacher where account.id is not null )
                     and    a.id not in (select account.id from User where account.id is not null )
            """)
    List<String> findallEmailNoConnected();

    @Query("""
            select a from Account a where a.email=:email
            """)
    Account findByEmail(@Param("email") String email);

    @Query("""
            SELECT CASE WHEN EXISTS (
                SELECT 1
                FROM Account a
                JOIN Teacher tea ON tea.account.id = a.id
                JOIN User u ON u.account.id = a.id
                JOIN Student stu ON stu.account.id = a.id
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
}
