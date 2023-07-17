package com.app.studentManagerment.restController;

import com.app.studentManagerment.configuration.CustomUserDetails;
import com.app.studentManagerment.configuration.jwt.JwtTokenUtil;
import com.app.studentManagerment.dto.AccountRequestDto;
import com.app.studentManagerment.dto.AccountResponseDto;
import com.app.studentManagerment.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthRestController {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;

	public AuthRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Validated AccountRequestDto accountRequestDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountRequestDto.getEmail(), accountRequestDto.getPassword())
		);
		CustomUserDetails account = (CustomUserDetails) authentication.getPrincipal();
		String accessToken = jwtTokenUtil.generateAccessToken(account);
		Account myAccount = account.getAccount();

		AccountResponseDto accountResponseDto = new AccountResponseDto();
		accountResponseDto.setEmail(account.getUsername());

		return ResponseEntity.ok(accountResponseDto);

	}
}
