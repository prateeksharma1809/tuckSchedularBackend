package com.tuck.matches.service;

import java.util.Base64;

public class EncodePasswordService {
	
	public static String encode(String password) {
		return Base64.getEncoder().encodeToString(password.getBytes());
		
	}

}
