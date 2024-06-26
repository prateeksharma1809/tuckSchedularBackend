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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.Constants;
import com.tuck.matches.entities.Availabilities;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.entities.Matches;
import com.tuck.matches.entities.MatchesId;
import com.tuck.matches.repository.AvailabilitiesRepository;
import com.tuck.matches.repository.CredentialsRepository;
import com.tuck.matches.repository.MatchesJpaRepository;

public class SchedularService {

	String pattern = "yyyy-MM-dd HH:mm";

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

	Logger logger = LoggerFactory.getLogger(SchedularService.class);

	@Autowired
	private AvailabilitiesRepository availabilitiesRepository;

	@Autowired
	private MatchesJpaRepository matchesJpaRepository;

	@Autowired
	private CredentialsRepository credentialsRepository;

	private String delimiter = "::";

	public void schedular() {
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		List<Availabilities> mentorAvailabilities = availabilitiesRepository.retrieveMentorAvalabilities();
		List<Availabilities> menteeAvailabilities = availabilitiesRepository.retrieveMenteeAvalabilities();
		List<Matches> matchesRecord = matchesJpaRepository.findAll();
		List<Matches> newMatches = new ArrayList<Matches>();
		Map<String, Integer> mentorMatchCount = new HashMap<>();
		Map<String, Integer> menteeMatchCount = new HashMap<>();
		if (null != mentorAvailabilities && null != menteeAvailabilities && !mentorAvailabilities.isEmpty()
				&& !menteeAvailabilities.isEmpty()) {
			Collections.shuffle(mentorAvailabilities);
			Collections.shuffle(menteeAvailabilities);
			for (int iteraion = 1; iteraion <= 2; iteraion++) {
				logger.info("iteration no : {}", iteraion);
				for (Availabilities mentorAva : mentorAvailabilities) {
					Credentials mentorCred = credentialsRepository
							.getById(mentorAva.getAvailabilitiesId().getUserName());
					if (mentorCred.getNumberOfCases() == null || mentorCred.getNumberOfCases() == 0) {
						logger.info("setting number of cases to 10 for : {}",
								mentorAva.getAvailabilitiesId().getUserName());
						mentorCred.setNumberOfCases(5);
					}
					if (mentorCred != null && null == mentorAva.getIsMatched()
							&& mentorMatchCount.getOrDefault(mentorAva.getAvailabilitiesId().getUserName(),
									0) < mentorCred.getNumberOfCases()) {
						checkMenteeAvailabilities(menteeAvailabilities, matchesRecord, newMatches, mentorMatchCount,
								menteeMatchCount, iteraion, mentorAva);
					}

				}
			}
		}
		if (!newMatches.isEmpty()) {
			sendMailToMatched(newMatches);
		}
		sendMailToUnmatchedMentees(menteeAvailabilities, mentorAvailabilities, menteeMatchCount);
		matchesJpaRepository.saveAllAndFlush(matchesRecord);
		availabilitiesRepository.deleteAll();

	}

	private void checkMenteeAvailabilities(List<Availabilities> menteeAvailabilities, List<Matches> matchesRecord,
			List<Matches> newMatches, Map<String, Integer> mentorMatchCount, Map<String, Integer> menteeMatchCount,
			int iteraion, Availabilities mentorAva) {
		for (Availabilities menteeAva : menteeAvailabilities) {
			if (null == menteeAva.getIsMatched()
					&& menteeMatchCount.getOrDefault(menteeAva.getAvailabilitiesId().getUserName(), 0) < iteraion) {
				if (mentorAva.getAvailabilitiesId().getFrom().compareTo(menteeAva.getAvailabilitiesId().getFrom()) >= 0
						&& mentorAva.getAvailabilitiesId().getTo()
								.compareTo(menteeAva.getAvailabilitiesId().getTo()) <= 0
						&& !checkAlreadyMatched2Times(menteeAva, mentorAva, matchesRecord, newMatches, iteraion,
								menteeMatchCount)) {
					MatchesId matchedId = new MatchesId();
					matchedId.setEmail_mentee(menteeAva.getAvailabilitiesId().getUserName());
					matchedId.setEmail_mentor(mentorAva.getAvailabilitiesId().getUserName());
					matchedId.setFrom(mentorAva.getAvailabilitiesId().getFrom());
					matchedId.setTo(mentorAva.getAvailabilitiesId().getTo());
					Matches matches = new Matches();
					String cases = checkCasesMatched(menteeAva, mentorAva);
					matches.setMatchesId(matchedId);
					matches.setCases(cases);
					menteeAva.setIsMatched(true);
					mentorAva.setIsMatched(true);
					newMatches.add(matches);
					matchesRecord.add(matches);
					menteeMatchCount.put(menteeAva.getAvailabilitiesId().getUserName(),
							menteeMatchCount.getOrDefault(menteeAva.getAvailabilitiesId().getUserName(), 0) + 1);
					mentorMatchCount.put(mentorAva.getAvailabilitiesId().getUserName(),
							mentorMatchCount.getOrDefault(mentorAva.getAvailabilitiesId().getUserName(), 0) + 1);
					break;
				}
			}
		}
	}

	private void sendMailToUnmatchedMentees(List<Availabilities> menteeAvailabilities,
			List<Availabilities> mentorAvailabilities, Map<String, Integer> menteeMatchCount) {
		SendMailService sendMailService = new SendMailService();
//		Set<String> uniqueMentees = createUniqueMenteeList(menteeAvailabilities, menteeMatchCount);
//		for (String mentee : uniqueMentees) {
//			String body = "Hi Mentee, \nYour available time slots do not coincide with any available mentor slots,"
//					+ "\nPlease try again next week!";
//			logger.info("body : {}", body);
////			sendMailService.sendMail(mentee, "Sorry, we were not able to find you a case slot", body);
//		}
		// logic to send mail with available mentors -- uncomment below

		List<Availabilities> availableMentors = new ArrayList<>();
		for (Availabilities mentor : mentorAvailabilities) {
			if (mentor.getIsMatched() == null || !mentor.getIsMatched()) {
				availableMentors.add(mentor);
			}
		}
		if (!availableMentors.isEmpty()) {
			String body = "Hi, Below are the available time slots of mentors";
			for (Availabilities mentorAva : availableMentors) {
				Credentials mentorCred = credentialsRepository.getById(mentorAva.getAvailabilitiesId().getUserName());
				body += "\nEmail: " + mentorAva.getAvailabilitiesId().getUserName() + "\t Name: " + mentorCred.getName()
						+ "\nAvailable from : " + simpleDateFormat.format(mentorAva.getAvailabilitiesId().getFrom())
						+ ", till: " + simpleDateFormat.format(mentorAva.getAvailabilitiesId().getTo())
						+ "\n------------------------------------------------------------------------------------------\n";
			}
			logger.info("body : {}", body);
			sendMailService.sendMail(Constants.EMAIL_ID, "Available Time slots of mentors", body);
		} else {
			sendMailService.sendMail(Constants.EMAIL_ID, "Sorry, no slots were available for any mentor", "");
		}

	}

//	private Set<String> createUniqueMenteeList(List<Availabilities> menteeAvailabilities,
//			Map<String, Integer> menteeMatchCount) {
//		Set<String> UniqueMentee = new HashSet<>();
//		for (Availabilities mentee : menteeAvailabilities) {
//			if (!menteeMatchCount.containsKey(mentee.getAvailabilitiesId().getUserName())) {
//				if (mentee.getIsMatched() == null || !mentee.getIsMatched())
//					UniqueMentee.add(mentee.getAvailabilitiesId().getUserName());
//			}
//		}
//		logger.info("unique mentee list : {}", UniqueMentee);
//		return UniqueMentee;
//	}

	private void sendMailToMatched(List<Matches> newMatches) {
//		SendMailService sendMailService = new SendMailService();
		for (Matches matches : newMatches) {
			Credentials mentorCred = credentialsRepository.getById(matches.getMatchesId().getEmail_mentor());
			Credentials menteeCred = credentialsRepository.getById(matches.getMatchesId().getEmail_mentee());
			String body = "Hi " + mentorCred.getName() + ",\nYou are matched with : " + menteeCred.getName()
					+ ", Email : " + matches.getMatchesId().getEmail_mentee() + " \nfor the time slot  : "
					+ simpleDateFormat.format(matches.getMatchesId().getFrom()) + " to : "
					+ simpleDateFormat.format(matches.getMatchesId().getTo());
			if (!matches.getCases().isEmpty()) {
				String[] cases = matches.getCases().split(delimiter);
				if (cases.length > 0 && !cases[0].isEmpty()) {
					body += "\nThe mentee would prefer the case type : " + cases[0];
				}
			}
			logger.info("mentor body : {}", body);
//			sendMailService.sendMail(matches.getMatchesId().getEmail_mentor(), "You were matched with a mentee!", body);
			body = "Hi " + menteeCred.getName() + ",\nYou are matched with : " + mentorCred.getName() + ", Email : "
					+ matches.getMatchesId().getEmail_mentor() + " \nTime slot : "
					+ simpleDateFormat.format(matches.getMatchesId().getFrom()) + " to : "
					+ simpleDateFormat.format(matches.getMatchesId().getTo());
			if (!matches.getCases().isEmpty()) {
				String[] cases = matches.getCases().split(delimiter);
				if (cases.length > 1 && !cases[1].isEmpty()) {
					body += "\nCase type : " + cases[1];
				}
			}
			if (null != mentorCred.getInterFirm() && !mentorCred.getInterFirm().isEmpty())
				body += "\nInternship : " + mentorCred.getInterFirm();
			if (null != mentorCred.getFullTmOffer() && !mentorCred.getFullTmOffer().isEmpty())
				body += "\nFull time offer : " + mentorCred.getFullTmOffer();
			if (null != mentorCred.getOfficeLocation() && !mentorCred.getOfficeLocation().isEmpty())
				body += "\nOffice location : " + mentorCred.getOfficeLocation();
			if (null != mentorCred.getCaseName() && !mentorCred.getCaseName().isEmpty())
				body += "\nAvailable cases : " + mentorCred.getCaseName();
			logger.info("mentee body : {}", body);
//			sendMailService.sendMail(matches.getMatchesId().getEmail_mentee(), "You were matched with a mentor!", body);
		}
	}

	private String checkCasesMatched(Availabilities menteeAva, Availabilities mentorAva) {
		String match = "";
		if (menteeAva.getCases() != null && !"".equals(menteeAva.getCases())) {
			match = menteeAva.getCases();
		}
		match += delimiter;
		if (mentorAva.getCases() != null && !"".equals(mentorAva.getCases())) {
			match += mentorAva.getCases();
		}
		return match;
	}

	private boolean checkAlreadyMatched2Times(Availabilities menteeAva, Availabilities mentorAva,
			List<Matches> matchesRecord, List<Matches> newMatches, int iteraion,
			Map<String, Integer> menteeMatchCount) {
		if (null != menteeAva.getIsMatched() && menteeAva.getIsMatched()) {
			logger.info("Mentee time slot already matched");
			return true;
		}
		if (null != mentorAva.getIsMatched() && mentorAva.getIsMatched()) {
			logger.info("Mentor time slot already matched");
			return true;
		}
		/**
		 * for each week the match should not be with same mentor
		 */
		for (Matches matches : newMatches) {
			if (matches.getMatchesId().getEmail_mentee().equalsIgnoreCase(menteeAva.getAvailabilitiesId().getUserName())
					&& matches.getMatchesId().getEmail_mentor()
							.equalsIgnoreCase(mentorAva.getAvailabilitiesId().getUserName())) {
				return true;
			}
		}
		/**
		 * for checking that there are only 2 matches per mentee per cycle
		 */
		int countOfMatchesMenteePerCycle = 0;
		for (Matches matches : newMatches) {
			if (matches.getMatchesId().getEmail_mentee()
					.equalsIgnoreCase(menteeAva.getAvailabilitiesId().getUserName())) {
				countOfMatchesMenteePerCycle++;
				if (countOfMatchesMenteePerCycle >= iteraion) {
					logger.info("Mentee already paired " + iteraion + " times in this cycle");
					return true;
				}
			}
		}

		int i = 0;
		for (Matches matches : matchesRecord) {
			if (matches.getMatchesId().getEmail_mentee().equalsIgnoreCase(menteeAva.getAvailabilitiesId().getUserName())
					&& matches.getMatchesId().getEmail_mentor()
							.equalsIgnoreCase(mentorAva.getAvailabilitiesId().getUserName())) {
				i++;
			}
		}
		if (i >= 2) {
			logger.info("Mentor Mentee already paired 2 times");
			return true;
		}
		return false;
	}

	@Deprecated
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
			if (null != mentees && null != mentors && null != cred && null != masterMatchs) {
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
									&& checkCred(mentee, cred, String.valueOf(j), matches, mentor[0], masterMatchs)) {
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
		updateMenteeAndMentorFiles(mentors, mentees, cred, masterMatchs);
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
		if (checkPairExists(mentee[0], mentor, masterMatch)) {
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
		if (masterMatchs != null && !masterMatchs.isEmpty()) {
			for (String[] masterMatch : masterMatchs) {
				if (masterMatch.length >= 2 && mentor.equals(masterMatch[0]) && mentee.equals(masterMatch[1])) {
					i++;
				}
			}
			if (i <= 1) {
				return false;
			}
		} else {
			return false;
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

	@Deprecated
	private void sendMails(List<String[]> mentors, List<String[]> cred, List<String[]> matches)
			throws FileNotFoundException, IOException, CsvException {
		SendMailService s = new SendMailService();
		for (String[] strings : matches) {
			String[] mentorCredentials = getCred(cred, strings[0]);
			String[] menteeCredentials = getCred(cred, strings[1]);
			String body = "Hi " + mentorCredentials[2] + ",\n You are matched with : " + menteeCredentials[2]
					+ ", Email : " + strings[1] + " \nfrom : " + strings[2] + " to : " + strings[3];
			if (strings.length > 5 && !strings[4].isEmpty()) {
				body += " common case type is : " + strings[4];
			}
			logger.info("mentor body : {}", body);
			s.sendMail(strings[0], "Case and time Matched", body);

			body = "Hi " + menteeCredentials[2] + ",\n You are matched with : " + mentorCredentials[2] + ", Email : "
					+ strings[0] + " \nfrom : " + strings[2] + " to: " + strings[3];
			if (strings.length > 5 && !strings[4].isEmpty()) {
				body += " common case type is : " + strings[4];
			}
			if (!mentorCredentials[5].isEmpty())
				body += "\n " + mentorCredentials[2] + " has done internship from :\n" + mentorCredentials[5];
			if (!mentorCredentials[6].isEmpty())
				body += "\n is holding a full time offer from :\n" + mentorCredentials[6];
			if (!mentorCredentials[7].isEmpty())
				body += "\n Cases are :\n" + mentorCredentials[7];
			logger.info("mentee body : {}", body);
			s.sendMail(strings[1], "Case and time Matched", body);
		}
		List<String[]> availableMentors = new ArrayList<String[]>();
		for (String[] mentor : mentors) {
			if (mentor[3].equals("0")) {
				availableMentors.add(mentor);
			}
		}
		if (!availableMentors.isEmpty()) {
			for (String[] mentee : cred) {
				if (mentee[3].equals("Mentee") && mentee[4].equals("0")) {
					String body = "Hi Mentee, \n Your time slot does not coincide with any available mentor time slot,"
							+ "\n Don't worry we got you covered. Below are some slots available still.\n";
					for (String[] ava : availableMentors) {
						body += "\n Email: " + ava[0] + "\t Available from : " + ava[1] + ", till: " + ava[2];
					}
					logger.info("body : {}", body);
					s.sendMail(mentee[0], "Hurry!", body);
				}
			}
		}

	}

	@Deprecated
	private String[] getCred(List<String[]> cred, String name) {
		for (String[] strings : cred) {
			if (name.equals(strings[0])) {
				return strings;
			}
		}
		return new String[] {};
	}

	// reset cred file with 0 in match column add a master match file
	@Deprecated
	private void updateMenteeAndMentorFiles(List<String[]> mentors, List<String[]> mentees, List<String[]> cred,
			List<String[]> masterMatchs) throws IOException {
		// change the logic here to erase the files
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

		File masterMatchesFile = new File("./src/main/resources/masterMatch.csv");
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
	
	@Async
	public CompletableFuture<String> schedularAsync() {
		this.schedular();
		return CompletableFuture.completedFuture("Success");
	}

}
