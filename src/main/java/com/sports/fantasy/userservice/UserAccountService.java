package com.sports.fantasy.userservice;

import java.util.List;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;

public interface UserAccountService {

  UserAccount getUserAccountInfo(Long userId);
  UserAccount getAccountByAccountId(String accountNumber);
  UserAccount updateUserAccount(UserAccount account);
  List<UserAccount> getpendingaccounts();
  UserAccount findByAccountId(Long accountId);
  PaymentResponse verifyaccount(UserAccount userAccount);

}
