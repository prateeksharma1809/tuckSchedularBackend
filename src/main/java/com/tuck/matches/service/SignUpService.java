package com.tuck.matches.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.repository.CredentialsRepository;

public class SignUpService {
	Logger logger = LoggerFactory.getLogger(SignUpService.class);
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	

	@Autowired
	private SendMailService sendMailService;
	
	public void signUpDB(UserDetails user) {
		this.validate(user);
		this.checkUserExistsInDB(user.getUserName());
		this.createUserInDB(user);
		this.sendMail(user);
	}

	private void sendMail(UserDetails user) {
		String body = "You have been registered with consulting club casing tool!\n \nIf you find this mail in SPAM folder please mark it as NOT SPAM to receive any further communications!";
		sendMailService.sendMail(user.getUserName(), "Let’s start casing!", body);
		
	}

	private void checkUserExistsInDB(String userName) {
		Optional<Credentials> record = credentialsRepository.findById(userName);
		if(record.isPresent()) {
			throw new RuntimeException("Username already exists, try logging in!");
		}
		
		
	}

	private void createUserInDB(UserDetails user) {
		credentialsRepository.save(createCredentialObject(user));
		
	}

	private Credentials createCredentialObject(UserDetails user) {
		Credentials credentials = new Credentials();
		credentials.setUserName(user.getUserName());
		credentials.setPassword(EncodePasswordService.encode(user.getPassword()));
		credentials.setName(user.getName());
		credentials.setIsMentor(user.getIsMentor());
		if(user.getIsMentor()) {
			credentials.setInterFirm(user.getInterFirm());
			credentials.setFullTmOffer(user.getFullTmOffer());
			credentials.setCaseName(user.getCaseName());
			credentials.setOfficeLocation(user.getOfficeLoc());
		}
		return credentials;
	}
	

	private void validate(UserDetails user) {
		if (null == user.getUserName() || user.getUserName().isEmpty()) {
			throw new RuntimeException("Username should not be empty!");
		} else if (null == user.getPassword() || user.getPassword().isEmpty()) {
			throw new RuntimeException("Password should not be empty!");
		} else if (null == user.getName() || user.getName().isEmpty()) {
			throw new RuntimeException("Name should not be empty!");
		} else if (user.getIsMentor() && (null == user.getInterFirm() || user.getInterFirm().isEmpty())) {
			throw new RuntimeException("Internship Firm name should not be empty!");
		}else {
			validateEmail(user.getUserName());
		}
	}

	private void validateEmail(String userName) {
		String regex = "^(.+)@(.+)$";  
		 Pattern pattern = Pattern.compile(regex);  
		 Matcher matcher = pattern.matcher(userName);  
		 if(!matcher.matches()) {
			 throw new RuntimeException("Email not valid!");
		 }	
	}

}
