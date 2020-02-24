package com.sports.fantasy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HowtoPlayController {
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("activetitle", "howtoplay");
  }
  
  @GetMapping(value = "/howtoplay")
  public String enter(Model model) {
    return "view/prelogin/howtoplay";
  }

}
