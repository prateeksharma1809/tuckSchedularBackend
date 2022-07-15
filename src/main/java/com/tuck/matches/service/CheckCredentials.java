package com.tuck.matches.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.LoginForm;
import com.tuck.matches.beans.LoginResponse;

public class CheckCredentials {

	Logger logger = LoggerFactory.getLogger(CheckCredentials.class);

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
