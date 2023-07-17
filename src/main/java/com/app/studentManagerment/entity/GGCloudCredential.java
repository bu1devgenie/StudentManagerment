package com.app.studentManagerment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ggcloud_credential")
public class GGCloudCredential {
	@Id
	private Long id;
	@Column(name = "name")
	private String name;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "authorization_code")
	private String authorizationCode;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "redirect_uris")
	private String redirectUris;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "update_date")
	private LocalDateTime updateDate;

	public GGCloudCredential() {
	}

	public GGCloudCredential(Long id, String name, String accessToken, String authorizationCode, String clientId, String clientSecret, LocalDateTime createDate, String redirectUris, String refreshToken, LocalDateTime updateDate) {
		this.id = id;
		this.name = name;
		this.accessToken = accessToken;
		this.authorizationCode = authorizationCode;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.createDate = createDate;
		this.redirectUris = redirectUris;
		this.refreshToken = refreshToken;
		this.updateDate = updateDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "GGCloudCredential{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", accessToken='" + accessToken + '\'' +
		       ", authorizationCode='" + authorizationCode + '\'' +
		       ", clientId='" + clientId + '\'' +
		       ", clientSecret='" + clientSecret + '\'' +
		       ", createDate=" + createDate +
		       ", redirectUris='" + redirectUris + '\'' +
		       ", refreshToken='" + refreshToken + '\'' +
		       ", updateDate=" + updateDate +
		       '}';
	}
}
