package com.app.studentManagerment.restController;

import com.app.studentManagerment.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/findAllEmailNoConnected")
	public List<String> allAccountNoConected() {
		return accountService.findallEmailNoConnected();
	}

	@PostMapping("/creatAccount")
	public List<String> creatAccount(@RequestParam("") String email) {
//		return accountService.createAccount(email, );

		return null;
	}

}
