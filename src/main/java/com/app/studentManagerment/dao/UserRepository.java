package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT us
            FROM User us LEFT JOIN Account a ON a.id = us.account.id
                        WHERE (((:type='' or :type like 'name') and (:searchTerm IS NULL OR us.name LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'dob') and (:searchTerm IS NULL OR us.dob LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'address') and (:searchTerm IS NULL OR us.address LIKE CONCAT('%', :searchTerm, '%')))
                        OR ((:type='' or :type like 'email') and (:searchTerm IS NULL OR a.email LIKE CONCAT('%', :searchTerm, '%'))))
                        ORDER BY
                          CASE
                            WHEN :type = 'name' THEN us.name
                            WHEN :type = 'dob' THEN us.dob
                            WHEN :type = 'address' THEN us.address
                            WHEN :type = 'email' THEN a.email END ASC
            """)
    Page<User> search(@Param("searchTerm") String searchText, @Param("type") String type, Pageable pageable);
}
