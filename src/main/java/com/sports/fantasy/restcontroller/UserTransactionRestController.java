package com.sports.fantasy.restcontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.model.MatchPayments;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserTransactionService;

@RestController
@RequestMapping(value = "/user/api/")
public class UserTransactionRestController {

  @Autowired
  private UserTransactionService userTransactionService;
  @Autowired
  private UserService userService;

  @GetMapping(value = "/depositTransactions")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<UserTransactions>> depositTransactions(Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<UserTransactions> userTransactions = userTransactionService.getAllUserPaymentTransactions(user.getId(), "Payment");
    return new ResponseEntity<List<UserTransactions>>(userTransactions, HttpStatus.OK);
  }

  @GetMapping(value = "/matchTransactions")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<MatchPayments>> matchTransactions(Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<MatchPayments> userTransactions = userTransactionService.getAllUserMatchPayments(user.getId());
    return new ResponseEntity<List<MatchPayments>>(userTransactions, HttpStatus.OK);
  }
  
  @GetMapping(value = "/withDrawTransactions")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<UserTransactions>> withDrawTransactions(Principal principal) {
    UserInfo user = userService.findByEmail(principal.getName());
    List<UserTransactions> userTransactions = userTransactionService.getAllUserPaymentTransactions(user.getId(), "Withdraws");
    return new ResponseEntity<List<UserTransactions>>(userTransactions, HttpStatus.OK);
  }

}
