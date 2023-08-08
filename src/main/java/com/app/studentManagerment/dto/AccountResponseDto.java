package com.app.studentManagerment.dto;

import com.app.studentManagerment.enumPack.enumRole;

import java.util.Arrays;
import java.util.List;

public class AccountResponseDto {
	private String email;
	private String ms;
	private List<enumRole> roles;
	private String name;
	private String avatar;

	private String accessToken;

	public AccountResponseDto() {
	}

	public AccountResponseDto(String email, String ms, List<enumRole> roles, String name, String avatar, String accessToken) {
		this.email = email;
		this.ms = ms;
		this.roles = roles;
		this.name = name;
		this.avatar = avatar;
		this.accessToken = accessToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public List<enumRole> getRoles() {
		return roles;
	}

	public void setRoles(List<enumRole> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "AccountResponseDto{" +
		       "email='" + email + '\'' +
		       ", ms='" + ms + '\'' +
		       ", roles=" + roles +
		       ", name='" + name + '\'' +
		       ", avatar='" + avatar + '\'' +
		       ", accessToken='" + accessToken + '\'' +
		       '}';
	}
}
