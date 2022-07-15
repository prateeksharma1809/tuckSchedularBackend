package com.tuck.matches.controller;

import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.exceptions.CsvException;
import com.tuck.matches.beans.Availabilitys;
import com.tuck.matches.beans.LoginForm;
import com.tuck.matches.beans.LoginResponse;
import com.tuck.matches.beans.ResetPasswordBean;
import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.beans.UserDetailsWithAvalabilites;
import com.tuck.matches.service.CheckCredentials;
import com.tuck.matches.service.EditProfileService;
import com.tuck.matches.service.ForgotPasswordService;
import com.tuck.matches.service.GetUserDetailsService;
import com.tuck.matches.service.RegisterAvailabilities;
import com.tuck.matches.service.SchedularService;
import com.tuck.matches.service.SignUpService;

@RestController
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);
	

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginForm loginForm ) throws IOException, CsvException {
		logger.info(loginForm.toString());
		CheckCredentials c = new CheckCredentials();
		LoginResponse b = c.check(loginForm);
		if(!b.getIsAdmin() && !b.getOpenPage()) {
			throw new RuntimeException("User Name or Password incorrect!");
		}
		return b;
			
	}
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/get-user-details/{username}")
	public UserDetailsWithAvalabilites getUserDetails(@PathVariable String username , @RequestHeader String password) throws IOException, CsvException, ParseException {
		logger.info("username:{}",username);
		GetUserDetailsService getUserDetailsService = new GetUserDetailsService();
		UserDetailsWithAvalabilites userDetails = getUserDetailsService.getDetails(username, password, true);
		if(null == userDetails) {
			throw new RuntimeException("User does not exist Kindly click on Sign up or try with correct credentials!");
		}
		return userDetails;
			
	}

	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/availabilities")
	public String availableities(@RequestBody Availabilitys availabilitys ) throws IOException, CsvException {
		logger.info(availabilitys.toString());	
		RegisterAvailabilities registerAvailabilities = new RegisterAvailabilities();
		if(null !=availabilitys.getIsMentor() && availabilitys.getIsMentor()) {
			registerAvailabilities.registerMentor(availabilitys);
			//return success message
		}else if(null !=availabilitys.getIsMentor() && !availabilitys.getIsMentor()) {
			registerAvailabilities.registerMentee(availabilitys);
			//return success message
		}else {
			throw new RuntimeException("Wrong input kindly retry with correct inputs!");
		}
		return "Success!";
	}
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/generate-otp/{username}")
	public String generateOTP(@PathVariable String username ) throws IOException, CsvException {
		logger.info(username);
		if(null == username) {
			throw new RuntimeException("User does not exist Kindly click on Sign up or try with correct credentials!");
		}
		ForgotPasswordService f = new ForgotPasswordService();
		f.generateOtp(username);
		return "Success";
			
	}
	

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/reset-password")
	public String resetPassword(@RequestBody ResetPasswordBean reset ) throws IOException, CsvException {
		logger.info(reset.toString());
		ForgotPasswordService f = new ForgotPasswordService();
		f.resetCall(reset);
		return "Success";
			
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/delete-availability")
	public String deleteAvalibility(@RequestBody Availabilitys availabilitys) throws IOException, CsvException {
		logger.info(availabilitys.toString());
		RegisterAvailabilities registerAvailabilities = new RegisterAvailabilities();
		registerAvailabilities.deleteEntry(availabilitys);
		return "Success";
			
	}
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/sign-up")
	public String signUp(@RequestBody UserDetails userdetails ) throws IOException, CsvException {
		logger.info(userdetails.toString());
		SignUpService s = new SignUpService();
		s.signUp(userdetails);
		return "Success";
			
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/edit-profile")
	public String editProfile(@RequestBody UserDetails userdetails ) throws IOException, CsvException {
		logger.info(userdetails.toString());
		EditProfileService s = new EditProfileService();
		s.editProfile(userdetails);
		return "Success";
	}
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/schedule/{username}")
	public String schedule(@PathVariable String username , @RequestHeader String password) throws IOException, CsvException, ParseException {
		logger.info("username {}, password:{}",username, password);
		if("Admin".equals(username) && "Admin11!".equals(password)) {
			SchedularService s = new SchedularService();
			s.schedule();
		}else {
			throw new RuntimeException("Only admins allowed to perform this action!");
		}
		return "Success";
			
	}
	
	
	
	
}
