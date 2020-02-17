package com.sports.fantasy.securityservice;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {

	void autoLogin(String email, String password, String role, HttpServletRequest request);

}
