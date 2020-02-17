package com.sports.fantasy.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;

@Controller
public class RedirectDashboardController {
	@Autowired private UserService userService;
	
	@GetMapping(value="/redirectdashboard")
	public String redirectdashboard(Principal principal) {
		String redirecturl = "";
		UserInfo dbUser = userService.findByEmail(principal.getName());
		if(dbUser!=null && StringUtils.hasText(dbUser.getRole())) {
			if(dbUser.getRole().equalsIgnoreCase("ADMIN")) {
				redirecturl = "redirect:/admin/dashboard";
			} else if(dbUser.getRole().equalsIgnoreCase("USER")) {
				dbUser.setLoginDate(new Date());
				userService.update(dbUser);
				redirecturl = "redirect:/user/dashboard";
			}
		}
		return redirecturl;
	}
}
