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
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminAccountVerificationController {
  
  @Autowired
  private UserAccountService userAccountService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
      model.addAttribute("title", "adminaccount");
  }
  
  @GetMapping(value="/getpendingaccounts")
  public String getpendingaccounts(Principal principal, Model model, RedirectAttributes attributes) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      return "redirect:/admin/verifyaccount";
  }
  
  @GetMapping(value="/verifyaccount")
  public String verifyaccount(Principal principal, Model model) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      List<UserAccount> userAccounts = userAccountService.getpendingaccounts();
      model.addAttribute("userAccounts", userAccounts);
      return "view/admin/accountsverification";
  }
  
  @GetMapping(value="/verifyaccount/{accountId}")
  public String verifyaccount(@PathVariable Long accountId, Principal principal, Model model, RedirectAttributes attributes) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      UserAccount userAccount = userAccountService.findByAccountId(accountId);
      if(userAccount!=null) {
        PaymentResponse paymentResponse =  userAccountService.verifyaccount(userAccount);
          if(paymentResponse!=null && StringUtils.hasText(paymentResponse.getResponseStatus()) && paymentResponse.getResponseStatus().equals("200")) {
              attributes.addFlashAttribute("successmessage", "Validation Done Successfully");
          } else {
              attributes.addFlashAttribute("errormessage", paymentResponse.getReponseMessage());
          }
      }
      
      return "redirect:/admin/verifyaccount";
  }
  

}
