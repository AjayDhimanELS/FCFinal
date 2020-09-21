package org.Gmail.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class MailCreation {
	 public static MimeMessage createEmail(Gmail gservice, String to, String subject, String bodyText) throws Exception {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			String me = "me";
			MimeMessage email = new MimeMessage(session);
			
			email.setFrom(new InternetAddress(me));
			email.addRecipient(javax.mail.Message.RecipientType.TO,
			new InternetAddress(to));
			email.setSubject(subject);
			
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(bodyText, "text/html");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			
			email.setContent(multipart);
			
		    sendMessage(gservice,to,email);
			
//			send(gservice,to,email,attachments);
			
			return email;
			}
	    //---------------------------------------------------------------------------------------------------------------
	    	
		
		public static Message createMessage(MimeMessage emailContent) throws MessagingException, IOException {
	    	
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        emailContent.writeTo(buffer);
	        byte[] bytes = buffer.toByteArray();
	        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
	        Message message = new Message();
	    	message.setRaw(encodedEmail);
	    	
	        return message;
	    }
		
	    
	    
//	    static void send(Gmail service, String userId, MimeMessage emailContent,Set<File> attachments) throws Exception {
//
//	        Message message = new Message();
//	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//	        emailContent.writeTo(buffer);
//
//	        // See if we need to use multipart upload
//	        if (attachments!=null) { //&& computeTotalSizeOfAttachments(attachments) > 5000000) {
//	        	System.out.println("+++++++++++++++++++++++++++++++    Calleds      ++++++++++++++++++++++++++++++++=");
//	            ByteArrayContent content = new ByteArrayContent("message/rfc822", buffer.toByteArray());
//	            
//	            message = service.users().messages().send(userId, null, content).execute();
//
//	        // Otherwise, use "simple" send
//	        } else {
//
//	        	message = createMessage(emailContent);
//	    		message = service.users().messages().send(userId, message).execute();
//	    		
//	    		System.out.println("Message id: " + message.getId());
//	    		System.out.println(message.toPrettyString());
//	        }
//
//	        System.out.println("Gmail Message: " + message.toPrettyString());
//	    }
	    
		public static Message sendMessage(Gmail service,String userId,MimeMessage emailContent)throws MessagingException, IOException {
			Message message = createMessage(emailContent);
			message = service.users().messages().send(userId, message).execute();
			
//			System.out.println("Message id: " + message.getId());
//			System.out.println(message.toPrettyString());
			return message;
			}
}
