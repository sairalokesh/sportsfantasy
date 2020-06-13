package com.sports.fantasy.restcontroller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.domain.RankingResponse;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserPoints;
import com.sports.fantasy.domain.ViewParticipants;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserRankingService;
import com.sports.fantasy.userservice.UserSelectedTeamService;

@RestController
@RequestMapping(value = "/user/api/")
public class GameHistoryRestController {
  
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
  private GameParticipantService gameParticipantsService;
  
  @GetMapping(value = "/game/{gametype}/upcoming")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<SelectedTeam>> upcoming(@PathVariable String gametype, Principal principal, Model model) {
    UserInfo user = userService.findByEmail(principal.getName()); 
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedEditParticipants(user.getId(), gametype, true);
    return new ResponseEntity<List<SelectedTeam>>(selectedTeams, HttpStatus.OK);
  }
  
  @GetMapping(value = "/game/{gametype}/live")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<SelectedTeam>> live(@PathVariable String gametype, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedLiveParticipants(user.getId(), gametype, true);
    return new ResponseEntity<List<SelectedTeam>>(selectedTeams, HttpStatus.OK);
  }
  
  @GetMapping(value = "/game/{gametype}/completed")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<SelectedTeam>> completed(@PathVariable String gametype, Principal principal, Model model) {
    UserInfo user = userService.findByEmail(principal.getName()); 
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedCompletedParticipants(user.getId(), gametype, false);
    return new ResponseEntity<List<SelectedTeam>>(selectedTeams, HttpStatus.OK);
  }
  
  @GetMapping("/game/{gametype}/amount/{questionId}/userParticipants/{amountId}")
  public ResponseEntity<RankingResponse> getParticipantScores(@PathVariable String gametype, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, Model model) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user, true);
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gametype);
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    RankingResponse rankingResponse = new RankingResponse();
    rankingResponse.setAmountEntry(amountEntry);
    rankingResponse.setGameQuestion(gameQuestion);
    rankingResponse.setRankings(rankings);
    return new ResponseEntity<RankingResponse>(rankingResponse, HttpStatus.OK);
  }
  
  @GetMapping("/game/{gametype}/amount/{questionId}/completedParticipants/{amountId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<RankingResponse> completedParticipants(@PathVariable String gametype, @PathVariable Long questionId,
      @PathVariable Long amountId, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<Ranking> rankings = userRankingService.getSelectedParticipantsScore(questionId, amountId, gametype, user, true);
    GameQuestions gameQuestion = gameQuestionsService.getCompletedGameQuestionByQuestionId(questionId, gametype);
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    RankingResponse rankingResponse = new RankingResponse();
    rankingResponse.setAmountEntry(amountEntry);
    rankingResponse.setGameQuestion(gameQuestion);
    rankingResponse.setRankings(rankings);
    return new ResponseEntity<RankingResponse>(rankingResponse, HttpStatus.OK);
  }
  
  @GetMapping(value="/game/selectedUserParticipantsScore/{teamId}/{userId}/{questionId}/{amountId}")
  public ResponseEntity<ViewParticipants> selectedUserParticipantsScore(@PathVariable Long teamId, @PathVariable Long userId, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal) {
    ViewParticipants viewParticipants = new ViewParticipants();
    boolean isActive = true;
    UserInfo user = userService.findByEmail(principal.getName());
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
        List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId);
        String lastCountry = "";
        if (countries != null && !countries.isEmpty()) {
          for (String country : countries) {
            lastCountry = country;
          }
        }
        viewParticipants.setLastCountry(lastCountry);
        viewParticipants.setTeamParticipants(teamParticipants);
        return new ResponseEntity<ViewParticipants>(viewParticipants, HttpStatus.OK);
    } else {
      return new ResponseEntity<ViewParticipants>(viewParticipants, HttpStatus.INTERNAL_SERVER_ERROR);
    }    
    } else {
      viewParticipants.setMessage("Access_Denied");
      return new ResponseEntity<ViewParticipants>(viewParticipants, HttpStatus.PRECONDITION_FAILED);
    }   
  }
  
  @GetMapping(value="/game/selectedParticipantsScoreInfo/{participantId}/{questionId}/{teamId}/{amountId}/{userId}")
  public ResponseEntity<UserPoints> selectedParticipantsScoreInfo(@PathVariable Long participantId, @PathVariable Long questionId, @PathVariable Long teamId, @PathVariable Long amountId,@PathVariable Long userId, Principal principal) {
    UserPoints userPoints = new UserPoints();  
    UserSelectedTeam userSelectedTeam = userSelectedTeamService.getSelectedUserTeam(teamId, questionId, amountId, userId);
      if(userSelectedTeam != null) {
        userPoints = userSelectedTeamService.getSelectedParticipantScores(userSelectedTeam, participantId, questionId);
      }
      return new ResponseEntity<UserPoints>(userPoints, HttpStatus.OK);
  }
}
