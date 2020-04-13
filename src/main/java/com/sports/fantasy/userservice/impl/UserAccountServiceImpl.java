package com.sports.fantasy.userservice.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.repository.UserAccountRepository;
import com.sports.fantasy.userservice.PaymentService;
import com.sports.fantasy.userservice.UserAccountService;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

  @Value("${com.payout.clientId}")
  private String clientId;
  @Value("${com.payout.clientSecret}")
  private String clientSecret;
  @Value("${com.payout.mode}")
  private String stage;

  @Autowired
  private UserAccountRepository userAccountRepository;
  @Autowired
  private PaymentService paymentService;

  @Override
  public UserAccount getUserAccountInfo(Long userId) {
    return userAccountRepository.getUserAccountInfo(userId);
  }

  @Override
  public UserAccount getAccountByAccountId(String accountNumber) {
    return userAccountRepository.getAccountByAccountId(accountNumber);
  }

  @Override
  public UserAccount updateUserAccount(UserAccount account) {
    return userAccountRepository.save(account);
  }

  @Override
  public List<UserAccount> getpendingaccounts() {
    return userAccountRepository.getpendingaccounts("Not_Verified");
  }

  @Override
  public UserAccount findByAccountId(Long accountId) {
    Optional<UserAccount> userAccount = userAccountRepository.findById(accountId);
    if (userAccount.isPresent()) {
      return userAccount.get();
    }
    return null;
  }

  @Override
  public PaymentResponse verifyaccount(UserAccount userAccount) {
    PaymentResponse paymentResponse = new PaymentResponse();
    try {
      PaymentResponse paymentTokenRespopnse = paymentService.clientAuth(clientId, clientSecret, stage);
      if (paymentTokenRespopnse != null
          && StringUtils.hasText(paymentTokenRespopnse.getResponseStatus())
          && paymentTokenRespopnse.getResponseStatus().equals("200")) {
        if (StringUtils.hasText(paymentTokenRespopnse.getToken())) {
          /*
           * PaymentRespopnse paymentAddBeficientResponse =
           * PaymentService.bankDetailsValidation(userAccount, paymentTokenRespopnse.getToken(),
           * stage); if(paymentAddBeficientResponse!=null) {
           */
          PaymentResponse paymentAddBenefIdResponse = paymentService.addBeneficiary(userAccount, paymentTokenRespopnse.getToken(), stage);
          if (paymentAddBenefIdResponse != null
              && StringUtils.hasText(paymentAddBenefIdResponse.getResponseStatus())
              && paymentAddBenefIdResponse.getResponseStatus().equals("200")) {
            if (StringUtils.hasText(paymentAddBenefIdResponse.getBenefId())) {
              userAccount.setStatus("Verified");
              userAccount.setBeneId(paymentAddBenefIdResponse.getBenefId());
              userAccount.setVerifyMessage(paymentAddBenefIdResponse.getReponseMessage());
              userAccountRepository.save(userAccount);
              return paymentAddBenefIdResponse;
            }
          } else {
            return paymentAddBenefIdResponse;
          }
          /*
           * } else { return paymentAddBeficientResponse; }
           */
        }
      } else {
        return paymentTokenRespopnse;
      }

    } catch (Exception e) {
      paymentResponse.setResponseStatus("400");
      paymentResponse.setReponseMessage(e.getMessage());
      return paymentResponse;
    }

    return null;
  }

}
