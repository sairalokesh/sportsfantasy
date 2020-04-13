package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserDashboardTeamParticipants;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.userservice.UserDashboardService;
import com.sports.fantasy.util.LoginUtil;
import com.sports.fantasy.util.PatternFormatUtil;

@Controller
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserDashboardService userDashboardService;
  @Autowired
  private UserAccountService userAccountService;

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "userdashboard");
  }

  @GetMapping(value = "/dashboard")
  public String userdashboard(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbUser = userService.findByEmail(principal.getName());
    Long userEntries = userDashboardService.getCountOfGameEntries(dbUser.getId());
    Double dbEntriesAmount = userDashboardService.getSumOfEntriesAmount(dbUser.getId());
    Long winningEntries = userDashboardService.getCountOfWinningEntries(dbUser.getId());
    Double dbWinningAmount = userDashboardService.getSumOfWinningAmount(dbUser.getId());
    String accountStatus = userDashboardService.getAccountStatusByUserId(dbUser.getId());
    List<UserTransactions> userParticipantTransactions = userDashboardService.getRecentTransactionsByUserId(dbUser.getId());
    List<UserDashboardTeamParticipants> userSelectedParticipants = userDashboardService.getRecentParticipantsByUserId(dbUser.getId());

    String accountNumber = "";
    String ifscCode = "";
    if (dbUser != null) {
      UserAccount account = userAccountService.getUserAccountInfo(dbUser.getId());
      if (account != null) {
        if (StringUtils.hasText(account.getAccountNumber())) {
          accountNumber = PatternFormatUtil.getPatternFormat(account.getAccountNumber());
        }

        if (StringUtils.hasText(account.getIfscCode())) {
          ifscCode = PatternFormatUtil.getPatternFormat(account.getIfscCode());
        }
      }
    }


    String status = "Not_Verified";
    if (StringUtils.hasText(accountStatus) && accountStatus.equalsIgnoreCase("Verified")) {
      status = "Verified";
    }


    String winningAmount = "0.00";
    if (dbWinningAmount != null && dbWinningAmount > 0) {
      winningAmount = dbWinningAmount.toString(); /*PatternFormatUtil.getFormattedAmount(dbWinningAmount);*/
    }


    String entriesAmount = "0.00";
    if (dbEntriesAmount != null && dbEntriesAmount > 0) {
      entriesAmount = dbEntriesAmount.toString(); /*PatternFormatUtil.getFormattedAmount(dbEntriesAmount);*/
    }


    model.addAttribute("accountNumber", accountNumber);
    model.addAttribute("userSelectedParticipants", userSelectedParticipants);
    model.addAttribute("userParticipantTransactions", userParticipantTransactions);
    model.addAttribute("ifscCode", ifscCode);
    model.addAttribute("status", status);
    model.addAttribute("entriesAmount", entriesAmount);
    model.addAttribute("winningEntries", winningEntries);
    model.addAttribute("winningAmount", winningAmount);
    model.addAttribute("userEntries", userEntries);
    model.addAttribute("user", dbUser);
    return "view/user/userdashboard";
  }
}
