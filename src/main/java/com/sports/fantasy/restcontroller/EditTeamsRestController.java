package com.sports.fantasy.restcontroller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.domain.GameParticipantsData;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserTeamMembers;
import com.sports.fantasy.domain.UserTokenInfo;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.MatchPayments;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UserTempParticipants;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.userservice.UserTempParticipantService;
import com.sports.fantasy.userservice.UserTransactionService;
import com.sports.fantasy.util.DataMapper;

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
  @Autowired
  private GameParticipantService gameParticipantsService;
  @Autowired
  private UserTempParticipantService userTempParticipantService;

  @GetMapping(value = "/game/{gameType}/edit")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<SelectedTeam>> gameedit(@PathVariable String gameType, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<SelectedTeam> selectedTeams = userSelectedTeamService.getTeamSelectedEditParticipants(user.getId(), gameType, true);
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
  
  
  @GetMapping(value = "/game/{gameType}/editParticipants/{teamId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> editParticipants(@PathVariable String gameType, @PathVariable Long teamId, Principal principal) throws AccessDeniedException {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    UserInfo user = userService.findByEmail(principal.getName());
    UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(teamId, user.getId());
    if (userSelectedTeam != null) {
      GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(userSelectedTeam.getGameQuestions().getId(), gameType);
      if (gameQuestion.getValidDate().before(new Date())) {
        throw new AccessDeniedException("Timeout");
      }

      if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
        gameQuestion.setGameQuestion(gameQuestion.getQuestion());
      }
      
      AmountEntries amountEntries = gameAmountService.findByAmountId(userSelectedTeam.getAmountEntries().getId());
      SelectedMembers selectedMembers = DataMapper.getSelectedMembers(userSelectedTeam);
      Map<String, List<GameParticipants>> gameParticipants = gameParticipantsService.getAllActiveParticipantsByQuestionId(userSelectedTeam.getGameQuestions().getId());
      Map<String, Integer> gametypecounts = DataMapper.getGameTypeCounts(gameParticipants, selectedMembers);
      if (gametypecounts != null && gametypecounts.size() > 0) {
        gameParticipantsDto.setGametypecounts(gametypecounts);
        gameParticipantsDto.setGamecount(gametypecounts.size());
      }

      Map<String, Integer> gameplayercounts = DataMapper.getGamePlayerCounts(gameParticipants, selectedMembers);
      gameParticipantsDto.setTypecount(gameplayercounts);
      gameParticipantsDto.setSelectedMembers(selectedMembers);
      gameParticipantsDto.setGameQuestion(gameQuestion);
      gameParticipantsDto.setAmountEntries(amountEntries);
      gameParticipantsDto.setGameParticipants(gameParticipants);
      gameParticipantsDto.setQuestionId(userSelectedTeam.getGameQuestions().getId());
      gameParticipantsDto.setAmountId(userSelectedTeam.getAmountEntries().getId());
      gameParticipantsDto.setGameType(gameType);
      return new ResponseEntity<GameParticipantsData>(gameParticipantsDto, HttpStatus.OK);
    }
    
    throw new AccessDeniedException("Internal Server Error");
  }
  
  @PostMapping(value = "/game/editCaptainSelection")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<SelectedMembers> editCaptainSelection(@RequestBody SelectedMembers selectedMembers, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(selectedMembers.getUserTeamId(), user.getId());
    if (userSelectedTeam != null && userSelectedTeam.getId() != null) {
      UserTempParticipants dbUserTempParticipants = userTempParticipantService.editsave(selectedMembers, user);
      if (dbUserTempParticipants != null && dbUserTempParticipants.getId() != null) {
        selectedMembers.setMessage("Successfully Updated");
        selectedMembers.setUserTempId(dbUserTempParticipants.getId());
        return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.OK);
      }
    }

    selectedMembers.setMessage("Something went wrong, Please try again");
    return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @GetMapping(value = "/edit/game/{gameType}/amount/{questionId}/participants/{amountId}/selectedParticipantRoles/{userTempId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> selectedParticipantRoles(@PathVariable String gameType,
      @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Long userTempId, Principal principal) throws AccessDeniedException {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    UserInfo user = userService.findByEmail(principal.getName());
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    AmountEntries amountEntries = gameAmountService.findByAmountId(amountId);
    Map<String, List<GameParticipants>> gameParticipants = userTempParticipantService.getAllTempParticipantsByQuestionId(userTempId, questionId, amountId, gameType, user);
    SelectedMembers selectedMembers = userTempParticipantService.findById(userTempId, questionId, amountId, gameType, user);
    UserTempParticipants dbTempParticipants = userTempParticipantService.findCurrentParticipantById(userTempId, questionId, amountId, gameType, user);
    
    gameParticipantsDto.setGameQuestion(gameQuestion);
    gameParticipantsDto.setGameParticipants(gameParticipants);
    gameParticipantsDto.setAmountEntries(amountEntries);
    gameParticipantsDto.setQuestionId(questionId);
    gameParticipantsDto.setAmountId(amountId);
    gameParticipantsDto.setGameType(gameType);
    gameParticipantsDto.setUserTempId(userTempId);
    gameParticipantsDto.setUserTeamId(dbTempParticipants.getUserTeamId());
    gameParticipantsDto.setSelectedMembers(selectedMembers);
    String lastCountry = "";
    List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId);
    if (countries != null && !countries.isEmpty()) {
      for (String country : countries) {
        lastCountry = country;
      }
    }
    gameParticipantsDto.setLastCountry(lastCountry);
    return new ResponseEntity<GameParticipantsData>(gameParticipantsDto, HttpStatus.OK);
  }
  
  @PostMapping(value = "/game/updateParticipants")
  public ResponseEntity<SelectedMembers> updateParticipants(@RequestBody SelectedMembers selectedMembers, Principal principal) throws AccessDeniedException {
   
    UserInfo user = userService.findByEmail(principal.getName());
    
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(selectedMembers.getQuestionId(), selectedMembers.getGameType());
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    
    UserTempParticipants userTempParticipants = userTempParticipantService.findCurrentParticipantById(selectedMembers.getUserTempId(),selectedMembers.getQuestionId(), selectedMembers.getAmountId(), selectedMembers.getGameType(), user);
    if (userTempParticipants != null && userTempParticipants.getUserTeamId() != null) {
      UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(userTempParticipants.getUserTeamId(), user.getId());
      if (userSelectedTeam != null) {
        userTempParticipants.setCapitanId(selectedMembers.getCapitanId());
        userTempParticipants.setViceCapitanId(selectedMembers.getViceCapitanId());
        userTempParticipants.setSuppoterId(selectedMembers.getSuppoterId());
        UserSelectedTeam selectedTeam = userSelectedTeamService.getUserSelectTeam(userTempParticipants);
        selectedTeam.setId(userTempParticipants.getUserTeamId());
        UserSelectedTeam dbUserSelectedTeam = userSelectedTeamService.save(selectedTeam);
        if (dbUserSelectedTeam != null) {
          
          deleteTempUserSelectionParticipants(userTempParticipants);
          selectedMembers.setMessage("Team Updated Successflly!");
          return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.OK);
       
        }
      }
    }
    selectedMembers.setMessage("Something went wrong, Please try again");
    return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  
  @GetMapping(value = "/edit/game/{gameType}/amount/{questionId}/editSelectedParticipants/{amountId}/{userTempId}")
  public ResponseEntity<GameParticipantsData> reSelectparticipants(@PathVariable String gameType, @PathVariable Long questionId,
      @PathVariable Long amountId, @PathVariable Long userTempId, Principal principal) throws AccessDeniedException {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    UserInfo user = userService.findByEmail(principal.getName());
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }

    UserTempParticipants dbTempParticipants = userTempParticipantService.findCurrentParticipantById(userTempId, questionId, amountId, gameType, user);
    if (dbTempParticipants != null && StringUtils.hasText(dbTempParticipants.getParticipants())) {
      SelectedMembers selectedMembers = DataMapper.getRedirectEditTeamSelection(dbTempParticipants, user);
      AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
      Map<String, List<GameParticipants>> gameParticipants = gameParticipantsService.getAllActiveParticipantsByQuestionId(questionId);
      Map<String, Integer> gametypecounts =  DataMapper.getGameTypeCounts(gameParticipants, selectedMembers);
      if (gametypecounts != null && gametypecounts.size() > 0) {
        gameParticipantsDto.setGametypecounts(gametypecounts);
        gameParticipantsDto.setGamecount(gametypecounts.size());
      }

      Map<String, Integer> gameplayercounts = DataMapper.getGamePlayerCounts(gameParticipants, selectedMembers);
      
      gameParticipantsDto.setTypecount(gameplayercounts);
      gameParticipantsDto.setSelectedMembers(selectedMembers);
      gameParticipantsDto.setGameQuestion(gameQuestion);
      gameParticipantsDto.setAmountEntries(amountEntry);
      gameParticipantsDto.setGameParticipants(gameParticipants);
      gameParticipantsDto.setQuestionId(gameQuestion.getId());
      gameParticipantsDto.setAmountId(amountEntry.getId());
      gameParticipantsDto.setGameType(gameType);
      gameParticipantsDto.setUserTeamId(dbTempParticipants.getUserTeamId());
      String lastCountry = "";
      List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId);
      if (countries != null && !countries.isEmpty()) {
        for (String country : countries) {
          lastCountry = country;
        }
      }
      gameParticipantsDto.setLastCountry(lastCountry);
      return new ResponseEntity<GameParticipantsData>(gameParticipantsDto, HttpStatus.OK);
    } else {
      throw new AccessDeniedException("Internal Server Error");
    }
  }
  
  private void deleteTempUserSelectionParticipants(UserTempParticipants dbTempParticipants) {
    if (dbTempParticipants != null && dbTempParticipants.getId() != null) {
      userTempParticipantService.deleteTempUserSelectedParticipantsById(dbTempParticipants.getId());
    }
  } 
}
