package com.sports.fantasy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping(value = "/")
	public String enter(Model model) {
		return "redirect:/signup";
	}

	@GetMapping(value = "/home")
	public String home(Model model) {
		return "view/index";
	}
}
