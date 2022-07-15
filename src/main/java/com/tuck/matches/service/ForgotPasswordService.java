package com.tuck.matches.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.ResetPasswordBean;

public class ForgotPasswordService {
	
	Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);

	public boolean checkUserExists(String username) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, username )) {
					return true;
				}
			}
		}
		return false;
		
	}
	private boolean match(String[] strings, String username) {
		for (String string : strings) {
			if(string.equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}
	public void generateOtp(String username) throws IOException, CsvException {
		if(this.checkUserExists(username)) {
			SendMailService mail = new SendMailService();
			mail.sendMail(username);
		}else {
			throw new RuntimeException("Username is invalid kindly try with a valid username!");
		}
		
	}
	
	public void resetCall(ResetPasswordBean reset) throws FileNotFoundException, IOException, CsvException {
		if(this.checkUserExists(reset.getUserName())) {
			if(this.checkOtpValid(reset)) {
				this.setNewPassword(reset);
			}else {
				throw new RuntimeException("OTP is not valid kindly try again!");
			}
		}else {
			throw new RuntimeException("Username is invalid kindly try with a valid username!");
		}
		
	}
	private void setNewPassword(ResetPasswordBean reset) throws IOException, CsvException {
		boolean isChange = false;
		List<String[]> r = null;
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, reset.getUserName())) {
					strings[1]=reset.getPassword();
					isChange=true;
					break;
				}
			}
		}
		if(isChange) {
			String fileName = "./src/main/resources/credentials.csv";
			File file = new File(fileName);
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			writer.writeAll(r);
			writer.close();
		}
		
	}
	private boolean checkOtpValid(ResetPasswordBean reset) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/OTP.csv"))) {
			List<String[]> r = reader.readAll();
			for(int i = r.size()-1;i>=0;i--) {
				String[] arr = r.get(i);
				if(arr[0].equalsIgnoreCase(reset.getUserName())) {
					if(arr[1].equals(reset.getOtp())) {
						return true;
					}
					return false;
					
				}
			}
		}
		return false;
	}
	
	
	
	
}
