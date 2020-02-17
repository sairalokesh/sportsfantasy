package com.sports.fantasy.util;

import java.security.Principal;

import org.springframework.util.StringUtils;

public class LoginUtil {

	public static boolean getAuthentication(Principal principal) {
		if (principal != null && StringUtils.hasText(principal.getName())) {
			return true;
		} else {
			return false;
		}

	}

}
