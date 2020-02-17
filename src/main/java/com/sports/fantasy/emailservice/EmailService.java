package com.sports.fantasy.emailservice;

import com.sports.fantasy.model.UserInfo;

public interface EmailService {
	public Boolean sendForgotPassword(UserInfo user);
}
