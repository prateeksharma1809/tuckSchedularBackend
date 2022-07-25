package com.tuck.matches.service;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.Availabilitys;
import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.beans.UserDetailsWithAvalabilites;

public class GetUserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(GetUserDetailsService.class);
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	


	public UserDetails getDetails(String username, String password) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, username, password)) {
					return populateUserDatils(strings);
				}
			}
		}
		return null;
	}

	public UserDetailsWithAvalabilites getDetails(String username, String password, boolean flag) throws IOException, CsvException, ParseException {
		UserDetailsWithAvalabilites ava = null;
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(this.match(strings, username, password)) {
					ava=  populateUserDatils(strings, true);
				}
			}
		}
		if(ava==null) {
			return null;
		}
		if(ava.getIsMentor()) {
			try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/mentorAvailabilities.csv"))) {
				List<String[]> r = reader.readAll();
				for (String[] strings : r) {
					if(strings[0].equals(username)) {
						populateAvalabilities(strings, ava);
					}
				}
			}
		}else {
			try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/menteeAvailabilities.csv"))) {
				List<String[]> r = reader.readAll();
				for (String[] strings : r) {
					if(strings[0].equals(username)) {
						populateAvalabilities(strings, ava);
					}
				}
			}
		}
		return ava;
	}
	

	private void populateAvalabilities(String[] strings, UserDetailsWithAvalabilites ava) throws ParseException {
		Availabilitys availability = new Availabilitys();
		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		availability.setStartDate(formatter.parse(strings[1]));
		availability.setEndDate(formatter.parse(strings[2]));
		ava.getAvailabilitys().add(availability);
	}

	private UserDetails populateUserDatils(String[] strings) {
		logger.info(Arrays.toString(strings));
		UserDetails userDetails = new UserDetails();
		if(strings.length>3) {
			for(int i=0; i <strings.length;i++) {
				switch(i) {
				  case 0:
					userDetails.setUserName(strings[0]);
						break;
				  case 1:
					  userDetails.setPassword(DecodePasswordService.decode(strings[1]));
						break;
				  case 2:
					  userDetails.setName(strings[2]);
						break;
				  case 3:
					  userDetails.setIsMentor("Mentor".equals(strings[3]));
						break;
				  case 5:
					  userDetails.setInterFirm(strings[5]);
						break;
				  case 6:
					  userDetails.setFullTmOffer(strings[6]);
						break;
				  case 7:
					  userDetails.setCaseName(strings[7]);
						break;
				}
			}
		}
		return userDetails;
	}
	
	private UserDetailsWithAvalabilites populateUserDatils(String[] strings, boolean flag) {
		logger.info(Arrays.toString(strings));
		UserDetailsWithAvalabilites userDetails = new UserDetailsWithAvalabilites();
		if(strings.length>3) {
			for(int i=0; i <strings.length;i++) {
				switch(i) {
				  case 0:
					userDetails.setUserName(strings[0]);
						break;
				  case 1:
					  userDetails.setPassword(DecodePasswordService.decode(strings[1]));
						break;
				  case 2:
					  userDetails.setName(strings[2]);
						break;
				  case 3:
					  userDetails.setIsMentor("Mentor".equals(strings[3]));
						break;
				  case 5:
					  userDetails.setInterFirm(strings[5]);
						break;
				  case 6:
					  userDetails.setFullTmOffer(strings[6]);
						break;
				  case 7:
					  userDetails.setCaseName(strings[7]);
						break;
				}
			}
		}
		return userDetails;
	}

	private boolean match(String[] cred, String user, String password) {
		logger.info(Arrays.toString(cred));
		logger.info(user);
		String password1 = "";
		if(cred[1]!=null) {
			password1=DecodePasswordService.decode(cred[1]);
		}
		if(cred.length>1 && cred[0].equalsIgnoreCase(user) && password1.equals(password)) {
			return true;
		}
		return false;

	}

}
