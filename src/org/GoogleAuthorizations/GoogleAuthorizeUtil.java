package org.GoogleAuthorizations;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.Servlets.ConnectionOnButtonClick;

public class GoogleAuthorizeUtil {
	
	public static ServletContext context;
    public void setServletContext(ServletContext cont) 
    {
    	context=cont; 
    }

    public static Credential authorize(HttpServletRequest request) throws IOException, GeneralSecurityException {
    	System.out.println("Authorize called!!");
    	ServletContext context = request.getSession().getServletContext();
    	
//    	String appPath = context.getRealPath("/resources/credentials.json");   	
//    	String tokenPath =  context.getRealPath("/resources/tokens");
//    	System.out.println("This is the path : "+appPath+" , "+tokenPath);
    	
    	final String APPLICATION_NAME = "FeedBack Creator";
    	final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    	final String TOKENS_DIRECTORY_PATH = "D:\\Workspace\\FCv2\\tokens";
//    	final String TOKENS_DIRECTORY_PATH = tokenPath;
    	

//    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    	final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS,DriveScopes.DRIVE,DriveScopes.DRIVE_FILE,DriveScopes.DRIVE_METADATA,GmailScopes.GMAIL_COMPOSE,GmailScopes.GMAIL_SEND);
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    	// build GoogleClientSecrets from JSON file
        final String CREDENTIALS_FILE_PATH = "/credentials.json";
//        final String CREDENTIALS_FILE_PATH = appPath;

//    	 Load client secrets.
    	 InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//    	 InputStream in =  context.getResourceAsStream(CREDENTIALS_FILE_PATH);
         if (in == null) {
             throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
         }
//         GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(
//        	        new File(appPath))));
         GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

         // Build flow and trigger user authorization request.
         GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                 HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                 .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                 .setAccessType("offline")
                 .build();
         LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
         
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        // build Credential object
        Credential credential =  new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        
        return credential;
    }
}