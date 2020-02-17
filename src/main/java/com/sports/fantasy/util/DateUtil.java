package com.sports.fantasy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formatedDate = format.format(date);
		return formatedDate;
		
	}
	
	public static Date stringToDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			Date formatedDate = format.parse(date);
			return formatedDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
