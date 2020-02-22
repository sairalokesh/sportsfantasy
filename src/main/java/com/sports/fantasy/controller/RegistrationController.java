package com.sports.fantasy.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.SecurityService;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserCouponService;

@Controller
public class RegistrationController {
	
	@Autowired private UserService userService;
	@Autowired private SecurityService securityService;
	@Autowired private UserCouponService userCouponService;
	@Autowired private UserAmountService userAmountService;
	
	@ModelAttribute
    public void admindashboardtitle(Model model) {
	  model.addAttribute("activetitle", "signup");
    }
	
	@GetMapping(value = "/signup")
	public String signup(Model model) {
		model.addAttribute("user", new UserInfo());
		return "view/prelogin/signup";
	}
	
	@PostMapping(value="/register")
	public String registration(@ModelAttribute UserInfo userInfo, HttpServletRequest request, Model model, RedirectAttributes attributes) {
		UserInfo user = userService.findByEmail(userInfo.getEmail());
		if(user!=null && user.getEmail().equalsIgnoreCase(userInfo.getEmail())) {
			attributes.addFlashAttribute("userinfo", userInfo);
			attributes.addFlashAttribute("errormessage", "Email is Already exist.");
			return "redirect:/userexist";
		}
		
		UserInfo usermobile = userService.findByPhoneNumber(userInfo.getPhoneNumber());
		if(usermobile!=null && usermobile.getPhoneNumber().equalsIgnoreCase(userInfo.getPhoneNumber())) {
			attributes.addFlashAttribute("userinfo", userInfo);
			attributes.addFlashAttribute("errormessage", "Phone Number is Already exist.");
			return "redirect:/userexist";
		}
		
		UserInfo cuponUser = new UserInfo();
		if(userInfo!=null && StringUtils.hasText(userInfo.getCuponCode())) {
			cuponUser = userService.getUserByCuponCode(userInfo.getCuponCode());
			if(cuponUser==null || cuponUser.getId() == null) {
				attributes.addFlashAttribute("userinfo", userInfo);
				attributes.addFlashAttribute("errormessage", "InValid Cuponcode");
				return "redirect:/userexist";
			}
		}
		String password = userInfo.getPassword();
		UserInfo dbUser = userService.save(userInfo);
		if(dbUser!=null && dbUser.getId()!=null) {
			userAmountService.saveUserAmount(dbUser.getId());
		}
		if(cuponUser!=null && cuponUser.getId()!=null && dbUser!=null && dbUser.getId()!=null) {
			userCouponService.saveCuponCodeUser(cuponUser.getId(), dbUser.getId());
		}
		
		securityService.autoLogin(dbUser.getEmail(), password, dbUser.getRole(), request);
		model.addAttribute("user", dbUser);
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = grantedAuthorities.iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println(name);
		return "redirect:/redirectdashboard";
	}
	
	@GetMapping(value="/userexist")
	public String userexist(@ModelAttribute("userinfo") UserInfo userinfo, Model model) {
		model.addAttribute("user", userinfo);
		return "view/prelogin/signup";
	}
}
