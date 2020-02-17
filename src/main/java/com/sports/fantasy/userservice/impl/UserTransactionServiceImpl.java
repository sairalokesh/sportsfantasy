package com.sports.fantasy.userservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.repository.UserTransactionRepository;
import com.sports.fantasy.userservice.UserTransactionService;

@Service
@Transactional
public class UserTransactionServiceImpl implements UserTransactionService {
  
  @Autowired private UserTransactionRepository userTransactionRepository;

  @Override
  public void saveUserAmountTransaction(UserTransactions userTransactions) {
    userTransactionRepository.save(userTransactions);
  }

  @Override
  public List<UserTransactions> getAllUserPaymentTransactions(Long userId, String transactionType) {
    return userTransactionRepository.getAllUserPaymentTransactions(userId, transactionType);
  }

}
