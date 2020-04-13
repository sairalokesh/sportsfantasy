package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sports.fantasy.model.MatchPayments;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserTransactionService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping("/user")
public class UserTransactionController {

  @Autowired
  private UserTransactionService userTransactionService;
  @Autowired
  private UserService userService;

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "usertransactions");
  }

  @GetMapping(value = "/transactions")
  public String transactionsuccess(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    List<UserTransactions> userTransactions =
        userTransactionService.getAllUserPaymentTransactions(user.getId(), "Payment");
    model.addAttribute("usertransactions", userTransactions);
    return "view/user/transactions";
  }
  
  
  @GetMapping(value = "/matchpayments")
  public String matchpayments(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    List<MatchPayments> userTransactions = userTransactionService.getAllUserMatchPayments(user.getId());
    model.addAttribute("usertransactions", userTransactions);
    return "view/user/matchpayments";
  }

  @GetMapping(value = "/withdraws")
  public String withdraws(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    List<UserTransactions> userTransactions =
        userTransactionService.getAllUserPaymentTransactions(user.getId(), "Withdraws");
    model.addAttribute("usertransactions", userTransactions);
    return "view/user/withdraws";
  }

}
