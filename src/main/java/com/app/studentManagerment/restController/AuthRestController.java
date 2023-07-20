package com.app.studentManagerment.restController;

import com.app.studentManagerment.configuration.CustomUserDetails;
import com.app.studentManagerment.configuration.jwt.JwtTokenUtil;
import com.app.studentManagerment.dto.AccountRequestDto;
import com.app.studentManagerment.dto.AccountResponseDto;
import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.services.outCycle.AccountCycle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AuthRestController {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final AccountCycle accountCycle;

	public AuthRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, AccountCycle accountCycle) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.accountCycle = accountCycle;
	}


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Validated AccountRequestDto accountRequestDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountRequestDto.getEmail(), accountRequestDto.getPassword())
		);
		CustomUserDetails account = (CustomUserDetails) authentication.getPrincipal();

		Account myAccount = account.getAccount();
		AccountResponseDto accountResponseDto = accountCycle.ACCOUNT_RESPONSE_DTO(myAccount.getEmail());
		if (accountResponseDto == null) {
			return null;
		}
		accountResponseDto.setAccessToken(jwtTokenUtil.generateAccessToken(account));
		return ResponseEntity.ok(accountResponseDto);
	}

	@PostMapping("/checkAccessToken")
	public ResponseEntity<?> checkAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String accessToken = header.split(" ")[1];
		if (jwtTokenUtil.validateAccessToken(accessToken)) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(false);
	}
}
