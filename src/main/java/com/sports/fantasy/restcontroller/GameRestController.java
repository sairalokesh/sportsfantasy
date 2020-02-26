package com.sports.fantasy.restcontroller;

import java.nio.file.AccessDeniedException;
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
import org.springframework.ui.Model;
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
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.userservice.UserAmountService;
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

  @GetMapping(value = "/game/{gameType}/entry")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<GameQuestions>> gameentry(@PathVariable String gameType,
      Principal principal) {
    List<GameQuestions> gameQuestions =
        gameQuestionsService.getGameQuestionsByGreaterthanCurrentDate(gameType);
    return new ResponseEntity<List<GameQuestions>>(gameQuestions, HttpStatus.OK);
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameSelectedAmount> selectamount(@PathVariable String gameType,
      @PathVariable Long questionId, Principal principal) throws AccessDeniedException {
    GameSelectedAmount gameSelectedAmount = new GameSelectedAmount();
    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
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

  @GetMapping(value = "/game/checkuseramount/{amountId}/{userId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Response> checkuseramount(@PathVariable Long amountId,
      @PathVariable Long userId, Principal principal) {
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
        return new ResponseEntity<Response>(new Response(200, "success"), HttpStatus.OK);
      } else {
        return new ResponseEntity<Response>(new Response(400, "failure"), HttpStatus.OK);
      }
    } else {
      return new ResponseEntity<Response>(new Response(400, "failure"), HttpStatus.OK);
    }
  }

  @GetMapping(value = "/game/{gameType}/amount/{questionId}/participants/{amountId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<GameParticipantsData> selectparticipants(@PathVariable String gameType,
      @PathVariable Long questionId, @PathVariable Long amountId, Model model,
      Principal principal) {
    GameParticipantsData gameParticipantsDto = new GameParticipantsData();
    GameQuestions gameQuestion =
        gameQuestionsService.getGameQuestionByQuestionId(questionId, gameType);
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
  
  @PostMapping(value="/game/viewparticipants")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Map<String, List<GameParticipants>>> viewparticipants(@RequestBody SelectedMembers selectedMembers, Principal principal) {
    Map<String, List<GameParticipants>> teamParticipants = new TreeMap<>();
    if(selectedMembers!=null && selectedMembers.getSelectedMembers()!=null && !selectedMembers.getSelectedMembers().isEmpty()) {
      List<GameParticipants> gameParticipants = gameParticipantsService.getAllParticipantsByQuestionIdAndParticipantIds(selectedMembers.getQuestionId(), selectedMembers.getSelectedMembers());
      if(gameParticipants!=null && !gameParticipants.isEmpty()) {
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
}
