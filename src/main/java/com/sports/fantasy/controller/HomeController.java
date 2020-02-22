package com.sports.fantasy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameQuestionsService;

@Controller
public class HomeController {

  @Autowired
  private GameQuestionsService gameQuestionsService;

  @GetMapping(value = "/")
  public String enter(Model model) {
    return "redirect:/home";
  }

  @GetMapping(value = "/home")
  public String home(Model model) {
    List<GameQuestions> gameQuestions = gameQuestionsService.getGameQuestionsByGreaterthanCurrentDatewithLimit("cricket");
    if (gameQuestions != null && !gameQuestions.isEmpty()) {
      model.addAttribute("gameQuestion", gameQuestions.get(0));
    }
    model.addAttribute("gameQuestions", gameQuestions);
    return "view/prelogin/home";
  }
}
