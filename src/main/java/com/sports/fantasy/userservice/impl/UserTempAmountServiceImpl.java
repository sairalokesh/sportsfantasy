package com.sports.fantasy.userservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserTempAmount;
import com.sports.fantasy.repository.UserTempAmountRepository;
import com.sports.fantasy.userservice.UserTempAmountService;

@Service
@Transactional
public class UserTempAmountServiceImpl implements UserTempAmountService {

  @Autowired
  private UserTempAmountRepository userTempAmountRepository;

  @Override
  public UserTempAmount saveUserTempAmount(UserAmount userAmount, String orderId) {
    UserTempAmount userTempAmount = new UserTempAmount();
    userTempAmount.setAmount(userAmount.getAddedAmount());
    userTempAmount.setOrderId(orderId);
    userTempAmount.setUserId(userAmount.getUser().getId());
    return userTempAmountRepository.save(userTempAmount);
  }

  @Override
  public UserTempAmount getUserTempAmount(String orderId) {
    return userTempAmountRepository.getUserTempAmount(orderId);
  }

  @Override
  public void deleteTempUserSelectedAmountById(Long userAmountTempId) {
    userTempAmountRepository.deleteById(userAmountTempId);
  }

  @Override
  public void deleteTempUserSelectedAmount(String orderId) {
    userTempAmountRepository.deleteTempUserSelectedAmount(orderId);
  }

}
