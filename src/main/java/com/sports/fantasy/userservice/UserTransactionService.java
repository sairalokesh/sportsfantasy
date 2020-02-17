package com.sports.fantasy.userservice;

import java.util.List;
import com.sports.fantasy.model.UserTransactions;

public interface UserTransactionService {

  public void saveUserAmountTransaction(UserTransactions userTransactions);
  public List<UserTransactions> getAllUserPaymentTransactions(Long userId, String transactionType);

}
