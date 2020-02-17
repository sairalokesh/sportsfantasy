package com.sports.fantasy.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.emailservice.EmailService;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.util.PasswordUtil;

@Controller
public class PasswordController {
	
	@Autowired private UserService userService;
	@Autowired private EmailService emailService;
	

	@GetMapping(value = "/resetpassword")
	public String resetpassword(Model model) {
		model.addAttribute("user", new UserInfo());
		return "view/prelogin/resetpassword";
	}
	
	@PostMapping(value = "/resetpassword")
	public String submitresetpassword(@ModelAttribute UserInfo user, Model model) {
		UserInfo dbUser = userService.findByEmail(user.getEmail());
		if (dbUser != null) {
			emailService.sendForgotPassword(dbUser);
		}
		
		return "view/prelogin/resetpassword";
	}

	@GetMapping(value = "/resetpassword/{email:.+}/{userId}")
	public String resetpassword(@PathVariable String email, @PathVariable Long userId, Model model) {
		model.addAttribute("user", new UserInfo(userId, email));
		return "view/prelogin/userresetpassword";
	}

	@PostMapping(value = "/changepassword")
	public String changepassword(@ModelAttribute UserInfo user, Model model, RedirectAttributes attributes) {
		try {
			UserInfo dbUser = userService.findByEmail(user.getEmail());
			if (dbUser != null && user.getId() == dbUser.getId()) {
				if (StringUtils.hasText(user.getPassword())) {
					dbUser.setPassword(PasswordUtil.getEncodedPassword(user.getPassword()));
				}
				dbUser.setUpdatedDate(new Date());
				userService.update(dbUser);
				attributes.addFlashAttribute("successmessage", "Password is successfully changed! Login with new password");
				return "redirect:/signin";
			} else {
				attributes.addFlashAttribute("errormessage", "Password is not change! Please check once again");
				return "redirect:/resetpassword/" + user.getEmail() + "/" + user.getId();
			}
		} catch (Exception e) {
			attributes.addFlashAttribute("errormessage", "Password is not change! Please check once again");
			return "redirect:/resetpassword/" + user.getEmail() + "/" + user.getId();
		}
	}

}
