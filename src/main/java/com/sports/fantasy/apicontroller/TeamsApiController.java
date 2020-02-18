package com.sports.fantasy.apicontroller;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sports.fantasy.api.ApiTeams;
import com.sports.fantasy.apiservice.TeamApiService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class TeamsApiController {

  @Autowired
  private TeamApiService teamApiService;
  @Autowired
  private GameQuestionsService gameQuestionsService;

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "adminteams");
  }

  @GetMapping(value = "/getteams")
  public String admindashboard(Principal principal, Model model)
      throws UnirestException, ParseException {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    List<ApiTeams> teams = teamApiService.getApiTeams();
    if (teams != null && !teams.isEmpty()) {
      gameQuestionsService.saveApiTeams(teams);

    }
    return "redirect:/admin/questions";
  }
}
