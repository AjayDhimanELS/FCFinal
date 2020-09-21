package org.Gmail.classes;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;

import org.GoogleAuthorizations.GoogleAuthorizeUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;


public class GmailServiceUtil {

	private static final String APPLICATION_NAME = "FeedBack Creator";
	 	    
    public static Gmail getGMailService(HttpServletRequest request) throws IOException, GeneralSecurityException {
        Credential credential = GoogleAuthorizeUtil.authorize(request);
        return new Gmail.Builder(
          GoogleNetHttpTransport.newTrustedTransport(), 
          JacksonFactory.getDefaultInstance(), credential)
          .setApplicationName(APPLICATION_NAME)
          .build();
    } 
    public static Gmail setup(HttpServletRequest request) throws GeneralSecurityException, IOException {
    	System.out.println("Setup Called!!!");
    	Gmail gmailService;
    	gmailService = getGMailService(request);
        return gmailService;
    }
}

