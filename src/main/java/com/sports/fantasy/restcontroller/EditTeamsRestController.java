package com.sports.fantasy.restcontroller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserTeamMembers;
import com.sports.fantasy.domain.UserTokenInfo;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.MatchPayments;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.userservice.UserTransactionService;

@RestController
@RequestMapping(value = "/user/api/")
public class EditTeamsRestController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  @Autowired
  private GameAmountService gameAmountService;
  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired 
  private UserTransactionService userTransactionService;

  @GetMapping(value = "/game/{gameType}/edit")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<SelectedTeam>> gameedit(@PathVariable String gameType,
      Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<SelectedTeam> selectedTeams =
        userSelectedTeamService.getTeamSelectedEditParticipants(user.getId(), gameType, true);
    return new ResponseEntity<List<SelectedTeam>>(selectedTeams, HttpStatus.OK);
  }

  @GetMapping(value = "/game/{gameType}/teamParticipants/{questionId}/{amountId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTeamMembers> teamParticipants(@PathVariable String gameType,
      @PathVariable Long questionId, @PathVariable Long amountId, Principal principal)
      throws AccessDeniedException {

    UserInfo user = userService.findByEmail(principal.getName());
    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }

    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }

    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    List<UserTeamParticipants> userSelectedTeams =
        userSelectedTeamService.getTeamsEditParticipants(questionId, amountId, user.getId());
    UserTeamMembers userTeamMembers = new UserTeamMembers();
    userTeamMembers.setUserTeamParticipants(userSelectedTeams);
    userTeamMembers.setGameQuestion(gameQuestion);
    userTeamMembers.setAmountEntry(amountEntry);
    return new ResponseEntity<UserTeamMembers>(userTeamMembers, HttpStatus.OK);
  }
  
  @GetMapping(value = "/game/deleteUserTeam/{userTeamId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTokenInfo> deleteUserTeam(@PathVariable Long userTeamId, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(userTeamId, user.getId());
    if (userSelectedTeam != null) {
      UserAmount amount = userAmountService.getUserAmount(user.getId());
      if (amount != null) {
        double bonusRemainigAmount = amount.getBonusAmount() + userSelectedTeam.getBonusAmount();
        double addedRemainigAmount = amount.getAddedAmount() + (userSelectedTeam.getAddedAmount() - 1);
        amount.setBonusAmount(bonusRemainigAmount);
        amount.setAddedAmount(addedRemainigAmount);
        UserAmount dbUserAmount = userAmountService.updateUserAmount(amount);
        if (dbUserAmount != null) {
          userSelectedTeamService.deleteUserTeam(userTeamId);
          if (userSelectedTeam.getAmountEntries() != null && !userSelectedTeam.getAmountEntries().getAmount().equals("0.0") && !userSelectedTeam.getAmountEntries().getAmount().equals("0.00")) {
            MatchPayments matchPayments = new MatchPayments();
            matchPayments.setAddedAmount(userSelectedTeam.getAddedAmount() - 1);
            matchPayments.setAmountType("CREDITED");
            matchPayments.setBonusAmount(userSelectedTeam.getBonusAmount());
            matchPayments.setMatchName(userSelectedTeam.getGameQuestions().getQuestion());
            matchPayments.setTransactionDate(new Date());
            matchPayments.setUser(user);
            userTransactionService.saveMatchPayment(matchPayments);
          }
          return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Team deleted successfully"), HttpStatus.OK);
        }
      }
    }
    return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Something went wrong, Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
