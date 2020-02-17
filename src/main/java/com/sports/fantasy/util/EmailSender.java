package com.sports.fantasy.util;

import java.io.File;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.from}")
	private String from;

	@Autowired
	private ServletContext context;

	public Boolean sendResetPassword(String to, String subject, String text) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(text, true);

			FileSystemResource phone = new FileSystemResource(new File(context.getRealPath("/imageicons/phone.png")));
			helper.addInline("phone", phone);
			FileSystemResource facebook = new FileSystemResource(
					new File(context.getRealPath("/imageicons/facebook.png")));
			helper.addInline("facebook", facebook);
			FileSystemResource link = new FileSystemResource(new File(context.getRealPath("/imageicons/link.png")));
			helper.addInline("link", link);
			javaMailSender.send(mail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
