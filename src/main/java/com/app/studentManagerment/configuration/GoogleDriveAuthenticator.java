package com.app.studentManagerment.configuration;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GoogleDriveAuthenticator {

    // Client ID and secret of your application, obtained from the Google Cloud Console
    private static final String CLIENT_ID = "204905918122-2v7dflbsdq1c7m6rqjs9k9uo5724i00c.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";

    // Redirect URI of your application, registered in the Google Cloud Console
    private static final String REDIRECT_URI = "http://localhost:8081/callback";

    // Scopes of the access token, determines the permissions granted to your application
    private static final String[] SCOPES = {"https://www.googleapis.com/auth/drive"};

    public static void main(String[] args) throws IOException {

        // Initialize the Google authorization flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                new JacksonFactory(),
                CLIENT_ID,
                CLIENT_SECRET,
                java.util.Arrays.asList(SCOPES))
                .setAccessType("offline")
                .build();

        // Generate the Google authorization URL
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI);
        String authorizationUrl = url.build();

        // Print the authorization URL and prompt the user to visit it
        System.out.println("Open the following URL in a browser:\n" + authorizationUrl);
        System.out.println("Enter the authorization code:");

        // Read the authorization code from the standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String authorizationCode = br.readLine();

        // Exchange the authorization code for an access token
        GoogleTokenResponse response = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .execute();

        // Extract the access token from the response
        String accessToken = response.getAccessToken();

        // Use the access token to make authorized requests to the Google Drive API
        Credential credential = new GoogleCredential().setAccessToken(accessToken);
        // TODO: Use the credential to make API requests

        System.out.println("Authentication successful.");
    }
}
