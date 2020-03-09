package com.sports.fantasy.usercontroller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.domain.Response;
import com.sports.fantasy.model.CuponCodeUsers;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserCouponService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/user")
public class UserAddCashController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired
  private UserCouponService userCouponService;
  
  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "useraddcash");
  }


  @GetMapping(value = "/addcash")
  public String addcash(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser!=null && !dbUser.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }

    UserAmount userAmount = new UserAmount();
    
    if (dbUser != null && dbUser.getId() != null) {
      userAmount = userAmountService.getUserAmount(dbUser.getId());
      List<CuponCodeUsers> cuponCodeUsers = userCouponService.getAllUtilizerUsers(dbUser.getId());
      userAmount.setCuponCodeUsers(cuponCodeUsers);
      if (StringUtils.hasText(dbUser.getPhoneNumber())) {
        userAmount.setPhonevalid(true);
      } else {
        userAmount.setPhonevalid(false);
      }
      model.addAttribute("userAmount", userAmount);
      return "view/user/addcash";
    }
    return "view/user/addamount";
  }

  @GetMapping(value = "/saveUserPhoneNumber")
  public @ResponseBody Response saveuserphoneNumber(@RequestParam String phoneNumber, Principal principal, Model model, RedirectAttributes attributes) {
    if (!LoginUtil.getAuthentication(principal)) {
      return new Response(301, "Login Required");
    }
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser!=null && !dbUser.getRole().equalsIgnoreCase("USER")) {
      return new Response(301, "Access Denied");
    }
    
    UserInfo usermobile = userService.findByPhoneNumber(phoneNumber);
    if (usermobile != null) {
      return new Response(401, "Phone Number is Already exist.");
    }

    dbUser.setPhoneNumber(phoneNumber);
    UserInfo updateUser = userService.update(dbUser);
    if (updateUser != null) {
      return new Response(201, "Phone Number is updated.");
    }
    return null;
  }

  @GetMapping(value = "/adduseramount")
  public String addamount(Model model, Principal principal) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser!=null && !dbUser.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    UserAmount userAmount = new UserAmount();
    model.addAttribute("userAmount", userAmount);
    return "view/user/adduseramount";
  }

  @PostMapping(value = "/saveuseramount")
  public String saveuseramount(@ModelAttribute UserAmount userAmount, Model model, Principal principal) throws NoSuchAlgorithmException, InvalidKeyException {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo user = userService.findByEmail(principal.getName());
    if (user!=null && !user.getRole().equalsIgnoreCase("USER")) {
      return "redirect:/accessdenied";
    }
    
    if (userAmount != null) {
      if (user != null) {
        userAmount.setUser(user);
        userAmountService.addAmount(userAmount, user, "user/useramountpaymentresponse", model);
        return "view/user/merchantpayment";
      }
    }
    return "redirect:/user/adduseramount/" + userAmount.getAddedAmount();
  }
  
  @PostMapping(value="/useramountpaymentresponse")
  public String useramountpaymentresponse(HttpServletRequest request, RedirectAttributes attributes) {
      Response response = userAmountService.saveUserAmountTransactions(request);
      if(response!=null) {
          if(StringUtils.hasText(response.getMessage()) && response.getMessage().equalsIgnoreCase("success")) {
              attributes.addFlashAttribute("successmessage", "Transaction SuccessFully!");
              return "redirect:/user/addcash";
          } else {
               attributes.addFlashAttribute("errormessage", "Transaction Failed Amount will refund within 2 working days!");
               return "redirect:/user/adduseramount";
          }
      } else {
          return null;
      }
  }
}
