package com.sports.fantasy.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.domain.Response;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAmountService;

@Controller
public class AddAmountController {
  @Autowired private UserService userService;
  @Autowired private UserAmountService userAmountService;
  
  @GetMapping(value="/addAmount/{userId}")
  public String addamount(@PathVariable Long userId, Model model) {
      UserAmount userAmount = new UserAmount();
      userAmount.setUser(new UserInfo(userId));
      model.addAttribute("userAmount", userAmount);
      return "view/user/addamount";
  }
  
  @PostMapping(value="/saveMobileAmount")
  public String saveMobileAmount(@ModelAttribute UserAmount userAmount, Model model, RedirectAttributes attributes) throws NoSuchAlgorithmException, InvalidKeyException {
      if(userAmount!=null && userAmount.getUser()!=null && userAmount.getUser().getId()!=null) {
          UserInfo user = userService.findById(userAmount.getUser().getId());
          if(user!=null) {
            userAmountService.addAmount(userAmount, user, "amountMobilePaymentResponse", model);
              return "view/user/merchantpayment";
          }
      }
      return "redirect:/addAmount/"+userAmount.getAddedAmount();
  }
  
  
  @RequestMapping(value="/amountMobilePaymentResponse")
  public String amountMobilePaymentResponse(HttpServletRequest request, HttpSession session, Principal principal, RedirectAttributes attributes) {
      Response response = userAmountService.saveUserAmountTransactions(request);
      if(response!=null) {
          if(StringUtils.hasText(response.getMessage()) && response.getMessage().equalsIgnoreCase("success")) {
              return "redirect:/successpage";
          } else {
               attributes.addFlashAttribute("errormessage", "Transaction Failed Amount will refund within 2 working days!");
               return "redirect:/addamount/"+response.getUserId();
          }
      } else {
          return null;
      }
  }
  
  @GetMapping(value="/successpage")
  public String  successpage(Model model) {
    model.addAttribute("successmessage", "Transaction SuccessFully! Please click on X Symbol for redirecting to amount page");
      return "view/user/successpage";
  }

}
