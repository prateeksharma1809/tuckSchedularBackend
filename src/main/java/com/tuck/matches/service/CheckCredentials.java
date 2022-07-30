package com.tuck.matches.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.LoginForm;
import com.tuck.matches.beans.LoginResponse;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.repository.CredentialsRepository;

public class CheckCredentials {

	Logger logger = LoggerFactory.getLogger(CheckCredentials.class);
	
	@Autowired
	private LoginResponse loginResponse;
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	public LoginResponse checkInDB(LoginForm loginForm) {
		validate(loginForm);
		if(checkIfAdmin(loginForm)) {
			loginResponse.setIsAdmin(true);
			loginResponse.setOpenPage(false);
			return loginResponse;
		}
		Optional<Credentials> record = credentialsRepository.findById(loginForm.getUserName());
		if(record.isPresent()) {
			Credentials cred = record.get();
//			logger.info("Should be removed : {}", cred);
			if(cred.getPassword().equals(loginForm.getPassword())) {
				loginResponse.setIsAdmin(false);
				loginResponse.setOpenPage(true);
			}
		}
	
		return loginResponse;
	}

	private void validate(LoginForm loginForm) {
		if(null ==loginForm || null == loginForm.getUserName() || loginForm.getUserName().isEmpty()) {
			throw new RuntimeException("Username cannot be blank!");
		}else if(null==loginForm.getPassword() || loginForm.getPassword().isEmpty()) {
			throw new RuntimeException("Password cannot be blank!");
		}
	}

	public LoginResponse check(LoginForm loginForm) throws IOException, CsvException {
		
		LoginResponse response = new LoginResponse();
		if(checkIfAdmin(loginForm)) {
			response.setIsAdmin(true);
			response.setOpenPage(false);
			return response;
		}
		
		
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, loginForm)) {
					response.setIsAdmin(false);
					response.setOpenPage(true);
				}
			}
		}
		return response;
	}
	public boolean check(LoginForm loginForm, Boolean res) throws IOException, CsvException {
		

		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, loginForm)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private boolean checkIfAdmin(LoginForm loginForm) {
		if("Admin".equals(loginForm.getUserName()) && "Admin11!".equals(loginForm.getPassword())) {
			return true;
		}
		return false;
		
	}

	private boolean match(String[] cred, LoginForm loginForm) {
		logger.info(Arrays.toString(cred));
		logger.info(loginForm.toString());
		logger.info(Arrays.toString(cred));
		String password = "";
		if(cred[1]!=null) {
			password=DecodePasswordService.decode(cred[1]);
		}
		logger.info("decoded password:{}", password);
		if(cred.length>1 && cred[0].equalsIgnoreCase(loginForm.getUserName()) && password.equals(loginForm.getPassword())) {
			return true;
		}
		return false;

	}
}
