package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.dao.RoleRepository;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;
import com.app.studentManagerment.services.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Value("${default-password}")
	private String DEFAULT_PASS;

	public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
	}


	@Override
	public List<String> searchEmailNoConnected(String email) {
		return accountRepository.searchEmailNoConnected(email);

	}

	@Override

	public Account createAccount(String email, List<String> roles) {
		if (accountRepository.findByEmail(email) == null) {
			Account account = new Account();
			account.setEmail(email);
			account.setPassword(passwordEncoder.encode(DEFAULT_PASS));
			account = uRole(roles, account);
			return accountRepository.save(account);
		}
		return null;
	}


	@Override
	public Account updateAccount(String email, String password, List<String> roles) {
		Account account = accountRepository.findByEmail(email);
		if (account != null) {
			if (password != null) {
				account.setPassword(passwordEncoder.encode(password));
			}
			if (! roles.isEmpty()) {
				account = uRole(roles, account);
			}
			return accountRepository.save(account);
		}
		return null;
	}

	@Override
	public Boolean resetPassword(String oldEmail) {
		Account account = accountRepository.findByEmail(oldEmail);
		if (account != null) {
			account.setPassword(passwordEncoder.encode(DEFAULT_PASS));
			accountRepository.save(account);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAccount(String email) {
		Account account = accountRepository.findByEmail(email);
		if (account != null) {
			accountRepository.delete(account);
			return true;
		}
		return false;
	}

	@Override
	public Account autoCreateAccount(String name, String ms, enumRole role) {
		StringBuilder email = new StringBuilder();
		name = name.replace(" ", "");
		email.append(name);
		String[] strings = ms.split("-");
		switch (role) {
			case Hr -> email.append("Hr");
			case Admin -> email.append("Admin");
			case Teacher -> email.append("Teacher");
			case Student -> email.append("Student");
			case Principal -> email.append("Principal");
		}
		email.append(strings[2]).append("@gmail.com");
		Account account = accountRepository.findByEmail(email.toString());
		if (account != null) {
			return null;
		}
		account.setEmail(email.toString());
		account.setPassword(passwordEncoder.encode(DEFAULT_PASS));
		accountRepository.save(account);
		return account;
	}

	@Override
	public List<Role> getRoles(String email) {
		return accountRepository.allRoleByEmail(email);
	}

	@Override
	public Page<Account> searchAccount(String email, Pageable pageable) {
		return accountRepository.searchWithEmail(email, pageable);
	}

	@Override
	public List<Role> allRoleForAccount() {
		List<enumRole> diffRoles = new ArrayList<>();
		diffRoles.add(enumRole.mail_PQLDT);
		diffRoles.add(enumRole.mail_PQLSV);
		diffRoles.add(enumRole.mail_DVMAIL);
		return roleRepository.getAllForAccount(diffRoles);
	}

	private Account uRole(List<String> roles, Account account) {
		List<Role> lRole = new ArrayList<>();
		for (String r : roles) {
			Role ro = roleRepository.findByName(enumRole.getEnumFromString(r));
			if (ro != null) {
				lRole.add(ro);
			}
		}
		account.setRole(lRole);
		return account;
	}
}
