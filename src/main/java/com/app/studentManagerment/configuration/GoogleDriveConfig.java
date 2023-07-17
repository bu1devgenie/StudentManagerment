package com.app.studentManagerment.configuration;


import com.app.studentManagerment.dao.GGcloud_credentialRepository;
import com.app.studentManagerment.entity.GGCloudCredential;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleDriveConfig {
	private static final String APPLICATION_NAME = "SchoolManager";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	// path file Google Drive Service
	private static final String CREDENTIALS_FILE_PATH = "/client_secret_506984316057-2bjacgkh3trub0eu7o9ciflen30hpca5.apps.googleusercontent.com.json";
	private static final List SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String REDIRECT_URI = "http://localhost:9999/callback";


	private GGcloud_credentialRepository gGcloudCredentialRepository;

	public GoogleDriveConfig(GGcloud_credentialRepository gGcloudCredentialRepository) {
		this.gGcloudCredentialRepository = gGcloudCredentialRepository;
	}


	public Drive getInstance() throws GeneralSecurityException, IOException, ServletException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Credential credential = getCredentials(HTTP_TRANSPORT);
		GGCloudCredential ggCloudCredential = gGcloudCredentialRepository.getByNameOrderByUpdateDate("credential_ggdrive");
		if (ggCloudCredential != null) {
			credential.setRefreshToken(ggCloudCredential.getRefreshToken());
			credential.setAccessToken(ggCloudCredential.getAccessToken());
		} else {
			credential = author();
			ggCloudCredential.setAccessToken(credential.getAccessToken());
			ggCloudCredential.setRefreshToken(credential.getRefreshToken());
			ggCloudCredential.setUpdateDate(LocalDateTime.now());
		}
		if (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60) {
			credential = refreshAccessToken();
		}
		Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
		return service;
	}

	private Credential author() throws IOException, ServletException {
		// Load the client credentials from the JSON file
		InputStream in = GoogleDriveConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		// Initialize the Google authorization flow using the loaded credentials
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(), new JacksonFactory(), clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline")
				.build();
		GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl()
				.setRedirectUri(REDIRECT_URI);
		String authorizationUrl = url.build();

		// open new web to author


		return null;
	}

	private Credential refreshAccessToken() {
		return null;
	}

	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		InputStream in = GoogleDriveConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline")
				.build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
}


