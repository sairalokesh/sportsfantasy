package com.sports.fantasy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MobileAppController {
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("activetitle", "mobileapp");
  }
  
  @GetMapping(value = "/mobileapp")
  public String mobileapp(Model model) {
    return "view/prelogin/mobileapp";
  }

}
