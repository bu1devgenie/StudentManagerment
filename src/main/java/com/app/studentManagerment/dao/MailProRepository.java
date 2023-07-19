package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.MailPro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface MailProRepository extends JpaRepository<MailPro, Long> {
    @Query(value = """
                    select m.id, m.host, m.password, m.port, m.sender_name, m.smtp_auth, m.smtp_starttls_enable, m.username, m.role_id, m.working from mail_pro m
                                        join role r on r.id = m.role_id
                                        where r.name like %:role% and m.working='ACTIVITY'
                    limit 1
            """, nativeQuery = true)
    MailPro getMailProWithRole(@Param("role") String role);
}
