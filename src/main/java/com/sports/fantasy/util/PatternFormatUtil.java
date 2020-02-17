package com.sports.fantasy.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFormatUtil {
	
	public static String getPatternFormat(String name) {
		final Pattern hidePattern = Pattern.compile("(.{1,2})(.*)(.{4,4})");
		  Matcher m = hidePattern.matcher(name);
		  String formatedName = null;
		  if(m.find()) {
		    int l = m.group(2).length();
		    formatedName = m.group(1) + new String(new char[l]).replace("\0", "X") + m.group(3);
		  }
		  return formatedName;
	}
	
	public static String getFormattedAmount(Double amount) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String formatedAmount = df.format(amount);
		return formatedAmount;
	}

}
