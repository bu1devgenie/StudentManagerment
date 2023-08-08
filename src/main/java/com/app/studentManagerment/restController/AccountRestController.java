package com.app.studentManagerment.restController;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.Role;
import com.app.studentManagerment.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountRestController {
	@Autowired
	private AccountService accountService;

	public AccountRestController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/searchEmailNoConnected")
	public List<String> searchEmailNoConnected(@RequestParam(name = "email") String email) {
		return accountService.searchEmailNoConnected(email);
	}

	@PostMapping("/addAccount")
	public Account addAccount(@RequestParam("email") String email,
	                          @RequestParam(name = "lsRole") List<String> roles) {
		return accountService.createAccount(email, roles);
	}

	@PostMapping("/searchAccount")
	public Page<Account> searchAccount(@RequestParam(value = "email", required = false) String email,
	                                   @RequestParam(name = "targetPageNumber") Integer targetPageNumber) {
		if (targetPageNumber < 0) {
			return null;
		}
		Pageable pageable = PageRequest.of(targetPageNumber, 20);
		return accountService.searchAccount(email, pageable);
	}

	@PutMapping("/updateAccount")
	public Account updateAccount(@RequestParam(value = "email") String email,
	                             @RequestParam(value = "password", required = false) String password,
	                             @RequestParam(value = "lsRole", required = false) List<String> roles) {
		return accountService.updateAccount(email, password, roles);
	}

	@GetMapping("/allRoleForAccount")
	public List<Role> allRoleForAccount() {
		return accountService.allRoleForAccount();
	}

	@PostMapping("/resetPassword")
	public boolean resetPassword(@RequestParam(name = "email") String email) {
		return accountService.resetPassword(email);
	}

	@DeleteMapping("/deleteAccount")
	public Boolean deleteAccount(@RequestParam(name = "email") String email) {
		return accountService.deleteAccount(email);
	}
}
