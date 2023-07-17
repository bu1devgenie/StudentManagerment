package com.app.studentManagerment.configuration;

import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
	private final AccountRepository accountRepository;

	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(email);
		if (account == null) {
			throw new UsernameNotFoundException("Cannot find any user account with email: " + email);
		} else {
			return new CustomUserDetails(account);
		}
	}
}
