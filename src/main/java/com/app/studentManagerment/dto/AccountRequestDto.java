package com.app.studentManagerment.dto;

public class AccountRequestDto {
	private String email;
	private String password;

	public AccountRequestDto() {
	}

	public AccountRequestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "AccountRequestDto{" +
		       "email='" + email + '\'' +
		       ", password='" + password + '\'' +
		       '}';
	}
}
