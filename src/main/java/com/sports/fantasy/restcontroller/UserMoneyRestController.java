package com.sports.fantasy.restcontroller;

import java.security.Principal;
import java.util.List;
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
import com.sports.fantasy.model.CuponCodeUsers;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserWithdrawAmount;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAccountService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserCouponService;
import com.sports.fantasy.userservice.UserWithDrawService;

@RestController
@RequestMapping(value = "/user/api/")
public class UserMoneyRestController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserAmountService userAmountService;
  @Autowired
  private UserCouponService userCouponService;

  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private UserWithDrawService userWithDrawService;

  @GetMapping(value = "/addCash")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserAmount> addCash(Principal principal) {
    UserInfo dbUser = userService.findByEmail(principal.getName());
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
    }
    return new ResponseEntity<UserAmount>(userAmount, HttpStatus.OK);
  }

  @GetMapping(value = "/withDrawCash")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserAmount> withDrawCash(Principal principal) throws Exception {
    UserInfo dbUser = userService.findByEmail(principal.getName());
    UserAmount userAmount = userAmountService.getUserAmount(dbUser.getId());
    UserAccount userAccount = userAccountService.getUserAccountInfo(dbUser.getId());
    List<UserWithdrawAmount> userWithdrawAmounts = userWithDrawService.getUserWithDraws(dbUser.getId());
    userAmount.setUserWithdrawAmounts(userWithdrawAmounts);

    if (userAccount != null) {
      if (userAccount.getStatus().equalsIgnoreCase("Not_Verified")) {
        userAmount.setMessage("Account is not verified, Please contact admin");
        return new ResponseEntity<UserAmount>(userAmount, HttpStatus.PAYMENT_REQUIRED);
      }

    } else {
      userAmount.setMessage("Account details not entered till now, Please fill Account Details");
      return new ResponseEntity<UserAmount>(userAmount, HttpStatus.PRECONDITION_FAILED);
    }

    return new ResponseEntity<UserAmount>(userAmount, HttpStatus.OK);
  }
  
  @PostMapping(value = "/saveWithDrawAmount")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserTokenInfo> saveWithDrawAmount(@RequestBody UserWithdrawAmount userWithdrawAmount, Principal principal) {
    
    UserInfo dbUser = userService.findByEmail(principal.getName());
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
            return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Amount will credit your account with in 2 - 3 working days"), HttpStatus.OK);
          }
        }
      }
    }
    return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Something went wrong, Please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
  }  
}
