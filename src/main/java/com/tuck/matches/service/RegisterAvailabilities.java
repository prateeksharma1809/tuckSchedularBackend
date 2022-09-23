package com.tuck.matches.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.Availabilitys;
import com.tuck.matches.beans.LoginForm;
import com.tuck.matches.beans.LoginResponse;
import com.tuck.matches.entities.Availabilities;
import com.tuck.matches.entities.AvailabilitiesId;
import com.tuck.matches.repository.AvailabilitiesRepository;

public class RegisterAvailabilities {
	String pattern = "yyyy-MM-dd HH:mm";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
	@Autowired
	private CheckCredentials checkCred;
	
	@Autowired
	private AvailabilitiesRepository availabilitiesRepository;

	Logger logger = LoggerFactory.getLogger(EditProfileService.class);

	public void registerInDB(Availabilitys availabilitys) {
		this.validate(availabilitys);
		AvailabilitiesId availabilitiesId = new AvailabilitiesId();
		availabilitiesId.setUserName(availabilitys.getUserName());
		Date startdate = availabilitys.getStartDate();
		Date enddate = availabilitys.getEndDate();
		long diff = (enddate.getTime() - startdate.getTime()) / (1000 * 60);
		logger.info("time diff in minutes : {}", String.valueOf(diff));
		if (diff < 45) {
			throw new RuntimeException("Slots can be booked with 45 minutes minimum time difference!");
		}
		Date startDateRounded = DateUtils.truncate(availabilitys.getStartDate(), Calendar.SECOND);
		availabilitiesId.setFrom(startDateRounded);
		logger.info("start time rounded : {}", String.valueOf(startDateRounded));
		Date endDateRounded = DateUtils.truncate(availabilitys.getEndDate(), Calendar.SECOND);
		logger.info("end time rounded : {}", String.valueOf(startDateRounded));
		availabilitiesId.setTo(endDateRounded);
		Availabilities availabilities = new Availabilities();
		availabilities.setAvailabilitiesId(availabilitiesId);
		availabilities.setIsMentor(availabilitys.getIsMentor());
		StringBuilder strbuild = new StringBuilder();
		for (String str : availabilitys.getOptions()) {
			strbuild.append(str+";");
		}
		availabilities.setCases(strbuild.toString());
		availabilitiesRepository.save(availabilities);
	}
	
	@Deprecated
	private void createEntry(Availabilitys availabilitys, String filePath) throws IOException {
		String username = availabilitys.getUserName();
		Date startdate = availabilitys.getStartDate();
		Date enddate = availabilitys.getEndDate();
		long diff = (enddate.getTime() - startdate.getTime()) / (1000 * 60);
		logger.info("time diff in minutes : {}", String.valueOf(diff));
		if (diff < 45) {
			throw new RuntimeException("Slots can be booked with 45 minutes minimum time difference!");
		}

		String stringStartDate = simpleDateFormat.format(startdate);
		String stringEndDate = simpleDateFormat.format(enddate);
		String[] array = availabilitys.getOptions().toArray(new String[0]);
		String[] newArray = new String[array.length + 4];
		int i = 4;
		newArray[0] = username;
		newArray[1] = stringStartDate;
		newArray[2] = stringEndDate;
		newArray[3] = "0";
		for (String string : array) {
			newArray[i] = string;
			i++;
		}
		File file = new File(filePath);
		FileWriter outputfile = new FileWriter(file, true);
		CSVWriter writer = new CSVWriter(outputfile);
		writer.writeNext(newArray);
		writer.close();
	}

	@Deprecated
	public void registerMentor(Availabilitys availabilitys) throws IOException, CsvException {
		this.validate(availabilitys);
		String fileName = "./src/main/resources/mentorAvailabilities.csv";
		this.createEntry(availabilitys, fileName);
	}

	@Deprecated
	public void registerMentee(Availabilitys availabilitys) throws IOException, CsvException {
		this.validate(availabilitys);
		String fileName = "./src/main/resources/menteeAvailabilities.csv";
		this.createEntry(availabilitys, fileName);
	}

	private void validate(Availabilitys availabilitys) {
		LoginResponse response = checkCred.checkInDB(this.getLoginForm(availabilitys));
		if (!response.getOpenPage()) {
			throw new RuntimeException("Invaild username or password kindly login again");
		}

	}

	private LoginForm getLoginForm(Availabilitys availabilitys) {
		LoginForm loginForm = new LoginForm();
		loginForm.setUserName(availabilitys.getUserName());
		loginForm.setPassword(availabilitys.getPassword());
		return loginForm;

	}
	
	public void deleteEntryInDB(Availabilitys availabilitys)  {
		this.validate(availabilitys);
		AvailabilitiesId availabilitiesId = new AvailabilitiesId();
		availabilitiesId.setUserName(availabilitys.getUserName());
		availabilitiesId.setFrom(availabilitys.getStartDate());
		availabilitiesId.setTo(availabilitys.getEndDate());
		availabilitiesRepository.deleteById(availabilitiesId);
		
	}
	

	@Deprecated
	public void deleteEntry(Availabilitys availabilitys) throws IOException, CsvException {
		this.validate(availabilitys);
		String fileName = "";
		if (null != availabilitys.getIsMentor() && availabilitys.getIsMentor()) {
			fileName = "./src/main/resources/mentorAvailabilities.csv";
		} else if (null != availabilitys.getIsMentor() && !availabilitys.getIsMentor()) {
			fileName = "./src/main/resources/menteeAvailabilities.csv";
		} else {
			throw new RuntimeException("Record Not found");
		}
		this.deleteEntry(availabilitys, fileName);
	}

	@Deprecated
	private void deleteEntry(Availabilitys availabilitys, String fileName) throws IOException, CsvException {
		String username = availabilitys.getUserName();
		Date startdate = availabilitys.getStartDate();
		Date enddate = availabilitys.getEndDate();
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String stringStartDate = simpleDateFormat.format(startdate);
		String stringEndDate = simpleDateFormat.format(enddate);
		List<String[]> r = null;
		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
			r = reader.readAll();

			Iterator<String[]> i = r.iterator();
			while (i.hasNext()) {
				String[] strings = i.next();
				if (this.match(strings, username, stringStartDate, stringEndDate)) {
					i.remove();
				}
			}

		}

		File mentor = new File(fileName);
		FileWriter mentorFile = new FileWriter(mentor);
		CSVWriter mentorwriter = new CSVWriter(mentorFile);
		mentorwriter.writeAll(r);
		mentorwriter.close();

	}
	
	@Deprecated
	private boolean match(String[] strings, String username, String stringStartDate, String stringEndDate) {
		if (username.equals(strings[0]) && stringStartDate.equals(strings[1]) && stringEndDate.equals(strings[2])) {
			return true;
		}
		return false;
	}

	

}
