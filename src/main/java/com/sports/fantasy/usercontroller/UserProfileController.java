package com.sports.fantasy.usercontroller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.util.LoginUtil;
import com.sports.fantasy.util.PasswordUtil;
import com.sports.fantasy.util.PatternFormatUtil;

@Controller
@RequestMapping(value = "/user")
public class UserProfileController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserAccountService userAccountService;

  @ModelAttribute
  public void admindashboardtitle(Model model) {
    model.addAttribute("title", "userprofile");
  }

  @GetMapping(value = "/profile")
  public String profile(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAccount account = new UserAccount();
    if (dbUser != null) {
      account = userAccountService.getUserAccountInfo(dbUser.getId());
      if (account != null) {
        if (StringUtils.hasText(account.getAccountNumber())) {
          String accountNumber = PatternFormatUtil.getPatternFormat(account.getAccountNumber());
          account.setAccountNumber(accountNumber);
        }

        if (StringUtils.hasText(account.getPanNumber())) {
          String panNumber = PatternFormatUtil.getPatternFormat(account.getPanNumber());
          account.setPanNumber(panNumber);
        }
      } else {
        account = new UserAccount();
      }

    }
    model.addAttribute("account", account);
    model.addAttribute("user", dbUser);
    model.addAttribute("changeuser", new UserInfo(dbUser.getId(), dbUser.getEmail()));
    return "view/user/viewprofile";
  }

  @GetMapping(value = "/editprofile")
  public String editprofile(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAccount account = new UserAccount();
    if (dbUser != null) {
      userService.datetostringformat(dbUser);
      account = userAccountService.getUserAccountInfo(dbUser.getId());
      if (account == null) {
        account = new UserAccount();
      }
    }
    model.addAttribute("account", account);
    model.addAttribute("user", dbUser);

    return "view/user/editprofile";
  }

  @PostMapping(value = "/updateprofile")
  public String updateprofile(@ModelAttribute UserInfo user, Principal principal, Model model,
      RedirectAttributes attributes) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbcUser = userService.findByEmail(principal.getName());
    if (dbcUser != null && !dbcUser.getEmail().equalsIgnoreCase(user.getEmail())) {
      attributes.addFlashAttribute("errormessage", "Email is Not Matched.");
      return "redirect:/user/editprofile";
    }

    UserInfo usermobile = userService.findByPhoneNumber(user.getPhoneNumber());
    if (usermobile != null && user.getId() != usermobile.getId()) {
      attributes.addFlashAttribute("errormessage", "Phone Number is Already exist.");
      return "redirect:/user/editprofile";
    }

    userService.stringtodateformat(user);
    user.setUpdatedDate(new Date());
    UserInfo dbUser = userService.update(user);
    if (dbUser != null) {
      attributes.addFlashAttribute("successmessage", "Profile Updated successfully!");
      return "redirect:/user/editprofile";
    }
    return "view/user/editprofile";
  }

  @PostMapping(value = "/updateaccount")
  public String updateaccount(@ModelAttribute UserAccount userAccount, Principal principal,
      Model model, RedirectAttributes attributes) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAccount existAccount =
        userAccountService.getAccountByAccountId(userAccount.getAccountNumber());
    if (existAccount != null && dbUser != null && dbUser.getId() != null
        && dbUser.getId() != existAccount.getUser().getId()) {
      attributes.addFlashAttribute("errormessage", "Account Number is already exist!");
      return "redirect:/user/editprofile";
    }

    if (userAccount != null && userAccount.getId() != null) {
      UserAccount dbUserAccount = userAccountService.getUserAccountInfo(dbUser.getId());
      if (dbUserAccount != null
          && (!dbUserAccount.getAccountNumber().equals(userAccount.getAccountNumber())
              || !dbUserAccount.getIfscCode().equals(userAccount.getIfscCode()))) {
        dbUserAccount.setStatus("Not_Verified");
        dbUserAccount.setVerifyMessage("Account is under Review");
      }

      if (dbUserAccount != null && dbUserAccount.getUser() != null
          && dbUserAccount.getId() == userAccount.getId()) {
        dbUserAccount.setAccountNumber(userAccount.getAccountNumber());
        dbUserAccount.setCardHolderName(userAccount.getCardHolderName());
        dbUserAccount.setIfscCode(userAccount.getIfscCode());
        dbUserAccount.setBankName(userAccount.getBankName());
        dbUserAccount.setBranchName(userAccount.getBranchName());
        dbUserAccount.setPanNumber(userAccount.getPanNumber());
        UserAccount updateUserAccount = userAccountService.updateUserAccount(dbUserAccount);
        if (updateUserAccount != null) {
          attributes.addFlashAttribute("successmessage", "Account Updated successfully!");
          return "redirect:/user/editprofile";
        }
      }
    } else {
      userAccount.setUser(dbUser);
      userAccount.setStatus("Not_Verified");
      userAccount.setVerifyMessage("Account is under Review");
      UserAccount updateUserAccount = userAccountService.updateUserAccount(userAccount);
      if (updateUserAccount != null) {
        attributes.addFlashAttribute("successmessage", "Account Updated successfully!");
        return "redirect:/user/editprofile";
      }
    }

    attributes.addFlashAttribute("errormessage", "Account not update!");
    return "redirect:/user/editprofile";
  }

  @PostMapping(value = "/updatepassword")
  public String updatepassword(@ModelAttribute UserInfo user, Principal principal, Model model,
      RedirectAttributes attributes) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }
    try {
      UserInfo dbUser = userService.findByEmail(user.getEmail());
      if (dbUser != null && user.getId() == dbUser.getId()) {
        if (StringUtils.hasText(user.getPassword())) {
          dbUser.setPassword(PasswordUtil.getEncodedPassword(user.getPassword()));
        }
        dbUser.setUpdatedDate(new Date());
        userService.update(dbUser);
        return "redirect:/user/changepasswordsuccess";
      }
    } catch (Exception e) {
      attributes.addFlashAttribute("successmessage", "Password not changed, Please try again");
      return "redirect:/user/profile";
    }

    return null;
  }

  @GetMapping(value = "/changepasswordsuccess")
  public String logoutsuccess(HttpServletRequest request, HttpServletResponse response,
      RedirectAttributes attributes) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    attributes.addFlashAttribute("successmessage",
        "Password is successfully changed! Login with new password");
    return "redirect:/signin";
  }

}
