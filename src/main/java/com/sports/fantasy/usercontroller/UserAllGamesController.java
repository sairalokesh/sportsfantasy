package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserPoints;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserRankingService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user/history")
public class UserAllGamesController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  @Autowired
  private UserRankingService userRankingService;
  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private GameAmountService gameAmountService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "usergameshistory");
  }

  @GetMapping(value = "/game/{gametype}/upcoming")
  public String upcoming(@PathVariable String gametype, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedEditParticipants(user.getId(), gametype, true);
    model.addAttribute("selectedTeams", selectedTeams);
    model.addAttribute("gameType", gametype);
    return "view/user/upcominggames";
  }

  @GetMapping(value = "/game/{gametype}/live")
  public String historygameentry(@PathVariable String gametype, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedLiveParticipants(user.getId(), gametype, true);
    model.addAttribute("selectedTeams", selectedTeams);
    model.addAttribute("gameType", gametype);
    return "view/user/livegames";
  }

  @GetMapping(value = "/game/{gametype}/completed")
  public String completed(@PathVariable String gametype, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedCompletedParticipants(user.getId(), gametype, false);
    model.addAttribute("selectedTeams", selectedTeams);
    model.addAttribute("gameType", gametype);
    return "view/user/completedgames";
  }

  @GetMapping("/game/{gametype}/amount/{questionId}/participants/{amountId}")
  public String getParticipantScores(@PathVariable String gametype, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user);
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gametype);
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    model.addAttribute("participantScores", rankings);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("gametype", gametype);
    model.addAttribute("questionId", questionId);
    model.addAttribute("amountId", amountId);
    return "view/user/userrankingscores";
  }
  
  @GetMapping("/game/{gametype}/amount/{questionId}/completedparticipants/{amountId}")
  public String completedparticipants(@PathVariable String gametype, @PathVariable Long questionId,
      @PathVariable Long amountId, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user);
    GameQuestions gameQuestion = gameQuestionsService.getCompletedGameQuestionByQuestionId(questionId, gametype);
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    model.addAttribute("participantScores", rankings);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("gametype", gametype);
    model.addAttribute("questionId", questionId);
    model.addAttribute("amountId", amountId);
    return "view/user/userrankingscores";
  }
  
  @GetMapping(value="/game/selecteduserparticipantsscore/{teamId}/{userId}/{questionId}/{amountId}")
  public String getuserpointsinfo(@PathVariable Long teamId, @PathVariable Long userId, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      boolean isActive = true; 
      UserInfo user = userService.findByEmail(principal.getName());
      if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
        return "redirect:/accessdenied";
      }
      if(user!=null && user.getId()!=null && user.getId() != userId) {
        GameQuestions gameQuestion = gameQuestionsService.findGameQuestionById(questionId);
        if(gameQuestion!=null && gameQuestion.getValidDate()!=null && gameQuestion.getValidDate().after(new Date())) {
          isActive = false;
        }
      }
      
      if(isActive) {
        UserSelectedTeam userSelectedTeam = userSelectedTeamService.getSelectedUserTeam(teamId, questionId, amountId, userId);
        if(userSelectedTeam != null) {
          Map<String, List<GameParticipantScore>> teamParticipants = userSelectedTeamService.getAllParticipantsScores(userSelectedTeam, questionId);
          model.addAttribute("teamParticipants", teamParticipants);
        }
        model.addAttribute("teamId", teamId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("amountId", amountId);
        model.addAttribute("userId", userId);
      }
      model.addAttribute("isActive", isActive);
      return "view/user/userselectedparticipantpoints";
  }
  
  @GetMapping(value="/game/selectedparticipantscoreinfo/{participantId}/{questionId}/{teamId}/{amountId}/{userId}")
  public String getparticipantscoreinfo(@PathVariable Long participantId, @PathVariable Long questionId, @PathVariable Long teamId, @PathVariable Long amountId,@PathVariable Long userId, Principal principal, Model model) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      UserSelectedTeam userSelectedTeam = userSelectedTeamService.getSelectedUserTeam(teamId, questionId, amountId, userId);
      if(userSelectedTeam != null) {
        UserPoints userPoints = userSelectedTeamService.getSelectedParticipantScores(userSelectedTeam, participantId, questionId);
        model.addAttribute("userPoints", userPoints);
        model.addAttribute("userSelectedTeam", userSelectedTeam);
      }
      model.addAttribute("questionId", questionId);
      return "view/user/userparticipantscoreinfo";
  } 
}
