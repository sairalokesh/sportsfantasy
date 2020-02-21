package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UserTempParticipants;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.userservice.UserTempParticipantService;
import com.sports.fantasy.util.DataMapper;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user")
public class UserTeamsEditController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  @Autowired
  private GameAmountService gameAmountService;
  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private GameParticipantService gameParticipantsService;
  @Autowired
  private UserTempParticipantService userTempParticipantService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "usereditgamepages");
  }


  @GetMapping(value = "/game/{gameType}/edit")
  public String gameedit(@PathVariable String gameType, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    List<SelectedTeam> selectedTeams =
        userSelectedTeamService.getTeamSelectedEditParticipants(user.getId(), gameType, true);
    model.addAttribute("selectedTeams", selectedTeams);
    model.addAttribute("gameType", gameType);
    return "view/user/editgameteams";
  }
  
  @GetMapping(value="/game/{gameType}/teamparticipants/{questionId}/{amountId}/{index}")
  public String teamparticipants(@PathVariable String gameType, @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Integer index, Model model, Principal principal) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
      if(gameQuestion.getValidDate().before(new Date())) {
          return "redirect:/user/game/"+gameType+"/entry";
      }
      UserInfo user = userService.findByEmail(principal.getName());
      AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
      List<UserTeamParticipants> userSelectedTeams = userSelectedTeamService.getTeamsEditParticipants(questionId, amountId, user.getId());
      model.addAttribute("userSelectedTeams", userSelectedTeams);
      model.addAttribute("amountEntry", amountEntry);
      model.addAttribute("gameType", gameType);
      model.addAttribute("gameQuestion", gameQuestion);
      model.addAttribute("index", index);
      return "view/user/editgameparticipantteams";
  }
  
  @GetMapping(value="/game/{gameType}/editparticipant/{teamId}/{index}")
  public String seelctparticipants(@PathVariable String gameType, @PathVariable Long teamId, @PathVariable Integer index, Model model, Principal principal, HttpSession session) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      UserInfo user = userService.findByEmail(principal.getName());
      UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(teamId, user.getId());
      if(userSelectedTeam!=null) {
          GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(userSelectedTeam.getGameQuestions().getId(), gameType);
          if(gameQuestion.getValidDate().before(new Date())) {
              return "redirect:/user/game/"+gameType+"/entry";
          }
          AmountEntries amountEntry = gameAmountService.findByAmountId(userSelectedTeam.getAmountEntries().getId());
          SelectedMembers selectedMembers = DataMapper.getSelectedMembers(userSelectedTeam);
          selectedMembers.setIndex(index);
          Map<String, List<GameParticipants>> gameParticipants = gameParticipantsService.getAllActiveParticipantsByQuestionId(userSelectedTeam.getGameQuestions().getId());
          Map<String, Integer> gametypecounts = DataMapper.getGameTypeCounts(gameParticipants, selectedMembers);
          if(gametypecounts!=null && gametypecounts.size()>0) {
              model.addAttribute("gametypecounts", gametypecounts);
              model.addAttribute("gamecount", gametypecounts.size());
          }
          
          Map<String, Integer> gameplayercounts = DataMapper.getGamePlayerCounts(gameParticipants, selectedMembers);
          model.addAttribute("typecount", gameplayercounts);
          model.addAttribute("gameQuestion", gameQuestion);
          model.addAttribute("gameParticipants", gameParticipants);
          model.addAttribute("selectedMembers", selectedMembers);
          model.addAttribute("gameType", gameType);
          model.addAttribute("index", index);
          model.addAttribute("questionId", userSelectedTeam.getGameQuestions().getId());
          model.addAttribute("amountId", userSelectedTeam.getAmountEntries().getId());
          model.addAttribute("amountEntry", amountEntry);
      }
      
      return "view/user/editgameParticipants";
  }
  
  @PostMapping(value="/editcapitanselection")
  public String editcapitanselection(@ModelAttribute SelectedMembers selectedMembers, Model model, Principal principal) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      UserInfo user = userService.findByEmail(principal.getName());
      UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(selectedMembers.getUserTeamId(), user.getId());
      if(userSelectedTeam!=null && userSelectedTeam.getId()!=null) {
          UserTempParticipants dbUserTempParticipants = userTempParticipantService.editsave(selectedMembers, user);
          if(dbUserTempParticipants!=null && dbUserTempParticipants.getId()!=null) {
              return "redirect:/user/edit/game/"+selectedMembers.getGameType()+"/amount/"+dbUserTempParticipants.getQuestionId()+"/participants/"+dbUserTempParticipants.getAmountId()+"/selectedparticipantroles/"+dbUserTempParticipants.getId()+"/"+selectedMembers.getIndex();
          } else  {
              return "redirect:/user/game/"+selectedMembers.getGameType()+"/editparticipant/"+selectedMembers.getUserTeamId()+"/"+selectedMembers.getIndex();
          }
      }
      
      return "redirect:/user/game/"+selectedMembers.getGameType()+"/edit";
  }
  
  @GetMapping(value="/edit/game/{gameType}/amount/{questionId}/participants/{amountId}/selectedparticipantroles/{userTempId}/{index}")
  public String selectededitparticipantroles(@PathVariable String gameType, @PathVariable Long questionId,@PathVariable Long amountId, @PathVariable Long userTempId, @PathVariable Integer index, Model model, Principal principal) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
      if(gameQuestion.getValidDate().before(new Date())) {
          return "redirect:/user/game/"+gameType+"/entry";
      }
      AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
      
      UserInfo user = userService.findByEmail(principal.getName());
      Map<String, List<GameParticipants>> gameParticipants = userTempParticipantService.getAllTempParticipantsByQuestionId(userTempId, questionId, amountId, gameType, user);
      SelectedMembers selectedMembers = userTempParticipantService.findById(userTempId, questionId, amountId, gameType, user);
      selectedMembers.setIndex(index);
      UserTempParticipants dbTempParticipants = userTempParticipantService.findCurrentParticipantById(userTempId, questionId, amountId, gameType, user);
      model.addAttribute("gameQuestion", gameQuestion);
      model.addAttribute("gameParticipants", gameParticipants);
      model.addAttribute("selectedMembers", selectedMembers);
      model.addAttribute("gameType", gameType);
      model.addAttribute("index", index);
      model.addAttribute("userTempId", userTempId); // temp table id
      model.addAttribute("userTeamId", dbTempParticipants.getUserTeamId()); // user selected team id
      model.addAttribute("questionId", questionId);
      model.addAttribute("amountId", amountId);
      model.addAttribute("amountEntry", amountEntry);
      return "view/user/editselectedparticipantroles";
  }
  
  @PostMapping(value="/updateparticpants")
  public String updateparticpants(@ModelAttribute SelectedMembers selectedMembers,Model model, Principal principal, RedirectAttributes attributes) {
      UserInfo user = userService.findByEmail(principal.getName());
      UserTempParticipants userTempParticipants = userTempParticipantService.findCurrentParticipantById(selectedMembers.getUserTempId(), selectedMembers.getQuestionId(), selectedMembers.getAmountId(), selectedMembers.getGameType(), user);
      if(userTempParticipants!=null && userTempParticipants.getUserTeamId()!=null) {
        UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(userTempParticipants.getUserTeamId(), user.getId());
        if(userSelectedTeam !=null) {
          userTempParticipants.setCapitanId(selectedMembers.getCapitanId());
          userTempParticipants.setViceCapitanId(selectedMembers.getViceCapitanId());
          userTempParticipants.setSuppoterId(selectedMembers.getSuppoterId());
          UserSelectedTeam selectedTeam = userSelectedTeamService.getUserSelectTeam(userTempParticipants);
          selectedTeam.setId(userTempParticipants.getUserTeamId());
          UserSelectedTeam dbUserSelectedTeam = userSelectedTeamService.save(selectedTeam);
          if(dbUserSelectedTeam != null) {
            deleteTempUserSelectionParticipants(userTempParticipants);
            attributes.addFlashAttribute("successmessage", "Team Updated Successflly!");
            return "redirect:/user/game/"+selectedMembers.getGameType()+"/edit";
          }
        }
      }
      return "redirect:/user/game/"+selectedMembers.getGameType()+"/edit";
  }
  
  @GetMapping(value="/game/{gameType}/amount/{questionId}/editselectedparticipants/{amountId}/{userTeamId}/{userTempId}/{index}")
  public String seelctparticipants(@PathVariable String gameType, @PathVariable Long questionId,@PathVariable Long amountId,
          @PathVariable Long userTeamId, @PathVariable Long userTempId, @PathVariable Integer index, Model model, Principal principal, HttpSession session) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      
      GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
      if(gameQuestion.getValidDate().before(new Date())) {
          return "redirect:/user/game/"+gameType+"/edit";
      }
      
      UserInfo user = userService.findByEmail(principal.getName());
      UserTempParticipants dbTempParticipants = userTempParticipantService.findCurrentParticipantById(userTempId, questionId, amountId, gameType, user);
      if(dbTempParticipants!=null && StringUtils.hasText(dbTempParticipants.getParticipants())) {
          SelectedMembers selectedMembers = DataMapper.getRedirectEditTeamSelection(dbTempParticipants, user);
          selectedMembers.setIndex(index);
          AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
          Map<String, List<GameParticipants>> gameParticipants = gameParticipantsService.getAllActiveParticipantsByQuestionId(questionId);
          Map<String, Integer> gametypecounts = DataMapper.getGameTypeCounts(gameParticipants, selectedMembers);
          if(gametypecounts!=null && gametypecounts.size()>0) {
              model.addAttribute("gametypecounts", gametypecounts);
              model.addAttribute("gamecount", gametypecounts.size());
          }
          
          Map<String, Integer> gameplayercounts = DataMapper.getGamePlayerCounts(gameParticipants, selectedMembers);
          model.addAttribute("typecount", gameplayercounts);
          model.addAttribute("gameQuestion", gameQuestion);
          model.addAttribute("amountEntry", amountEntry);
          model.addAttribute("gameParticipants", gameParticipants);
          model.addAttribute("selectedMembers", selectedMembers);
          model.addAttribute("userTeamId", userTeamId);
          model.addAttribute("gameType", gameType);
          model.addAttribute("questionId", questionId);
          model.addAttribute("index", index);
          return "view/user/editgameParticipants";
      } else {
          return "redirect:/user/game/"+gameType+"/edit";
      }
  }
  
  private void deleteTempUserSelectionParticipants(UserTempParticipants dbTempParticipants) {
    if (dbTempParticipants != null && dbTempParticipants.getId() != null) {
      userTempParticipantService.deleteTempUserSelectedParticipantsById(dbTempParticipants.getId());
    }
  } 
}
