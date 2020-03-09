package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserSelectedTeamService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user")
public class TestController {
  
  @Autowired
  private UserService userService;
  @Autowired
  private UserSelectedTeamService userSelectedTeamService;
  
  
  @GetMapping(value = "/test/userteam/{userTeamId}")
  @ResponseBody
  public String gameedit(@PathVariable Long userTeamId, Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "success";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "success";
    }
    UserSelectedTeam userSelectedTeam = userSelectedTeamService.findByTeamId(userTeamId, user.getId());
    if (userSelectedTeam != null) {
      for(int i = 0; i<=10000; i++) {
        UserSelectedTeam userSelectedTeam2 = new UserSelectedTeam();
        if (i % 2 == 0) {
          userSelectedTeam2.setUser(new UserInfo(2L));
          userSelectedTeam2.setCaptainId(userSelectedTeam.getParticipantThreeId());
          userSelectedTeam2.setParticipantThreeId(userSelectedTeam.getCaptainId());
          userSelectedTeam2.setViceCaptainId(userSelectedTeam.getParticipantSixId());
          userSelectedTeam2.setParticipantSixId(userSelectedTeam.getViceCaptainId());
          userSelectedTeam2.setSuppoterId(userSelectedTeam.getParticipantEightId());
          userSelectedTeam2.setParticipantEightId(userSelectedTeam.getSuppoterId());
          userSelectedTeam2.setParticipantOneId(userSelectedTeam.getParticipantOneId());
          userSelectedTeam2.setParticipantTwoId(userSelectedTeam.getParticipantTwoId());
          userSelectedTeam2.setParticipantFourId(userSelectedTeam.getParticipantFourId());
          userSelectedTeam2.setParticipantFiveId(userSelectedTeam.getParticipantFiveId());
          userSelectedTeam2.setParticipantSevenId(userSelectedTeam.getParticipantSevenId()); 
          
        } else if (i % 3 == 0) {
          userSelectedTeam2.setUser(new UserInfo(3L));
          userSelectedTeam2.setCaptainId(userSelectedTeam.getParticipantTwoId());
          userSelectedTeam2.setParticipantTwoId(userSelectedTeam.getCaptainId());
          userSelectedTeam2.setViceCaptainId(userSelectedTeam.getParticipantFourId());
          userSelectedTeam2.setParticipantFourId(userSelectedTeam.getViceCaptainId());
          userSelectedTeam2.setSuppoterId(userSelectedTeam.getParticipantSixId());
          userSelectedTeam2.setParticipantSixId(userSelectedTeam.getSuppoterId());
          userSelectedTeam2.setParticipantOneId(userSelectedTeam.getParticipantOneId());
          userSelectedTeam2.setParticipantThreeId(userSelectedTeam.getParticipantThreeId());
          userSelectedTeam2.setParticipantFiveId(userSelectedTeam.getParticipantFiveId());
          userSelectedTeam2.setParticipantSevenId(userSelectedTeam.getParticipantSevenId());
          userSelectedTeam2.setParticipantEightId(userSelectedTeam.getParticipantEightId());
        } else if (i % 11 == 0) {
          userSelectedTeam2.setUser(new UserInfo(4L));
          userSelectedTeam2.setCaptainId(userSelectedTeam.getParticipantSevenId());
          userSelectedTeam2.setParticipantSevenId(userSelectedTeam.getCaptainId());
          userSelectedTeam2.setViceCaptainId(userSelectedTeam.getParticipantOneId());
          userSelectedTeam2.setParticipantOneId(userSelectedTeam.getViceCaptainId());
          userSelectedTeam2.setSuppoterId(userSelectedTeam.getParticipantFiveId());
          userSelectedTeam2.setParticipantFiveId(userSelectedTeam.getSuppoterId());
          userSelectedTeam2.setParticipantTwoId(userSelectedTeam.getParticipantTwoId());
          userSelectedTeam2.setParticipantThreeId(userSelectedTeam.getParticipantThreeId());
          userSelectedTeam2.setParticipantFourId(userSelectedTeam.getParticipantFourId());
          userSelectedTeam2.setParticipantSixId(userSelectedTeam.getParticipantSixId());
          userSelectedTeam2.setParticipantEightId(userSelectedTeam.getParticipantEightId());
        } else {
          userSelectedTeam2.setUser(userSelectedTeam.getUser());
          userSelectedTeam2.setCaptainId(userSelectedTeam.getCaptainId());
          userSelectedTeam2.setParticipantSevenId(userSelectedTeam.getParticipantSevenId());
          userSelectedTeam2.setViceCaptainId(userSelectedTeam.getViceCaptainId());
          userSelectedTeam2.setParticipantOneId(userSelectedTeam.getParticipantOneId());
          userSelectedTeam2.setSuppoterId(userSelectedTeam.getSuppoterId());
          userSelectedTeam2.setParticipantFiveId(userSelectedTeam.getParticipantFiveId());
          userSelectedTeam2.setParticipantTwoId(userSelectedTeam.getParticipantTwoId());
          userSelectedTeam2.setParticipantThreeId(userSelectedTeam.getParticipantThreeId());
          userSelectedTeam2.setParticipantFourId(userSelectedTeam.getParticipantFourId());
          userSelectedTeam2.setParticipantSixId(userSelectedTeam.getParticipantSixId());
          userSelectedTeam2.setParticipantEightId(userSelectedTeam.getParticipantEightId());
        }
        userSelectedTeam2.setGameQuestions(userSelectedTeam.getGameQuestions());
        userSelectedTeam2.setAmountEntries(userSelectedTeam.getAmountEntries());
        userSelectedTeam2.setCreatedDate(new Date());
        userSelectedTeam2.setPlayerResult(userSelectedTeam.getPlayerResult());
        userSelectedTeam2.setWinningAmount(userSelectedTeam.getWinningAmount());
        userSelectedTeam2.setPaymentDone(userSelectedTeam.getPaymentDone());
        userSelectedTeam2.setGameType(userSelectedTeam.getGameType());
        userSelectedTeam2.setAddedAmount(userSelectedTeam.getAddedAmount());
        userSelectedTeam2.setBonusAmount(userSelectedTeam.getBonusAmount());
        userSelectedTeamService.save(userSelectedTeam2);
        
      }
    }
    return "failure";
  }

}
