package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminProfileController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/profile")
	public String adminprofile(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		UserInfo dbUser = userService.findByEmail(principal.getName());
		model.addAttribute("user", dbUser);
		model.addAttribute("changeuser", new UserInfo(dbUser.getId(), dbUser.getEmail()));
		return "view/admin/viewprofile";
	}

	@GetMapping(value = "/editprofile")
	public String editprofile(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		UserInfo dbUser = userService.findByEmail(principal.getName());
		if (dbUser != null) {
			userService.datetostringformat(dbUser);
		}
		model.addAttribute("user", dbUser);
		return "view/admin/editprofile";
	}

	@PostMapping(value = "/updateprofile")
	public String updateprofile(@ModelAttribute UserInfo user, Principal principal, Model model,
			RedirectAttributes attributes) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}

		UserInfo dbcUser = userService.findByEmail(principal.getName());
		if (dbcUser != null && !dbcUser.getEmail().equalsIgnoreCase(user.getEmail())) {
			attributes.addFlashAttribute("errormessage", "Email is Not Matched.");
			return "redirect:/admin/editprofile";
		}

		UserInfo usermobile = userService.findByPhoneNumber(user.getPhoneNumber());
		if (usermobile != null && user.getId() != usermobile.getId()) {
			attributes.addFlashAttribute("errormessage", "Phone Number is Already exist.");
			return "redirect:/admin/editprofile";
		}
		
		userService.stringtodateformat(user);
		user.setUpdatedDate(new Date());
		UserInfo dbUser = userService.update(user);
		if (dbUser != null) {
			attributes.addFlashAttribute("successmessage", "Profile Updated successfully!");
			return "redirect:/admin/editprofile";
		}
		return "view/admin/editprofile";
	}

}
