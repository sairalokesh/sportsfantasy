package com.sports.fantasy.userservice;

import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserTempAmount;

public interface UserTempAmountService {

  UserTempAmount saveUserTempAmount(UserAmount userAmount, String orderId);
  UserTempAmount getUserTempAmount(String orderId);
  void deleteTempUserSelectedAmountById(Long userAmountTempId);
  void deleteTempUserSelectedAmount(String orderId);

}
