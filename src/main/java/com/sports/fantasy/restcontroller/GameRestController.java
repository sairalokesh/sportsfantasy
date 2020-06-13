package com.sports.fantasy.restcontroller;

import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
import com.sports.fantasy.domain.GameSelectedAmount;
import com.sports.fantasy.domain.Response;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.domain.SelectedParticipants;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.CuponCodeUsers;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.MatchPayments;
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
import com.sports.fantasy.userservice.UserTransactionService;
import com.sports.fantasy.util.DataMapper;

@RestController
@RequestMapping(value = "/user/api/")
public class GameRestController {

  @Autowired
  private UserService userService;
  @Autowired
  private GameQuestionsService gameQuestionsService;
  @Autowired
  private GameAmountService gameAmountService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired
  private GameParticipantService gameParticipantsService;
  @Autowired
  private UserTempParticipantService userTempParticipantService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  @Autowired
  private UserCouponService userCouponService;
  @Autowired private UserTransactionService userTransactionService;

  @GetMapping(value = "/game/{gameType}/entry")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<GameQuestions>> gameEntry(@PathVariable String gameType, Principal principal) {
    List<GameQuestions> gameQuestions = gameQuestionsService.getGameQuestionsByGreaterthanCurrentDate(gameType);
    return new ResponseEntity<List<GameQuestions>>(gameQuestions, HttpStatus.OK);
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameSelectedAmount> selectAmount(@PathVariable String gameType, @PathVariable Long questionId, Principal principal) throws AccessDeniedException {
    GameSelectedAmount gameSelectedAmount = new GameSelectedAmount();
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }

    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    
    List<AmountEntries> amountEntries = gameAmountService.findAllActiveAmountEntries();
    gameSelectedAmount.setGameQuestion(gameQuestion);
    gameSelectedAmount.setAmountEntries(amountEntries);
    gameSelectedAmount.setGameType(gameType);
    gameSelectedAmount.setQuestionId(questionId);
    return new ResponseEntity<GameSelectedAmount>(gameSelectedAmount, HttpStatus.OK);
  }
  
  @GetMapping(value = "/checkUserAmount/{gameType}/{questionId}/{amountId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Response> checkUserAmount(@PathVariable String gameType, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal) throws AccessDeniedException {
    Response response = new Response();
    UserInfo user = userService.findByEmail(principal.getName());
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    Long count = userSelectedTeamService.getSelectedUserCount(gameType, questionId, amountId, user.getId());
    if(count == null) {
      count = 0L;
    }
    response.setTeamCount(count);
    AmountEntries amountEntry = gameAmountService.findByAmountId(amountId);
    UserAmount amount = userAmountService.getUserAmount(user.getId());
    if (amount != null && amountEntry != null) {
      double bonusAmount = 0.00;
      if(Double.parseDouble(amountEntry.getAmount()) >=10 && Double.parseDouble(amountEntry.getAmount()) < 20) {
        bonusAmount = amount.getBonusAmount() >= 1 ? 1.00 : 0.00;
      } else if(Double.parseDouble(amountEntry.getAmount()) >=20 && Double.parseDouble(amountEntry.getAmount()) < 30) {
        bonusAmount = amount.getBonusAmount() >= 3 ? 3.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntry.getAmount()) >=30 && Double.parseDouble(amountEntry.getAmount()) < 50) {
        bonusAmount = amount.getBonusAmount() >= 5 ? 5.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntry.getAmount()) >=50 && Double.parseDouble(amountEntry.getAmount()) < 1000) {
        bonusAmount = amount.getBonusAmount() >= 10 ? 10.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntry.getAmount()) >=1000) {
        bonusAmount = amount.getBonusAmount() >= 15 ? 15.00 : amount.getBonusAmount();
      }
      
      double cashamount = Double.parseDouble(amountEntry.getAmount()) - bonusAmount;
      if (amount.getAddedAmount() >= cashamount) {
        response.setEnabled(true);
      } else {
        response.setEnabled(false);
      }
    } else {
      response.setEnabled(false);
    }
    
    return new ResponseEntity<Response>(response, HttpStatus.OK);
  }
  
  @GetMapping(value = "/game/{gameType}/amount/{questionId}/participants/{amountId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> selectparticipants(@PathVariable String gameType, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal) throws AccessDeniedException {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    AmountEntries amountEntries = gameAmountService.findByAmountId(amountId);
    Map<String, List<GameParticipants>> gameParticipants = gameParticipantsService.getAllActiveParticipantsByQuestionId(questionId);
    List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId);
    if (countries != null && !countries.isEmpty()) {
      Map<String, Integer> gamecountries = new TreeMap<>();
      for (String country : countries) {
        gamecountries.put(country, 0);
      }
      gameParticipantsDto.setGametypecounts(gamecountries);
      gameParticipantsDto.setGamecount(gamecountries.size());
    }

    if (gameParticipants != null && gameParticipants.size() > 0) {
      Set<String> types = gameParticipants.keySet();
      if (types != null && types.size() > 0) {
        Map<String, Integer> typecount = new TreeMap<>();
        for (String type : types) {
          typecount.put(type, 0);
        }
        gameParticipantsDto.setTypecount(typecount);
      }
    }

    gameParticipantsDto.setGameQuestion(gameQuestion);
    gameParticipantsDto.setAmountEntries(amountEntries);
    gameParticipantsDto.setGameParticipants(gameParticipants);
    gameParticipantsDto.setQuestionId(questionId);
    gameParticipantsDto.setAmountId(amountId);
    gameParticipantsDto.setGameType(gameType);
    return new ResponseEntity<GameParticipantsData>(gameParticipantsDto, HttpStatus.OK);
  }

  @PostMapping(value = "/game/viewparticipants")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Map<String, List<GameParticipants>>> viewparticipants(@RequestBody SelectedMembers selectedMembers, Principal principal) {
    Map<String, List<GameParticipants>> teamParticipants = new TreeMap<>();
    if (selectedMembers != null && selectedMembers.getSelectedMembers() != null && !selectedMembers.getSelectedMembers().isEmpty()) {
      List<GameParticipants> gameParticipants = gameParticipantsService.getAllParticipantsByQuestionIdAndParticipantIds(selectedMembers.getQuestionId(), selectedMembers.getSelectedMembers());
      if (gameParticipants != null && !gameParticipants.isEmpty()) {
        SelectedParticipants participants = new SelectedParticipants();
        participants.setSelectedParticipants(selectedMembers.getSelectedMembers());
        participants.setCaptainId(selectedMembers.getCapitanId());
        participants.setViceCaptainId(selectedMembers.getViceCapitanId());
        participants.setSuppoterId(selectedMembers.getSuppoterId());
        teamParticipants = DataMapper.getGameParticipants(gameParticipants, participants);
      }
    }
    return new ResponseEntity<Map<String, List<GameParticipants>>>(teamParticipants, HttpStatus.OK);
  }


  @PostMapping(value = "/game/sendcapitanselection")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<SelectedMembers> sendcapitanselection(@RequestBody SelectedMembers selectedMembers, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants dbUserTempParticipants = userTempParticipantService.save(selectedMembers, user);
    if (dbUserTempParticipants != null && dbUserTempParticipants.getId() != null) {
      selectedMembers.setMessage("Successfully added");
      selectedMembers.setUserTempId(dbUserTempParticipants.getId());
      return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.OK);
    }
    selectedMembers.setMessage("Something went wrong, Please try again");
    return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}/participants/{amountId}/gameparticipantrole/{usertempId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> selectedparticipantroles(@PathVariable String gameType, @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Long usertempId, Principal principal) throws NoSuchAlgorithmException, InvalidKeyException, AccessDeniedException {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    AmountEntries amountEntries = gameAmountService.findByAmountId(amountId);

    UserInfo user = userService.findByEmail(principal.getName());
    Map<String, List<GameParticipants>> gameParticipants = userTempParticipantService.getAllTempParticipantsByQuestionId(usertempId, questionId, amountId, gameType, user);
    SelectedMembers selectedMembers = userTempParticipantService.findById(usertempId, questionId, amountId, gameType, user);

    gameParticipantsDto.setGameQuestion(gameQuestion);
    gameParticipantsDto.setGameParticipants(gameParticipants);
    gameParticipantsDto.setAmountEntries(amountEntries);
    gameParticipantsDto.setQuestionId(questionId);
    gameParticipantsDto.setAmountId(amountId);
    gameParticipantsDto.setGameType(gameType);
    gameParticipantsDto.setUserTempId(usertempId);
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

  @PostMapping(value = "/game/updateteamrole")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<SelectedMembers> updateTeamRole(
      @RequestBody SelectedMembers selectedMembers, Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants userTempParticipants = userTempParticipantService.findCurrentParticipantById(selectedMembers.getUserTempId(), selectedMembers.getQuestionId(), selectedMembers.getAmountId(), selectedMembers.getGameType(), user);
    if (userTempParticipants != null) {
      UserTempParticipants updatedUserTempParticipants = userTempParticipantService.update(selectedMembers, userTempParticipants);
      if (updatedUserTempParticipants != null && updatedUserTempParticipants.getId() != null) {
        selectedMembers.setMessage("Role Updated");
        return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.OK);
      }
    }
    selectedMembers.setMessage("Role Not update");
    return new ResponseEntity<SelectedMembers>(selectedMembers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping(
      value = "/game/{gameType}/amount/{questionId}/participants/{amountId}/seletedpayments/{usertempId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> seletedpayments(@PathVariable String gameType, @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Long usertempId, Principal principal) throws NoSuchAlgorithmException, InvalidKeyException, AccessDeniedException {
    UserInfo user = userService.findByEmail(principal.getName());
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }
    AmountEntries amountEntries = gameAmountService.findByAmountId(amountId);
    SelectedMembers selectedMembers = userTempParticipantService.findById(usertempId, questionId, amountId, gameType, user);
    String lastCountry = "";
    List<String> countries = gameParticipantsService.getAllParticipantTypesByQuestionId(questionId);
    if (countries != null && !countries.isEmpty()) {
      for (String country : countries) {
        lastCountry = country;
      }
    }
    gameParticipantsDto.setLastCountry(lastCountry);

    UserAmount amount = userAmountService.getUserAmount(user.getId());
    if (gameQuestion != null && StringUtils.hasText(gameQuestion.getQuestion())) {
      gameQuestion.setGameQuestion(gameQuestion.getQuestion());
    }
    gameParticipantsDto.setGameQuestion(gameQuestion);
    gameParticipantsDto.setQuestionId(questionId);
    gameParticipantsDto.setAmountId(amountId);
    gameParticipantsDto.setGameType(gameType);
    gameParticipantsDto.setUserTempId(usertempId);
    gameParticipantsDto.setSelectedMembers(selectedMembers);

    if (amount != null && amountEntries != null) {
      gameParticipantsDto.setAmountEntries(amountEntries);
      double bonusAmount = 0.00;
      if(Double.parseDouble(amountEntries.getAmount()) >=10 && Double.parseDouble(amountEntries.getAmount()) < 20) {
        bonusAmount = amount.getBonusAmount() >= 1 ? 1.00 : 0.00;
      } else if(Double.parseDouble(amountEntries.getAmount()) >=20 && Double.parseDouble(amountEntries.getAmount()) < 30) {
        bonusAmount = amount.getBonusAmount() >= 3 ? 3.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=30 && Double.parseDouble(amountEntries.getAmount()) < 50) {
        bonusAmount = amount.getBonusAmount() >= 5 ? 5.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=50 && Double.parseDouble(amountEntries.getAmount()) < 1000) {
        bonusAmount = amount.getBonusAmount() >= 10 ? 10.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=1000) {
        bonusAmount = amount.getBonusAmount() >= 15 ? 15.00 : amount.getBonusAmount();
      }

      double cashamount = Double.parseDouble(amountEntries.getAmount()) - bonusAmount;
      if (amount.getAddedAmount() >= cashamount) {
        gameParticipantsDto.setBonusAmount(bonusAmount);
        gameParticipantsDto.setCashAmount(cashamount);
        return new ResponseEntity<GameParticipantsData>(gameParticipantsDto, HttpStatus.OK);
      }
    }

    return new ResponseEntity<GameParticipantsData>(gameParticipantsDto,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping(
      value = "/game/{gameType}/amount/{questionId}/participants/{amountId}/seletedpayments/{usertempId}/createfinalteam")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Response> createfinalteam(@PathVariable String gameType,
      @PathVariable Long questionId, @PathVariable Long amountId, @PathVariable Long usertempId,
      Principal principal) throws NoSuchAlgorithmException, InvalidKeyException, AccessDeniedException {
    Response response = new Response();

    GameQuestions gameQuestion = gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
    if (gameQuestion.getValidDate().before(new Date())) {
      throw new AccessDeniedException("Timeout");
    }

    UserInfo user = userService.findByEmail(principal.getName());
    UserTempParticipants userTempParticipants = userTempParticipantService.findCurrentParticipantById(usertempId, questionId, amountId, gameType, user);
    
    if (userTempParticipants != null && user != null && user.getId() == userTempParticipants.getUserId()) {
      UserAmount amount = userAmountService.getUserAmount(user.getId());
      AmountEntries amountEntries = gameAmountService.findByAmountId(amountId);
      
      double bonusAmount = 0.00;
      if(Double.parseDouble(amountEntries.getAmount()) >=10 && Double.parseDouble(amountEntries.getAmount()) < 20) {
        bonusAmount = amount.getBonusAmount() >= 1 ? 1.00 : 0.00;
      } else if(Double.parseDouble(amountEntries.getAmount()) >=20 && Double.parseDouble(amountEntries.getAmount()) < 30) {
        bonusAmount = amount.getBonusAmount() >= 3 ? 3.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=30 && Double.parseDouble(amountEntries.getAmount()) < 50) {
        bonusAmount = amount.getBonusAmount() >= 5 ? 5.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=50 && Double.parseDouble(amountEntries.getAmount()) < 1000) {
        bonusAmount = amount.getBonusAmount() >= 10 ? 10.00 : amount.getBonusAmount();
      } else if(Double.parseDouble(amountEntries.getAmount()) >=1000) {
        bonusAmount = amount.getBonusAmount() >= 15 ? 15.00 : amount.getBonusAmount();
      }
      
      double cashamount = Double.parseDouble(amountEntries.getAmount()) - bonusAmount;
      if (amount.getAddedAmount() >= cashamount) {
        UserSelectedTeam selectedTeam =  userSelectedTeamService.getUserSelectTeam(userTempParticipants);
        selectedTeam.setBonusAmount(bonusAmount);
        selectedTeam.setAddedAmount(cashamount);
        UserSelectedTeam dbUserSelectedTeam = userSelectedTeamService.save(selectedTeam);
        if (dbUserSelectedTeam != null && dbUserSelectedTeam.getId() != null) {
          double bonusRemainigAmount = amount.getBonusAmount() - bonusAmount;
          double addedRemainigAmount = amount.getAddedAmount() - cashamount;
          amount.setBonusAmount(bonusRemainigAmount);
          amount.setAddedAmount(addedRemainigAmount);
          UserAmount dbUserAmount = userAmountService.updateUserAmount(amount);
          if (dbUserAmount != null) {
            CuponCodeUsers codeUsers = userCouponService.getUserByUtilizerId(user.getId());
            if (codeUsers != null) {
              UserAmount userAmount = userAmountService.getUserAmount(codeUsers.getCreatorUser().getId());
              if (userAmount != null) {
                double bonusAddedAmount = userAmount.getBonusAmount() + 10;
                userAmount.setBonusAmount(bonusAddedAmount);
                userAmountService.updateUserAmount(amount);
                codeUsers.setCodeused(true);
                userCouponService.updateCuponCode(codeUsers);
              }
            }
            deleteTempUserSelectionParticipants(userTempParticipants);
            if (amountEntries != null && !amountEntries.getAmount().equals("0.0") && !amountEntries.getAmount().equals("0.00")) {
              MatchPayments matchPayments = new MatchPayments();
              matchPayments.setAddedAmount(cashamount);
              matchPayments.setAmountType("DEBITED");
              matchPayments.setBonusAmount(bonusAmount);
              matchPayments.setMatchName(gameQuestion.getQuestion());
              matchPayments.setTransactionDate(new Date());
              matchPayments.setUser(user);
              userTransactionService.saveMatchPayment(matchPayments);
            }
            response.setMessage("Team is Created Successfully");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
          } 
        }
      } 
    }
    response.setMessage("Some Thing Went Wrong Please Try Again");
    return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }



  private void deleteTempUserSelectionParticipants(UserTempParticipants dbTempParticipants) {
    if (dbTempParticipants != null && dbTempParticipants.getId() != null) {
      userTempParticipantService.deleteTempUserSelectedParticipantsById(dbTempParticipants.getId());
    }
  }



}
