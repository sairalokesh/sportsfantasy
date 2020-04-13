package com.sports.fantasy.userservice;

import java.util.List;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserWithdrawAmount;

public interface UserWithDrawService {

  List<UserWithdrawAmount> getUserWithDraws(Long userId);
  UserWithdrawAmount saveUSerWithDrawAMount(UserWithdrawAmount userWithdrawAmount);
  List<UserWithdrawAmount> getPendingWithDraws(String status);
  UserWithdrawAmount findByWithdrawId(Long withdrawId);
  PaymentResponse amountSettlement(UserWithdrawAmount userWithdrawAmount, UserAccount userAccount);

}
