package com.sports.fantasy.userservice;

import org.json.simple.parser.ParseException;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserWithdrawAmount;

public interface PaymentService {

  PaymentResponse clientAuth(String clientId, String clientSecret, String stage) throws ParseException;
  PaymentResponse addBeneficiary(UserAccount userAccount, String token, String stage) throws ParseException;
  PaymentResponse requestTransfer(UserWithdrawAmount userWithdrawAmount, UserAccount userAccount, String token, String stage) throws ParseException;

}
