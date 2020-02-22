package com.sports.fantasy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.sports.fantasy.model.UserInfo;

@Controller
public class LoginController {
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("activetitle", "signin");
  }

	@GetMapping(value = "/signin")
	public String signin(Model model) {
		model.addAttribute("user", new UserInfo());
		return "view/prelogin/signin";
	}

	@GetMapping(value = "/loginfailure")
	public String loginfailure(Model model) {
		model.addAttribute("user", new UserInfo());
		model.addAttribute("errormessage", "Invalid email or Password");
		return "view/prelogin/signin";
	}

	@GetMapping(value = "/accessdenied")
	public String accessdenied(Model model) {
		model.addAttribute("user", new UserInfo());
		model.addAttribute("errormessage", "Access Deined");
		return "view/prelogin/signin";
	}

	@GetMapping(value = "/logoutsuccess")
	public String logoutsuccess(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/signin";
	}

}
