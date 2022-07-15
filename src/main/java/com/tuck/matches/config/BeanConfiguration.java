package com.tuck.matches.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tuck.matches.beans.LoginForm;

@Configuration
public class BeanConfiguration {
	
	
	@Bean
	public LoginForm loginForm() {
		return new LoginForm();
	}

}
