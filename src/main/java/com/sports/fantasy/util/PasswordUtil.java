package com.sports.fantasy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

	static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

}
