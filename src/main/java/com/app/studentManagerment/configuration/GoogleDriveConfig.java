package com.app.studentManagerment.configuration;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleDriveConfig {
    private static final String APPLICATION_NAME = "SchoolManager";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    // path file Google Drive Service
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_204905918122-2v7dflbsdq1c7m6rqjs9k9uo5724i00c.apps.googleusercontent.com.json";

    public Drive getInstance() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        if (credential.getExpiresInSeconds() == null || credential.getExpiresInSeconds() <= 60) { // Kiểm tra xem Access Token đã hết hạn chưa, với buffer time là 60 giây
            credential.setAccessToken("ya29.a0AWY7CkmK5eDgyUSK3s91_GqjaCMwsjXFu0JIWdJCNRHHthweo-TTuRzUppgGjBWsZ_HtPb63ldwT0iYUSVtSs_rwu6toPmsUwQxzqfzIsufbHd0epUFvV80tzsC51qoGELQ0DMX5pXyEyyVTN9Z6KI-aymuXVEtgaCgYKAT8SARMSFQG1tDrpSDYV0Qr55VDzbpVzSVcZxg0167");
        }
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
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

