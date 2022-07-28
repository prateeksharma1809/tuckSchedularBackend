package com.tuck.matches.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.Availabilitys;
import com.tuck.matches.beans.Constants;
import com.tuck.matches.beans.LoginForm;
import com.tuck.matches.beans.LoginResponse;
import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.beans.UserDetailsWithAvalabilites;
import com.tuck.matches.service.CheckCredentials;
import com.tuck.matches.service.EditProfileService;
import com.tuck.matches.service.GetUserDetailsService;
import com.tuck.matches.service.RegisterAvailabilities;
import com.tuck.matches.service.SignUpService;

@RestController
public class NewRestController {
	
	Logger logger = LoggerFactory.getLogger(NewRestController.class);
	
	@Autowired
	private SignUpService signUpService;
	
	@Autowired
	private GetUserDetailsService getUserDetailsService;
	
	@Autowired
	private RegisterAvailabilities registerAvailabilities;

	@Autowired
	private CheckCredentials checkCredentials;
	
	@Autowired
	EditProfileService editProfileService;
	
	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@PostMapping("/sign-up")
	public String signUp(@RequestBody UserDetails userdetails ) {
		logger.info("sign up new :{} ", userdetails.toString());
		signUpService.signUpDB(userdetails);
		return "Success";
			
	}
	
	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginForm loginForm ) {
		logger.info("login new :{}",loginForm.toString());
		LoginResponse loginResponse = checkCredentials.checkInDB(loginForm);
		if(!loginResponse.getIsAdmin() && !loginResponse.getOpenPage()) {
			throw new RuntimeException("User Name or Password incorrect!");
		}
		return loginResponse;
	}

	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@GetMapping("/get-user-details/{username}")
	public UserDetailsWithAvalabilites getUserDetails(@PathVariable String username , @RequestHeader String password) {
		logger.info("username:{}",username);
		UserDetailsWithAvalabilites userDetails = getUserDetailsService.getUserDetailsFromDB(username, password, true);
		if(null == userDetails) {
			throw new RuntimeException("User does not exist Kindly click on Sign up or try with correct credentials!");
		}
		return userDetails;
			
	}
	
	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@PostMapping("/availabilities")
	public String availableities(@RequestBody Availabilitys availabilitys ) throws IOException, CsvException {
		logger.info(availabilitys.toString());
		if(null !=availabilitys.getIsMentor()) {
			registerAvailabilities.registerInDB(availabilitys);
		}else {
			throw new RuntimeException("Wrong input kindly retry with correct inputs!");
		}
		return "Success!";
	}
	
	
	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@PostMapping("/delete-availability")
	public String deleteAvalibility(@RequestBody Availabilitys availabilitys) {
		logger.info(availabilitys.toString());
		registerAvailabilities.deleteEntryInDB(availabilitys);
		return "Success";		
	}
	
	@CrossOrigin(origins = Constants.ORIGIN_URL)
	@PostMapping("/edit-profile")
	public String editProfile(@RequestBody UserDetails userdetails ) {
		logger.info(userdetails.toString());
		editProfileService.editProfile(userdetails);
		return "Success";
	}
}
