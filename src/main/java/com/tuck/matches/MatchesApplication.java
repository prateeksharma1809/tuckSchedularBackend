package com.tuck.matches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchesApplication{// extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MatchesApplication.class, args);
	}
//	//new code for war
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(MatchesApplication.class);
//    }
}
