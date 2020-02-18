package com.sports.fantasy.apicontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sports.fantasy.apiservice.TeamApiService;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class TeamParticipantsApiController {

  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private TeamApiService teamApiService;
  
  @ModelAttribute
  public void adminparticipantstitle(Model model) {
      model.addAttribute("title", "adminpp");
  }

  @GetMapping(value = "/api/questionparticipants")
  public String points(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
    model.addAttribute("gameQuestions", gameQuestions);
    return "view/admin/apiquestionparticipants";
  }

  @GetMapping(value = "/api/questionparticipants/{questionId}")
  public String questionparticipants(@PathVariable Long questionId, Principal principal,
      Model model) throws UnirestException {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    teamApiService.getAllApiParticipantsByQuestionId(questionId);
    return "redirect:/admin/questionparticipants/"+questionId;
  }



}
