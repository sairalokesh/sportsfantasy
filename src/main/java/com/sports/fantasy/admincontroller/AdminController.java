package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired private UserService userService;
	
	@ModelAttribute
	public void admindashboardtitle(Model model) {
		model.addAttribute("title", "admindashboard");
	}

	@GetMapping(value = "/dashboard")
	public String admindashboard(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}

		UserInfo dbUser = userService.findByEmail(principal.getName());
		model.addAttribute("user", dbUser);
		List<UserInfo> users = userService.findAllByOrderByCreatedDateDesc();
		model.addAttribute("users", users);
		return "view/admin/admindashboard";
	}
}
