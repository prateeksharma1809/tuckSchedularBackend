package com.tuck.matches.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class SchedularService {

	String pattern = "yyyy-MM-dd HH:mm";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	Logger logger = LoggerFactory.getLogger(SchedularService.class);

	public void schedule() throws IOException, CsvException, ParseException {
		List<String[]> mentors = null;
		List<String[]> mentees = null;
		List<String[]> cred = null;
		List<String[]> masterMatchs = null;
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/credentials.csv"))) {
			cred = reader.readAll();
		}
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/mentorAvailabilities.csv"))) {
			mentors = reader.readAll();
		}
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/menteeAvailabilities.csv"))) {
			mentees = reader.readAll();
		}
		try (CSVReader reader = new CSVReader(new FileReader("./src/main/resources/masterMatch.csv"))) {
			masterMatchs = reader.readAll();
		}
		/**
		 * index 0 = user name 1 = time 2 = available 3 onwards = subjects
		 */
		List<String[]> matches = new ArrayList<String[]>();
		for (int j = 0; j < 2; j++) {
			if (null != mentees && null != mentors && null != cred && null!=masterMatchs) {
				List<String[]> mentorsWith45minTime = breakdownTimeIn45mins(mentors);
				mentors = mentorsWith45minTime;
				for (String[] mentor : mentorsWith45minTime) {
					logger.info("mentor :{}", Arrays.toString(mentor));
					if (mentor.length > 3 && "0".equals(mentor[3])) {
						List<Integer> potentialMatchesIndex = new ArrayList<>();
						int menteeIndex = 0;
						for (String[] mentee : mentees) {
							logger.info("mentee :{}", Arrays.toString(mentee));
							if (mentee.length > 3 && "0".equals(mentee[3])
									&& checkCred(mentee, cred, String.valueOf(j), matches, mentor[0],masterMatchs)) {
								Date fromDate = new SimpleDateFormat(pattern).parse(mentor[1]);
								Date toDate = new SimpleDateFormat(pattern).parse(mentor[2]);
								Date fromDateMentee = new SimpleDateFormat(pattern).parse(mentee[1]);
								Date toDateMentee = new SimpleDateFormat(pattern).parse(mentee[2]);
								if (fromDate.compareTo(fromDateMentee) >= 0 && toDate.compareTo(toDateMentee) <= 0) {
									potentialMatchesIndex.add(menteeIndex);
								}
							}
							menteeIndex++;
						}
						boolean check = false;
						for (Integer index : potentialMatchesIndex) {
							String[] mentee = mentees.get(index);
							String match = checkCaseMatch(mentee, mentor);
							if (!"".equals(match)) {
								check = true;
								String[] newArray = new String[5];
								String[] masterArry = new String[2];
								newArray[0] = mentor[0];
								masterArry[0] = mentor[0];
								newArray[1] = mentee[0];
								masterArry[1] = mentee[0];
								newArray[2] = mentor[1];
								newArray[3] = mentor[2];
								newArray[4] = match;
								logger.info("Matches:{}", Arrays.toString(newArray));
								matches.add(newArray);
								masterMatchs.add(masterArry);
								mentee[3] = "1";
								mentor[3] = "1";
								markCred1(cred, mentee[0]);
								break;
							}
						}
						if (!check && !potentialMatchesIndex.isEmpty()) {
							String[] mentee = mentees.get(potentialMatchesIndex.get(0));
							String[] newArray = new String[5];
							String[] masterArry = new String[2];
							newArray[0] = mentor[0];
							masterArry[0] = mentor[0];
							newArray[1] = mentee[0];
							masterArry[1] = mentee[0];
							newArray[2] = mentor[1];
							newArray[3] = mentor[2];
							logger.info("Matches:{}", Arrays.toString(newArray));
							matches.add(newArray);
							masterMatchs.add(masterArry);
							mentee[3] = "1";
							mentor[3] = "1";
							markCred1(cred, mentee[0]);
						}
					}
				}
			}
		}
		File file = new File("./src/main/resources/matches.csv");
		FileWriter outputfile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputfile);
		writer.writeAll(matches);
		writer.close();
		updateMenteeAndMentorFiles(mentors, mentees, cred,masterMatchs);
		this.sendMails(mentors, cred, matches);

	}

	private void markCred1(List<String[]> cred, String string) {
		for (String[] credentails : cred) {
			if (credentails[0].equals(string)) {
				int i = Integer.parseInt(credentails[4]);
				credentails[4] = String.valueOf(i + 1);
			}
		}
	}

	private boolean checkCred(String[] mentee, List<String[]> cred, String iterationNumber, List<String[]> matches,
			String mentor, List<String[]> masterMatch) {
		if(checkPairExists(mentee[0],mentor , masterMatch)) {
			return false;
		}
		for (String[] credentails : cred) {
			if (iterationNumber.equals("1")) {
				for (String[] match : matches) {
					if (mentee[0].equals(match[1]) && mentor.equals(match[0])) {
						return false;
					}
				}
			}
			if (credentails[0].equals(mentee[0]) && credentails[4].equals(iterationNumber)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkPairExists(String mentee, String mentor, List<String[]> masterMatchs) {
		int i = 0;
		if(masterMatchs!=null && !masterMatchs.isEmpty()) {
			for (String[] masterMatch : masterMatchs) {
				if(masterMatch.length>=2 && mentor.equals(masterMatch[0]) && mentee.equals(masterMatch[1])) {
					i++;
				}
			}
			if(i<=1) {
				return false;
			}
		}
		return true;
	}

	private List<String[]> breakdownTimeIn45mins(List<String[]> mentors) throws ParseException {
		List<String[]> list = new ArrayList<String[]>();
		for (String[] mentor : mentors) {
			logger.info("mentor :{}", Arrays.toString(mentor));
			if (mentor.length > 3) {
				Date fromDate = new SimpleDateFormat(pattern).parse(mentor[1]);
				Date toDate = new SimpleDateFormat(pattern).parse(mentor[2]);
				long diff = 0;
				do {
					String[] s = new String[mentor.length];
					Date newFromDate = DateUtils.addMinutes(fromDate, 45);
					diff = (toDate.getTime() - newFromDate.getTime()) / (1000 * 60);
					for (int i = 0; i < mentor.length; i++) {
						if (i == 1) {
							String stringStartDate = simpleDateFormat.format(fromDate);
							s[i] = stringStartDate;
						} else if (i == 2) {
							String stringEndDate = simpleDateFormat.format(newFromDate);
							s[i] = stringEndDate;
						} else {
							s[i] = mentor[i];
						}
					}
					logger.info("time diff in minutes in do while loop : {}", String.valueOf(diff));
					logger.info("s :{}", Arrays.toString(s));
					list.add(s);

					fromDate = newFromDate;
				} while (diff >= 45);

			}
		}
		return list;

	}

	private void sendMails(List<String[]> mentors, List<String[]> cred, List<String[]> matches)
			throws FileNotFoundException, IOException, CsvException {
		SendMailService s = new SendMailService();
		for (String[] strings : matches) {
			String[] mentorCredentials = getCred(cred,strings[0]);
			String[] menteeCredentials = getCred(cred,strings[1]);
			String body = "Hi "+mentorCredentials[2]+
			",\n You are matched with : "+menteeCredentials[2]+", Email : " + strings[1] + " \nfrom : " + strings[2] + " to : "
					+ strings[3];
			if (strings.length>5 && !strings[4].isEmpty()) {
				body += " common case type is : " + strings[4];
			}
			logger.info("mentor body : {}", body);
			s.sendMail(strings[0], "Case and time Matched", body);
			
			body = "Hi "+menteeCredentials[2]+",\n You are matched with : "+mentorCredentials[2]+", Email : " + strings[0] + " \nfrom : " + strings[2] + " to: " + strings[3];
			if (strings.length>5 && !strings[4].isEmpty()) {
				body += " common case type is : " + strings[4];
			}
			if(!mentorCredentials[5].isEmpty())
				body +="\n "+mentorCredentials[2]+" has done internship from :\n"+mentorCredentials[5];
			if(!mentorCredentials[6].isEmpty())
				body +="\n is holding a full time offer from :\n"+mentorCredentials[6];
			if(!mentorCredentials[7].isEmpty())
				body +="\n Cases are :\n"+mentorCredentials[7];
			logger.info("mentee body : {}", body);
			s.sendMail(strings[1], "Case and time Matched", body);
		}
		List<String[]> availableMentors = new ArrayList<String[]>();
		for (String[] mentor : mentors) {
			if(mentor[3].equals("0")) {
				availableMentors.add(mentor);
			}
		}
		if(!availableMentors.isEmpty()) {
			for (String[] mentee : cred) {
				if(mentee[3].equals("Mentee") && mentee[4].equals("0") ) {
					String body = "Hi Mentee, \n Your time slot does not coincide with any available mentor time slot,"
							+ "\n Don't worry we got you covered. Below are some slots available still.\n";
					for(String[] ava : availableMentors) {
						body +="\n Email: "+ava[0]+ "\t Available from : "+ava[1]+", till: "+ava[2];
					}
					logger.info("body : {}", body);
					s.sendMail(mentee[0], "Hurry!", body);
				}
			}
		}
		

	}

	private String[] getCred(List<String[]> cred, String name) {
		for (String[] strings : cred) {
			if(name.equals(strings[0])) {
				return strings;
			}
		}
		return new String[] {};
	}
    //reset cred file with 0 in match column add a master match file 
	private void updateMenteeAndMentorFiles(List<String[]> mentors, List<String[]> mentees, List<String[]> cred, List<String[]> masterMatchs)
			throws IOException {
		//change the logic here to erase the files
		File mentor = new File("./src/main/resources/mentorAvailabilities.csv");
		FileWriter mentorFile = new FileWriter(mentor);
		CSVWriter mentorwriter = new CSVWriter(mentorFile);
		mentorwriter.writeAll(mentors);
		mentorwriter.close();

		File mentee = new File("./src/main/resources/menteeAvailabilities.csv");
		FileWriter menteeFile = new FileWriter(mentee);
		CSVWriter menteewriter = new CSVWriter(menteeFile);
		menteewriter.writeAll(mentees);
		menteewriter.close();
		
		File masterMatchesFile = new File("./src/main/resources/menteeAvailabilities.csv");
		FileWriter masterMatchesFileWriter = new FileWriter(masterMatchesFile);
		CSVWriter masterMatchesCSVWriter = new CSVWriter(masterMatchesFileWriter);
		masterMatchesCSVWriter.writeAll(masterMatchs);
		masterMatchesCSVWriter.close();

//		File credentials = new File("./src/main/resources/credentials.csv");
//		FileWriter credfile = new FileWriter(credentials);
//		CSVWriter credwriter = new CSVWriter(credfile);
//		credwriter.writeAll(cred);
//		credwriter.close();
	}

	private String checkCaseMatch(String[] mentee, String[] mentor) {
		for (int i = 4; i < mentor.length; i++) {
			for (int j = 4; j < mentee.length; j++) {
				if (mentor[i].equals(mentee[j])) {
					return mentor[i];
				}
			}
		}
		return "";
	}

}
