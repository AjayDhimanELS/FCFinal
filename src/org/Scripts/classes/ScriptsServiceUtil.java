package org.Scripts.classes;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;

import org.GoogleAuthorizations.GoogleAuthorizeUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;



public class ScriptsServiceUtil {
	
	private static final String APPLICATION_NAME = "FeedBack Creator";
	 	    
    public static Script getScriptsService(HttpServletRequest request) throws IOException, GeneralSecurityException {
        Credential credential = GoogleAuthorizeUtil.authorize(request);
        return new Script.Builder(
          GoogleNetHttpTransport.newTrustedTransport(), 
          JacksonFactory.getDefaultInstance(), credential)
          .setApplicationName(APPLICATION_NAME)
          .build();
    } 
    public static Script setup(HttpServletRequest request) throws GeneralSecurityException, IOException {
    	System.out.println("Setup Called!!!");
    	Script sriptService;
    	sriptService = getScriptsService(request);
        return sriptService;
    }
}

