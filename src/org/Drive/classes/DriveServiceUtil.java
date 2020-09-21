package org.Drive.classes;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;

import org.GoogleAuthorizations.GoogleAuthorizeUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

public class DriveServiceUtil {

	 private static final String APPLICATION_NAME = "FeedBack Creator";
	 
	    public static Drive getDriveService(HttpServletRequest request) throws IOException, GeneralSecurityException {
	        Credential credential = GoogleAuthorizeUtil.authorize(request);
	        return new Drive.Builder(
	          GoogleNetHttpTransport.newTrustedTransport(), 
	          JacksonFactory.getDefaultInstance(), credential)
	          .setApplicationName(APPLICATION_NAME)
	          .build();
	    }
	    
	    public static Drive setup(HttpServletRequest request) throws GeneralSecurityException, IOException {
	    	System.out.println("Setup Called!!!");
	    	Drive driveService;
	    	driveService = getDriveService(request);
	        return driveService;
	    }
	    
}
