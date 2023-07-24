package com.app.studentManagerment.services;

import com.app.studentManagerment.dto.AccountResponseDto;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;

import java.util.List;


public interface AccountService {
	List<String> searchEmailNoConnected(String email);

	Account createAccount(String email, String password, List<enumRole> roles);

	Account updateAccount(String oldEmail, String email, String password, List<enumRole> roles);

	boolean deleteAccount(String email);

	Account autoCreateAccount(String name, String ms, enumRole role);

	List<Role> getRoles(String email);

}
