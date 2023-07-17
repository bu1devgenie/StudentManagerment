package com.app.studentManagerment.configuration.jwt;

import com.app.studentManagerment.dao.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	private final AccountRepository accountRepository;
	private final JwtTokenUtil jwtTokenUtil;

	public JwtTokenFilter(AccountRepository accountRepository, JwtTokenUtil jwtTokenUtil) {
		this.accountRepository = accountRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	}
}
