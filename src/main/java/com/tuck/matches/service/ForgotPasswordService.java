package com.tuck.matches.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.ResetPasswordBean;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.repository.CredentialsRepository;

public class ForgotPasswordService {
	
	Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	SendMailService sendMailService;

	public boolean checkUserExists(String username)  {
		Optional<Credentials> record = credentialsRepository.findById(username);
		return record.isPresent();
	}
	
	public void generateOtp(String username) throws IOException, CsvException {
		if(this.checkUserExists(username)) {
			sendMailService.sendMail(username);
		}else {
			throw new RuntimeException("Username is invalid kindly try with a valid username!");
		}
		
	}
	
	public void resetCall(ResetPasswordBean reset) throws FileNotFoundException, IOException, CsvException {
		if(this.checkUserExists(reset.getUserName())) {
			if(this.checkOtpValid(reset)) {
				this.setNewPasswordInDB(reset);
			}else {
				throw new RuntimeException("OTP is not valid kindly try again!");
			}
		}else {
			throw new RuntimeException("Username is invalid kindly try with a valid username!");
		}
		
	}
	private void setNewPasswordInDB(ResetPasswordBean reset) {
		Optional<Credentials> record = credentialsRepository.findById(reset.getUserName());
		if(record.isPresent()){
			Credentials cred = record.get();
			cred.setPassword(EncodePasswordService.encode(reset.getPassword()));
			credentialsRepository.save(cred);
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
