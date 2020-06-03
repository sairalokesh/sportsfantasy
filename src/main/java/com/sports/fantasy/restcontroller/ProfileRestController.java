package com.sports.fantasy.restcontroller;

import java.security.Principal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.domain.UserTokenInfo;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.util.PasswordUtil;
import com.sports.fantasy.util.PatternFormatUtil;

@RestController
@RequestMapping(value = "/user/api/")
public class ProfileRestController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserAccountService userAccountService;

  @GetMapping(value = "profile")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserInfo> profile(Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    userService.datetostringformat(user);
    return new ResponseEntity<UserInfo>(user, HttpStatus.OK);
  }

  @PostMapping(value = "/updateProfile")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTokenInfo> updateprofile(@RequestBody UserInfo user, Principal principal) {
    UserInfo dbcUser = userService.findByEmail(principal.getName());
    if (dbcUser != null && !dbcUser.getEmail().equalsIgnoreCase(user.getEmail())) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Email is Not Matched."), HttpStatus.NOT_FOUND);
    }

    UserInfo usermobile = userService.findByPhoneNumber(user.getPhoneNumber());
    if (usermobile != null && user.getId() != usermobile.getId()) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Phone Number is Already exist."), HttpStatus.FOUND);
    }

    userService.stringtodateformat(user);
    user.setUpdatedDate(new Date());
    UserInfo dbUser = userService.update(user);
    if (dbUser != null) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("User Updated Successfully!"), HttpStatus.OK);
    } else {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("User Profile not updated, please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/account")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserAccount> account(Principal principal) {
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
    return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
  }

  @GetMapping(value = "/editAccount")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserAccount> editAccount(Principal principal) {
    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAccount account = new UserAccount();
    if (dbUser != null) {
      account = userAccountService.getUserAccountInfo(dbUser.getId());
      if (account == null) {
        account = new UserAccount();
      }
    }
    return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
  }
  
  @PostMapping(value = "/updateAccount")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTokenInfo> updateAccount(@RequestBody UserAccount userAccount, Principal principal) {
    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAccount existAccount = userAccountService.getAccountByAccountId(userAccount.getAccountNumber());
    if (existAccount != null && dbUser != null && dbUser.getId() != null && dbUser.getId() != existAccount.getUser().getId()) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Account Number is already exist!"), HttpStatus.FOUND);
    }

    if (userAccount != null && userAccount.getId() != null) {
      UserAccount dbUserAccount = userAccountService.getUserAccountInfo(dbUser.getId());
      if (dbUserAccount != null && (!dbUserAccount.getAccountNumber().equals(userAccount.getAccountNumber()) || !dbUserAccount.getIfscCode().equals(userAccount.getIfscCode()))) {
        dbUserAccount.setStatus("Not_Verified");
        dbUserAccount.setVerifyMessage("Account is under Review");
      }

      if (dbUserAccount != null && dbUserAccount.getUser() != null && dbUserAccount.getId() == userAccount.getId()) {
        dbUserAccount.setAccountNumber(userAccount.getAccountNumber());
        dbUserAccount.setCardHolderName(userAccount.getCardHolderName());
        dbUserAccount.setIfscCode(userAccount.getIfscCode());
        dbUserAccount.setBankName(userAccount.getBankName());
        dbUserAccount.setBranchName(userAccount.getBranchName());
        dbUserAccount.setPanNumber(userAccount.getPanNumber());
        UserAccount updateUserAccount = userAccountService.updateUserAccount(dbUserAccount);
        if (updateUserAccount != null) {
          return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Account Updated successfully!"), HttpStatus.OK);
        }
      }
    } else {
      userAccount.setUser(dbUser);
      userAccount.setStatus("Not_Verified");
      userAccount.setVerifyMessage("Account is under Review");
      UserAccount updateUserAccount = userAccountService.updateUserAccount(userAccount);
      if (updateUserAccount != null) {
        return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Account Updated successfully!"), HttpStatus.OK);
      }
    }

    return new ResponseEntity<UserTokenInfo>(
        new UserTokenInfo("User Account not updated, please try again"),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @PostMapping(value = "/updatePassword")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTokenInfo> updatePassword(@RequestBody UserInfo user, Principal principal) {
    try {
      UserInfo dbUser = userService.findByEmail(user.getEmail());
      if (dbUser != null && user.getId() == dbUser.getId()) {
        if (StringUtils.hasText(user.getPassword())) {
          dbUser.setPassword(PasswordUtil.getEncodedPassword(user.getPassword()));
        }
        dbUser.setUpdatedDate(new Date());
        userService.update(dbUser);
        return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Password Updated successfully!"), HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Password not changed, Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Password not changed, Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
  }  
}
