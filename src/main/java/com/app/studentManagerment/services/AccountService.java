package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.AccountResponseDto;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountService {
	List<String> searchEmailNoConnected(String email);

	Account createAccount(String email, List<String> roles);

	Account updateAccount(String email, String password, List<String> roles);

	Boolean resetPassword(String oldEmail);

	boolean deleteAccount(String email);

	Account autoCreateAccount(String name, String ms, enumRole role);

	List<Role> getRoles(String email);

	Page<Account> searchAccount(String email, Pageable pageable);

	List<Role> allRoleForAccount();
}
