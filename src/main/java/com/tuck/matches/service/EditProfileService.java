package com.tuck.matches.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tuck.matches.beans.UserDetails;
import com.tuck.matches.entities.Credentials;
import com.tuck.matches.repository.CredentialsRepository;

public class EditProfileService {
	
	Logger logger = LoggerFactory.getLogger(EditProfileService.class);
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	public void editProfile(UserDetails user){
	
		this.updateProfileinDB(user);
	}
	
	private void updateProfileinDB(UserDetails user) {
		Credentials cred = credentialsRepository.getById(user.getUserName());
		cred.setPassword(EncodePasswordService.encode(user.getPassword()));
		cred.setInterFirm(user.getInterFirm());
		cred.setFullTmOffer(user.getFullTmOffer());
		cred.setCaseName(user.getCaseName());
		cred.setOfficeLocation(user.getOfficeLoc());
		cred.setNumberOfCases(user.getNumberOfCases());
		credentialsRepository.save(cred);
	}	
}
