package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserWithdrawAmount;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.userservice.UserWithDrawService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminWithdrawsController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserWithDrawService userWithDrawService;

  @Autowired
  private UserAccountService userAccountService;
  

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "adminwithdraws");
  }

  @GetMapping(value = "/withdraws")
  public String addcash(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser != null && !dbUser.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }

    List<UserWithdrawAmount> userWithdrawAmounts =
        userWithDrawService.getPendingWithDraws("Pending");
    model.addAttribute("userWithdrawAmounts", userWithdrawAmounts);
    model.addAttribute("user", new UserWithdrawAmount());
    return "view/admin/withdraw";
  }

  @GetMapping(value="/process/withdraw/{withdrawId}")
  public String savesettlement(@PathVariable Long withdrawId, Principal principal, Model model, RedirectAttributes attributes) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      UserWithdrawAmount userWithdrawAmount = userWithDrawService.findByWithdrawId(withdrawId);
      if(userWithdrawAmount!=null && userWithdrawAmount.getUser()!=null) {
        UserAccount userAccount = userAccountService.getUserAccountInfo(userWithdrawAmount.getUser().getId());
        if (userAccount!=null && StringUtils.hasText(userAccount.getBeneId())) {
          PaymentResponse paymentResponse =  userWithDrawService.amountSettlement(userWithdrawAmount, userAccount);
          if(paymentResponse!=null && StringUtils.hasText(paymentResponse.getResponseStatus()) && paymentResponse.getResponseStatus().equals("200")) {
            attributes.addFlashAttribute("successmessage", "Payout Done Successfully");
            attributes.addFlashAttribute("errormessage", "");
          } else {
              attributes.addFlashAttribute("errormessage", paymentResponse.getReponseMessage());
              attributes.addFlashAttribute("successmessage", "");
          }
        }
      }
      
      return "redirect:/admin/withdraws";
  }
}
