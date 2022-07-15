package com.tuck.matches.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.UserDetails;

public class EditProfileService {
	
	Logger logger = LoggerFactory.getLogger(EditProfileService.class);
	
	public void editProfile(UserDetails user) throws IOException, CsvException {
	
		this.updateProfile(user);

	}

	private void updateProfile(UserDetails user) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {
				if(strings[0].equals(user.getUserName())) {
					strings[1]=EncodePasswordService.encode(user.getPassword());
					if(user.getIsMentor()) {
						strings[6]=user.getFullTmOffer();
						strings[7]=user.getCaseName();
					}
					break;
				}
			}
			updateFile(r);
		}
		
	}

	private void updateFile(List<String[]> r) throws IOException {
		String fileName = "./src/main/resources/credentials.csv";
		File file = new File(fileName);
		FileWriter outputfile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputfile);
		writer.writeAll(r);
		writer.close();
	}	

}
