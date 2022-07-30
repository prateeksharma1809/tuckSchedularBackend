package com.tuck.matches.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.repository.CredentialsRepository;

public class SignUpService {
	Logger logger = LoggerFactory.getLogger(SignUpService.class);
	
	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	private Credentials credentials;
	@Deprecated
	public void signUp(UserDetails user) throws FileNotFoundException, IOException, CsvException {
		this.validate(user);
		this.checkUserExists(user.getUserName());
		this.createUser(user);
	}
	
	public void signUpDB(UserDetails user) {
		this.validate(user);
		this.checkUserExistsInDB(user.getUserName());
		this.createUserInDB(user);
	}

	private void checkUserExistsInDB(String userName) {
		Optional<Credentials> record = credentialsRepository.findById(userName);
		if(record.isPresent()) {
//			logger.info(record.get().toString());
			throw new RuntimeException("Username already exists, try logging in!");
		}
		
		
	}

	private void createUserInDB(UserDetails user) {
		credentialsRepository.save(createCredentialObject(user));
		
	}

	private Credentials createCredentialObject(UserDetails user) {
		credentials.setUserName(user.getUserName());
		credentials.setPassword(user.getPassword());
		credentials.setName(user.getName());
		credentials.setIsMentor(user.getIsMentor());
		if(user.getIsMentor()) {
			credentials.setInterFirm(user.getInterFirm());
			credentials.setFullTmOffer(user.getFullTmOffer());
			credentials.setCaseName(user.getCaseName());
		}
		return credentials;
	}
	@Deprecated
	private void createUser(UserDetails user) throws IOException {
		String[] arr = new String[8];
		arr[0]=user.getUserName();
		arr[1]=EncodePasswordService.encode(user.getPassword());
		arr[2]=user.getName();
		arr[3]=user.getIsMentor()? "Mentor": "Mentee";
		arr[4]=user.getNumberOfMatches();
		if(user.getIsMentor()) {
			arr[5]=user.getInterFirm();
			arr[6]=user.getFullTmOffer();
			arr[7]=user.getCaseName();
		}
		String fileName = "./src/main/resources/credentials.csv";
		File file = new File(fileName);
		FileWriter outputfile = new FileWriter(file,true);
		CSVWriter writer = new CSVWriter(outputfile);
		writer.writeNext(arr);
		writer.close();
	}
	@Deprecated
	private void checkUserExists(String userName) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if (strings[0].equalsIgnoreCase(userName)) {
					throw new RuntimeException("User Already exists kindly use sign in!");
				}
			}
		}
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
