package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.GGCloudCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GGcloud_credentialRepository extends JpaRepository<GGCloudCredential, Long> {

	GGCloudCredential getByNameOrderByUpdateDate(String credentialGgdrive);
}
