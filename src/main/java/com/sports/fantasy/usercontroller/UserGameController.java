package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.domain.SelectedParticipants;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.CuponCodeUsers;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTempParticipants;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserCouponService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.userservice.UserTempParticipantService;
import com.sports.fantasy.util.DataMapper;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user")
public class UserGameController {

  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private GameAmountService gameAmountService;
  @Autowired
  private GameParticipantService gameParticipantsService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired
  private UserTempParticipantService userTempParticipantService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  @Autowired
  private UserCouponService userCouponService;

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "usergameentrypages");
  }


  @GetMapping(value = "/game/{gameType}/entry")
  public String gameentry(@PathVariable String gameType, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    List<GameQuestions> gameQuestions =
        gameQuestionsService.getGameQuestionsByGreaterthanCurrentDate(gameType);
    model.addAttribute("gameQuestions", gameQuestions);
    model.addAttribute("gameType", gameType);
    return "view/user/gameentry";
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}")
  public String selectamount(@PathVariable String gameType, @PathVariable Long questionId,
      Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo userInfo = userService.findByEmail(principal.getName());
    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + gameType + "/entry";
    }
    List<AmountEntries> amountEntries = gameAmountService.findAllActiveAmountEntries();
    model.addAttribute("amountEntries", amountEntries);
    model.addAttribute("questionId", questionId);
    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("user", userInfo);
    model.addAttribute("gameType", gameType);
    return "view/user/selectamount";
  }

  @GetMapping(value = "/checkuseramount/{amountId}")
  public @ResponseBody boolean checkuseramount(@PathVariable Long amountId, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return true;
    }

    UserInfo user = userService.findByEmail(principal.getName());
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    UserAmount amount = userAmountService.getUserAmount(user.getId());
    if (amount != null && amountEntry != null) {
      double bonusAmount = 0.00;
      if (amount.getBonusAmount() >= 10) {
        bonusAmount = 10.00;
      }
      double cashamount = Double.parseDouble(amountEntry.getAmount()) - bonusAmount;
      if (amount.getAddedAmount() >= cashamount) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}/participants/{amountId}")
  public String seelctparticipants(@PathVariable String gameType, @PathVariable Long questionId,
      @PathVariable Long amountId, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + gameType + "/entry";
    }
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    Map<String, List<GameParticipants>> gameParticipants =
        gameParticipantsService.getAllActiveParticipantsByQuestionId(questionId);
    List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId); // Means
                                                                                                     // Names
    if (countries != null && !countries.isEmpty()) {
      Map<String, Integer> gamecountries = new LinkedHashMap<>();
      for (String country : countries) {
        gamecountries.put(country, 0);
      }
      model.addAttribute("gametypecounts", gamecountries);
      model.addAttribute("gamecount", gamecountries.size());
    }

    if (gameParticipants != null && gameParticipants.size() > 0) {
      Set<String> types = gameParticipants.keySet();
      if (types != null && !types.isEmpty()) {
        Map<String, Integer> typecount = new LinkedHashMap<>(); // Means Wk, BOWL, BATS, AR
        for (String type : types) {
          typecount.put(type, 0);
        }
        model.addAttribute("typecount", typecount);
      }
    }

    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("gameParticipants", gameParticipants);
    model.addAttribute("selectedMembers", new SelectedMembers(questionId, amountId, gameType));
    model.addAttribute("errormessage", null);
    model.addAttribute("gameType", gameType);
    model.addAttribute("questionId", questionId);
    return "view/user/gameParticipants";
  }

  @PostMapping(value = "/sendcapitanselection")
  public String sendcapitanselection(@ModelAttribute SelectedMembers selectedMembers, Model model,
      Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(
        selectedMembers.getQuestionId(), selectedMembers.getGameType());
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + selectedMembers.getGameType() + "/entry";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants dbUserTempParticipants =
        userTempParticipantService.save(selectedMembers, user);
    if (dbUserTempParticipants != null && dbUserTempParticipants.getId() != null) {
      return "redirect:/user/game/" + selectedMembers.getGameType() + "/amount/"
          + dbUserTempParticipants.getQuestionId() + "/participants/"
          + dbUserTempParticipants.getAmountId() + "/selectedparticipantroles/"
          + dbUserTempParticipants.getId();
    }

    return "redirect:/user/game/" + selectedMembers.getGameType() + "/entry";
  }

  @GetMapping(
      value = "/game/{gameType}/amount/{questionId}/participants/{amountId}/selectedparticipantroles/{tempId}")
  public String selectedparticipantroles(@PathVariable String gameType,
      @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Long tempId,
      Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + gameType + "/entry";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);

    Map<String, List<GameParticipants>> gameParticipants = userTempParticipantService
        .getAllTempParticipantsByQuestionId(tempId, questionId, amountId, gameType, user);
    SelectedMembers selectedMembers =
        userTempParticipantService.findById(tempId, questionId, amountId, gameType, user);

    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("gameParticipants", gameParticipants);
    model.addAttribute("selectedMembers", selectedMembers);
    model.addAttribute("gameType", gameType);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("questionId", questionId);
    model.addAttribute("amountId", amountId);
    model.addAttribute("tempId", tempId);
    return "view/user/selectedparticipantroles";
  }

  @PostMapping(value = "/userteamcreation")
  public String userpayment(@ModelAttribute SelectedMembers selectedMembers, Model model,
      HttpSession session, Principal principal) throws Exception {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(
        selectedMembers.getQuestionId(), selectedMembers.getGameType());
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + selectedMembers.getGameType() + "/entry";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants userTempParticipants =
        userTempParticipantService.findCurrentParticipantById(selectedMembers.getUserTempId(),
            selectedMembers.getQuestionId(), selectedMembers.getAmountId(),
            selectedMembers.getGameType(), user);
    if (userTempParticipants != null) {
      UserTempParticipants updatedUserTempParticipants =
          userTempParticipantService.update(selectedMembers, userTempParticipants);
      if (updatedUserTempParticipants != null && updatedUserTempParticipants.getId() != null) {
        return "redirect:/user/game/" + selectedMembers.getGameType() + "/amount/"
            + selectedMembers.getQuestionId() + "/participants/" + selectedMembers.getAmountId()
            + "/confirmpayment/" + selectedMembers.getUserTempId();
      }
    }
    return "redirect:/user/game/" + selectedMembers.getGameType() + "/entry";
  }

  @GetMapping(
      value = "/game/{gameType}/amount/{questionId}/participants/{amountId}/confirmpayment/{tempId}")
  public String confirmpayment(@PathVariable String gameType, @PathVariable Long questionId,
      @PathVariable Long amountId, @PathVariable Long tempId, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + gameType + "/entry";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    UserAmount amount = userAmountService.getUserAmount(user.getId());
    if (amount != null && amountEntry != null) {
      double bonusAmount = 0.00;
      if (amount.getBonusAmount() >= 10) {
        bonusAmount = 10.00;
      }
      double cashamount = Double.parseDouble(amountEntry.getAmount()) - bonusAmount;
      if (amount.getAddedAmount() >= cashamount) {
        model.addAttribute("bonusAmount", bonusAmount);
        model.addAttribute("cashAmount", cashamount);
      } else {
        return "redirect:/user/game/" + gameType + "/amount/" + questionId + "/participants/"
            + amountId + "/selectedparticipantroles/" + tempId;
      }
    } else {
      return "redirect:/user/game/" + gameType + "/amount/" + questionId + "/participants/"
          + amountId + "/selectedparticipantroles/" + tempId;
    }

    SelectedMembers selectedMembers =
        userTempParticipantService.findById(tempId, questionId, amountId, gameType, user);

    model.addAttribute("gameQuestion", gameQuestion);
    model.addAttribute("amountEntry", amountEntry);
    model.addAttribute("selectedMembers", selectedMembers);
    model.addAttribute("gameType", gameType);
    model.addAttribute("questionId", questionId);
    model.addAttribute("amountId", amountId);
    model.addAttribute("tempId", tempId);
    return "view/user/paymentconfirmation";
  }

  @PostMapping(value = "/userpaymentconfirmation")
  public String createfinalteam(@ModelAttribute SelectedMembers selectedMembers, Model model,
      HttpSession session, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants userTempParticipants =
        userTempParticipantService.findCurrentParticipantById(selectedMembers.getUserTempId(),
            selectedMembers.getQuestionId(), selectedMembers.getAmountId(),
            selectedMembers.getGameType(), user);

    if (userTempParticipants != null && user != null
        && user.getId() == userTempParticipants.getUserId()) {
      UserSelectedTeam selectedTeam =
          userSelectedTeamService.getUserSelectTeam(userTempParticipants);
      UserSelectedTeam dbUserSelectedTeam = userSelectedTeamService.save(selectedTeam);
      if (dbUserSelectedTeam != null && dbUserSelectedTeam.getId() != null) {
        UserAmount amount = userAmountService.getUserAmount(user.getId());
        double bonusAmount = 0.00;
        if (amount.getBonusAmount() >= 10) {
          bonusAmount = 10.00;
        }
        AmountEntries amountEntries =
            gameAmountService.findByAmountId(selectedMembers.getAmountId());
        double cashamount = Double.parseDouble(amountEntries.getAmount()) - bonusAmount;
        if (amount.getAddedAmount() >= cashamount) {
          double bonusRemainigAmount = amount.getBonusAmount() - bonusAmount;
          double addedRemainigAmount = amount.getAddedAmount() - cashamount;
          amount.setBonusAmount(bonusRemainigAmount);
          amount.setAddedAmount(addedRemainigAmount);
          UserAmount dbUserAmount = userAmountService.updateUserAmount(amount);
          if (dbUserAmount != null) {
            CuponCodeUsers codeUsers = userCouponService.getUserByUtilizerId(user.getId());
            if (codeUsers != null) {
              UserAmount userAmount =
                  userAmountService.getUserAmount(codeUsers.getCreatorUser().getId());
              if (userAmount != null) {
                double bonusAddedAmount = userAmount.getBonusAmount() + 10;
                userAmount.setBonusAmount(bonusAddedAmount);
                userAmountService.updateUserAmount(amount);
                codeUsers.setCodeused(true);
                userCouponService.updateCuponCode(codeUsers);
              }
            }
            deleteTempUserSelectionParticipants(userTempParticipants);
            return "redirect:/user/game/" + selectedMembers.getGameType() + "/edit";
          }
        }
      }
    }
    return "redirect:/user/game/" + selectedMembers.getGameType() + "/entry";
  }

  private void deleteTempUserSelectionParticipants(UserTempParticipants dbTempParticipants) {
    if (dbTempParticipants != null && dbTempParticipants.getId() != null) {
      userTempParticipantService.deleteTempUserSelectedParticipantsById(dbTempParticipants.getId());
    }
  }

  @GetMapping(
      value = "/game/{gameType}/amount/{questionId}/selectedparticipants/{amountId}/{userTempId}")
  public String seelctparticipants(@PathVariable String gameType, @PathVariable Long questionId,
      @PathVariable Long amountId, @PathVariable Long userTempId, Model model, Principal principal,
      HttpSession session) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      return "redirect:/user/game/" + gameType + "/entry";
    }

    UserInfo user = userService.findByEmail(principal.getName());
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    UserTempParticipants dbTempParticipants = userTempParticipantService
        .findCurrentParticipantById(userTempId, questionId, amountId, gameType, user);
    if (dbTempParticipants != null && StringUtils.hasText(dbTempParticipants.getParticipants())) {
      SelectedMembers selectedMembers =
          DataMapper.getRedirectTeamSelection(dbTempParticipants, user);
      Map<String, List<GameParticipants>> gameParticipants =
          gameParticipantsService.getAllActiveParticipantsByQuestionId(questionId);
      Map<String, Integer> gametypecounts =
          DataMapper.getGameTypeCounts(gameParticipants, selectedMembers);
      if (gametypecounts != null && gametypecounts.size() > 0) {
        model.addAttribute("gametypecounts", gametypecounts);
        model.addAttribute("gamecount", gametypecounts.size());
      }

      Map<String, Integer> gameplayercounts =
          DataMapper.getGamePlayerCounts(gameParticipants, selectedMembers);
      model.addAttribute("typecount", gameplayercounts);
      model.addAttribute("gameQuestion", gameQuestion);
      model.addAttribute("amountEntry", amountEntry);
      model.addAttribute("gameParticipants", gameParticipants);
      model.addAttribute("selectedMembers", selectedMembers);
      model.addAttribute("gameType", gameType);
      model.addAttribute("questionId", questionId);
      return "view/user/gameParticipants";
    } else {
      return "redirect:/user/game/" + gameType + "/entry";
    }
  }
  
  @PostMapping(value = "/game/previewUserTeam")
  public String previewUserTeam(@RequestBody SelectedParticipants participants, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    Map<String, List<GameParticipants>> teamParticipants = new LinkedHashMap<>();
    if(participants!=null && participants.getSelectedParticipants()!=null && !participants.getSelectedParticipants().isEmpty()) {
      List<GameParticipants> gameParticipants = gameParticipantsService.getAllParticipantsByQuestionIdAndParticipantIds(participants.getQuestionId(), participants.getSelectedParticipants());
      if(gameParticipants!=null && !gameParticipants.isEmpty()) {
        teamParticipants = DataMapper.getGameParticipants(gameParticipants, participants);
        model.addAttribute("teamParticipants", teamParticipants);
      }
    }
    model.addAttribute("teamParticipants", teamParticipants);
    return "view/user/teamParticipants";
  }
}
