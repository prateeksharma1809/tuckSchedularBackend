package com.tuck.matches.service;

import java.util.Base64;

public class DecodePasswordService {
	
	public static String decode(String password) {
		byte[] decodedBytes = Base64.getDecoder().decode(password);
		String decodedString = new String(decodedBytes);
		return decodedString;
		
	}

}
