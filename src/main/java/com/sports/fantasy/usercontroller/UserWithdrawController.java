package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserWithdrawAmount;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserWithDrawService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user")
public class UserWithdrawController {
  
  @Autowired
  private UserService userService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private UserWithDrawService userWithDrawService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "useraddcash");
  }
  
  @GetMapping(value = "/withcash")
  public String addcash(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser!=null && !dbUser.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }

    UserAmount userAmount = userAmountService.getUserAmount(dbUser.getId());
    UserAccount userAccount = userAccountService.getUserAccountInfo(dbUser.getId());
    List<UserWithdrawAmount> userWithdrawAmounts = userWithDrawService.getUserWithDraws(dbUser.getId());
    model.addAttribute("userWithdrawAmounts", userWithdrawAmounts);
    
    if(userAccount != null) {
      if(userAccount.getStatus().equalsIgnoreCase("Not_Verified")) {
        model.addAttribute("message", "Account is not verified, Please contact admin");
      }
      
    } else {
      model.addAttribute("message", "Account details not entered till now, Please fill Account Details");
    }
    
    model.addAttribute("userAmount", userAmount);
    model.addAttribute("user", new UserWithdrawAmount());
    return "view/user/withdraw";
  }
  
  @PostMapping(value = "/withdrawamount")
  public String withdrawamount(@ModelAttribute UserWithdrawAmount userWithdrawAmount, Principal principal, RedirectAttributes redirectAttributes) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser!=null && !dbUser.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    userWithdrawAmount.setUser(dbUser);
    userWithdrawAmount.setStatus("Pending");
    
    UserWithdrawAmount dbUserWithdrawAmount = userWithDrawService.saveUSerWithDrawAMount(userWithdrawAmount);
    if(dbUserWithdrawAmount !=null) {
      UserAmount amount = userAmountService.getUserAmount(dbUser.getId());
      if (amount != null) {
        if (amount.getAddedAmount() >= userWithdrawAmount.getAmount()) {
          double addedRemainigAmount = amount.getAddedAmount() - userWithdrawAmount.getAmount();
          amount.setAddedAmount(addedRemainigAmount);
          UserAmount dbUserAmount = userAmountService.updateUserAmount(amount);
          if(dbUserAmount != null) {
            redirectAttributes.addFlashAttribute("successmessage", "Amount will credit your account with in 2 - 3 working days");
          }
        }
      }
    }
    return "redirect:/user/withcash";
  }
}
