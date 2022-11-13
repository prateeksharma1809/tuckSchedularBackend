package com.tuck.matches.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.tuck.matches.service.CheckCredentials;
import com.tuck.matches.service.EditProfileService;
import com.tuck.matches.service.ForgotPasswordService;
import com.tuck.matches.service.GetUserDetailsService;
import com.tuck.matches.service.RegisterAvailabilities;
import com.tuck.matches.service.SchedularService;
import com.tuck.matches.service.SendMailService;
import com.tuck.matches.service.SignUpService;

@Configuration
public class BeanConfiguration {
	
	
	@Bean
	@Scope("prototype")
	public SchedularService schedularService() {
		return new SchedularService();
	}
	
	
	@Bean
	@Scope("prototype")
	public RegisterAvailabilities registerAvailabilities() {
		return new RegisterAvailabilities();
	}
	

	@Bean
	@Scope("prototype")
	public SendMailService sendMailService(){
		return new SendMailService();
	}
	
	@Bean
	@Scope("prototype")
	public EditProfileService editProfileService(){
		return new EditProfileService();
	}


	@Bean
	@Scope("prototype")
	public ForgotPasswordService forgotPasswordService() {
		return new ForgotPasswordService();
	}

	
	@Bean
	@Scope("prototype")
	public GetUserDetailsService GetUserDetailsService() {
		return new GetUserDetailsService();
	}
	
	@Bean
	@Scope("prototype")
	public CheckCredentials checkCredentials() {
		return new CheckCredentials();
	}
	
	

	@Bean
	@Scope("prototype")
	public SignUpService signUpService() {
		return new SignUpService();
	}
	
	
//	@Bean
//	@Scope("prototype")
//	public UserDetails userDetails() {
//		return new UserDetails();
//	}
//	
//	@Bean
//	@Scope("prototype")
//	public Credentials credentials() {
//		return new Credentials();
//	}
	
//	@Bean
//	@Scope("prototype")
//	public UserDetailsWithAvalabilites userDetailsWithAvalabilites() {
//		return new UserDetailsWithAvalabilites();
//	}
//	
	
//	@Bean
//	@Scope("prototype")
//	public LoginForm loginForm() {
//		return new LoginForm();
//	}
//	
//	@Bean
//	@Scope("prototype")
//	public LoginResponse loginResponse() {
//		return new LoginResponse();
//	}
	
//	@Bean
//	@Scope("prototype")
//	public AvailabilitiesId availabilitiesId() {
//		return new AvailabilitiesId();
//	}
//	@Bean
//	@Scope("prototype")
//	public Availabilities availabilities() {
//		return new Availabilities();
//	}
	
}
