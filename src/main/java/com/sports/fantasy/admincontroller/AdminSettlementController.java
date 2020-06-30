package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sports.fantasy.adminservice.MoneyService;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameMoneyRange;
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
@RequestMapping(value = "/admin/settlement")
public class AdminSettlementController {
  
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
  
  @Autowired
  private MoneyService moneyService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
      model.addAttribute("title", "adminsettlement");
  }
  
  @GetMapping(value = "/{gametype}")
  public String settlement(@PathVariable String gametype, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }
    
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSettlements(gametype);
    model.addAttribute("selectedTeams", selectedTeams);
    model.addAttribute("gameType", gametype);
    return "view/admin/settlement";
  }
  
  @GetMapping("/game/{gametype}/amount/{questionId}/participants/{amountId}")
  public String getParticipantScores(@PathVariable String gametype, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user, true);
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gametype);
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    model.addAttribute("participantScores", rankings);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("gametype", gametype);
    model.addAttribute("questionId", questionId);
    model.addAttribute("amountId", amountId);
    return "view/admin/rankingscores";
  }
  
  @GetMapping("/game/final/{gametype}/amount/{questionId}/participants/{amountId}")
  public String getFinalParticipantScores(@PathVariable String gametype, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user, false);
    Long usersCount = userSelectedTeamService.getUsersCount(gametype, questionId, amountId);
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    List<GameMoneyRange> gameMoneyRanges = moneyService.getGameMoneyRange(Double.parseDouble(amountEntry.getAmount()), Integer.parseInt(usersCount.toString()));
    if(gameMoneyRanges!=null && gameMoneyRanges.size()>0) {
      Map<Integer, Double> map = new TreeMap<Integer, Double>();
      for(GameMoneyRange questionAmount : gameMoneyRanges) {
          if(questionAmount!=null && questionAmount.getPersonFrom() < questionAmount.getPersonTo()) {
            for(int index = questionAmount.getPersonFrom(); index <= questionAmount.getPersonTo(); index++) {
              map.put(index, questionAmount.getAmount());
            }
          } else {
            map.put(questionAmount.getPersonFrom(), questionAmount.getAmount());
          }
      }
      
      List<Entry<Integer,Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
      Integer lastEntry = entryList.get(entryList.size()-1).getKey();
      
      List<Ranking> filteredRankings = rankings.stream().filter(rank -> rank.getRank() <= lastEntry).collect(Collectors.toList());
      
      if(filteredRankings!=null && filteredRankings.size() >= map.size()) {
        for(int i = (map.size()+1);i<=filteredRankings.size(); i++) {
            // map.put(i, map.get(map.size() - 1));
               map.put(i, 0.0);
        }
        
        Double amount = 0.00;
        List<Ranking> winningResults = new ArrayList<Ranking>();
        Integer count = 0;
        
        for (int i = 0; i <filteredRankings.size(); i++) {
            if(i<(filteredRankings.size()-1)) {
              
                if (filteredRankings.get(i).getRank() == filteredRankings.get(i + 1).getRank()) {
                    amount = amount + map.get(i+1);
                    winningResults.add(filteredRankings.get(i));
                    count = count + 1;
                } else {
                    amount = amount + map.get(i+1);
                    count = count + 1;
                    Double spiltAmount = amount/count;
                    Integer finalAmount = spiltAmount.intValue();
                    winningResults.add(filteredRankings.get(i));
                    updateParticipantData(winningResults, questionId, amountId, gametype, finalAmount);
                    amount = 0.00;
                    winningResults.clear();
                    count = 0;
                    
                }
                
            } else if(i == (filteredRankings.size()-1)) {
                amount = amount + map.get(i+1);
                count = count + 1;
                Double spiltAmount = amount/count;
                Integer finalAmount = spiltAmount.intValue();;
                winningResults.add(filteredRankings.get(i));
                updateParticipantData(winningResults, questionId, amountId, gametype, finalAmount);
                amount = 0.00;
                winningResults.clear();
                count = 0;
                
            }   
        }   
    }  
    }
 
    gameQuestionsService.updateGameQuestionStatus(questionId, gametype);
    return "redirect:/admin/settlement/" + gametype;
  }
  
  private void updateParticipantData(List<Ranking> winningResults, Long questionId, Long amountId, String gametype, Integer spiltAmount) {
    if(winningResults != null && winningResults.size() >0) {
      for(Ranking ranking : winningResults) {
        userSelectedTeamService.updateUserSelectedTeam(ranking, questionId, amountId, gametype, spiltAmount);
      }
    }
  }

  @GetMapping(value="/game/selecteduserparticipantsscore/{teamId}/{userId}/{questionId}/{amountId}")
  public String getuserpointsinfo(@PathVariable Long teamId, @PathVariable Long userId, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      boolean isActive = true; 
      UserInfo user = userService.findByEmail(principal.getName());
      if (user!=null && !user.getRole().equalsIgnoreCase("ADMIN")) {
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

}
