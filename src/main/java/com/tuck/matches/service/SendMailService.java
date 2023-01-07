package com.tuck.matches.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.tuck.matches.beans.Constants;

public class SendMailService {
	Logger logger = LoggerFactory.getLogger(SendMailService.class);
	
	Session session;
	String from = Constants.EMAIL_ID;
	String password = Constants.PASSWORD;
	String host = "smtp.gmail.com";
	MimeMessage message;
	
	public SendMailService(){
	        // Get system properties
	        Properties properties = System.getProperties();

	        // Setup mail server
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", "465");
	        properties.put("mail.smtp.ssl.enable", "true");
	        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        this.session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);
            }
        });
        message = new MimeMessage(session);
	}
	
	public void sendMail(String to, String subject, String body) {
		
		// Assuming you are sending email from through gmails smtp
       


        // Used to debug SMTP issues
//        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            logger.info("sending...");
            // Send message
            Transport.send(message);
            
            logger.info("Sent message successfully....");
            message.setRecipient(Message.RecipientType.TO, null);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
	}

	
	private String getRandomNumberString() {
	    // It will generate 6 digit random Number.
	    // from 0 to 999999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    // this will convert any number sequence into 6 character.
	    return String.format("%06d", number);
	}
	
//	public void sendMail() {
	public void sendMail(String to) throws IOException {
//		String from = Constants.EMAIL_ID;
//		String password = Constants.PASSWORD;
		logger.info("from :{}, password :{}", from, password);
		
        String otp = this.getRandomNumberString();
        logger.info(otp);

        // Assuming you are sending email from through gmails smtp
//        String host = "smtp.gmail.com";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
//        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//
//            protected PasswordAuthentication getPasswordAuthentication() {
//
//                return new PasswordAuthentication(from, password);
//
//            }
//
//        });
        // Used to debug SMTP issues
//        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Reset password / Sign up");

            // Now set the actual message
            message.setText("OTP : "+otp);
            createEntry(to,otp);

            logger.info("sending...");
            // Send message
            Transport.send(message);
            
            logger.info("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

	}
	private void createEntry(String username, String otp) throws IOException {
		String fileName = "./src/main/resources/OTP.csv";
		File file = new File(fileName);
		FileWriter outputfile = new FileWriter(file, true);
		CSVWriter writer = new CSVWriter(outputfile);
		String[] newArray = new String[2];
		newArray[0]=username;
		newArray[1]=otp;
		writer.writeNext(newArray);
		writer.close();
	}

}
