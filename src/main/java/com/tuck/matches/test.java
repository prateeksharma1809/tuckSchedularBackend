package com.tuck.matches;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

	public static void main(String[] args) throws ParseException {
		String pattern = "yyyy-MM-dd HH:mm";
		Date fromDate=new SimpleDateFormat(pattern).parse("2022-05-16 23:15");
		Date toDate=new SimpleDateFormat(pattern).parse("2022-05-17 14:15");
		Date fromCheck = new  SimpleDateFormat(pattern).parse("2022-05-16 23:14");
		Date toCheck = new  SimpleDateFormat(pattern).parse("2022-05-17 14:16");
		System.out.println(fromDate.compareTo(fromCheck));
		System.out.println(toDate.compareTo(toCheck));

	}

}
