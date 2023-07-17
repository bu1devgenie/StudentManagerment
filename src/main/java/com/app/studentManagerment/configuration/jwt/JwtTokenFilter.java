package com.app.studentManagerment.configuration.jwt;

import com.app.studentManagerment.configuration.CustomUserDetails;
import com.app.studentManagerment.dao.AccountRepository;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.enumPack.enumRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		if (! hasAuthorizationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String accessToken = jwtTokenUtil.getAccessToken(request);
		if (! jwtTokenUtil.validateAccessToken(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}
		setAuthenticationContext(accessToken, request);
		filterChain.doFilter(request, response);
	}

	private boolean hasAuthorizationHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (ObjectUtils.isEmpty(header) || ! header.startsWith("Bearer")) {
			return false;
		}
		return true;
	}
	private void setAuthenticationContext(String token, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(token);

		UsernamePasswordAuthenticationToken
				authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	private UserDetails getUserDetails(String token) {
		Account account = new Account();

		Claims claims = jwtTokenUtil.parseClaims(token);
		String claimRoles = (String) claims.get("roles");
		claimRoles = claimRoles.replace("[", "").replace("]", "");
		String[] roleNames = claimRoles.split(", ");
		List<Role> roles = new ArrayList<>();
		for(String roleName : roleNames) {
			Role role = new Role();
			role.setName(enumRole.getEnumFromString(roleName));
			roles.add(role);
		}
		account.setRole(roles);
		String subject = (String) claims.get(Claims.SUBJECT);
		String email = subject;

		account.setEmail(email);

		return new CustomUserDetails(account);
	}
}
