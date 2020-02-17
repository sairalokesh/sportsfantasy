package com.sports.fantasy.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.util.EmailSender;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired private SpringTemplateEngine templateEngine;
	@Autowired private EmailSender emailSender;
	private static final String USER_FPRGOT_PASSWORD = "email/resetPassword";
	
	@Value("${com.website.url}")
	private String websiteurl;

	@Override
	public Boolean sendForgotPassword(UserInfo user) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("resetpasswordurl", websiteurl+"resetpassword/"+user.getEmail()+"/"+user.getId());
		String body = templateEngine.process(USER_FPRGOT_PASSWORD, context);
        return emailSender.sendResetPassword(user.getEmail(), "Reset Password - Sports Fantasy", body);
	}

}
