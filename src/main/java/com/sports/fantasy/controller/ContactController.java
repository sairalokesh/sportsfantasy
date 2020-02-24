package com.sports.fantasy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ContactController {
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("activetitle", "contact");
  }
  
  @GetMapping(value = "/contact")
  public String enter(Model model) {
    return "view/prelogin/contact";
  }


}
