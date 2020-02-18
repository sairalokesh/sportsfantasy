package com.sports.fantasy.apicontroller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sports.fantasy.apiservice.TeamApiService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class PlayersApiController {
  
  @Autowired
  private TeamApiService teamApiService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "adminplayers");
  }

  @GetMapping(value = "/getplayers")
  public String admindashboard(Principal principal, Model model)
      throws UnirestException {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    teamApiService.getApiPlayers();
    return "redirect:/admin/questions";
  }

}
