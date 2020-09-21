package org.Sheets.classes;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;

import org.GoogleAuthorizations.GoogleAuthorizeUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.sheets.v4.Sheets;

public class SheetsServiceUtil {

	 private static final String APPLICATION_NAME = "FeedBack Creator";
	 
	    public static Sheets getSheetsService(HttpServletRequest request) throws IOException, GeneralSecurityException {
	    	System.out.println("Get Sheets Service called!!!");

	        Credential credential = GoogleAuthorizeUtil.authorize(request);
	        return new Sheets.Builder(
	          GoogleNetHttpTransport.newTrustedTransport(), 
	          JacksonFactory.getDefaultInstance(), credential)
	          .setApplicationName(APPLICATION_NAME)
	          .build();
	    }
	    
	    public static Sheets setup(HttpServletRequest request) throws GeneralSecurityException, IOException {
	    	System.out.println("Setup Called!!!");
	    	Sheets sheetsService;
	    	sheetsService = getSheetsService(request);
	        return sheetsService;
	    }
}
