package com.app.studentManagerment.configuration;

import com.app.studentManagerment.configuration.jwt.JwtTokenFilter;
import com.app.studentManagerment.dao.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
public class SecurityConfig {
	private final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
	private final AccountRepository accountRepository;
	private final JwtTokenFilter jwtTokenFilter;


	public SecurityConfig(AccountRepository accountRepository, JwtTokenFilter jwtTokenFilter) {
		this.accountRepository = accountRepository;
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService(accountRepository);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}


	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/login").hasAnyAuthority()
						.anyRequest().authenticated()
				);

		return null;
	}
}
