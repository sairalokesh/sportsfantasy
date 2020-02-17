package com.sports.fantasy.userservice;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.sports.fantasy.domain.Response;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;

public interface UserAmountService {

  UserAmount saveUserAmount(Long userId);
  UserAmount getUserAmount(Long userId);
  public void addAmount(UserAmount userAmount, UserInfo user, String returnUrl, Model model) throws NoSuchAlgorithmException, InvalidKeyException;
  Response saveUserAmountTransactions(HttpServletRequest request);
  UserAmount updateUserAmount(UserAmount userAmount);

}
