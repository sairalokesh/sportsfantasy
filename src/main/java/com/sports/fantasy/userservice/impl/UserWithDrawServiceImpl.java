package com.sports.fantasy.userservice.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.model.UserWithdrawAmount;
import com.sports.fantasy.repository.UserWithDrawRepository;
import com.sports.fantasy.userservice.PaymentService;
import com.sports.fantasy.userservice.UserTransactionService;
import com.sports.fantasy.userservice.UserWithDrawService;

@Service
@Transactional
public class UserWithDrawServiceImpl implements UserWithDrawService {

  @Value("${com.payout.clientId}")
  private String clientId;
  @Value("${com.payout.clientSecret}")
  private String clientSecret;
  @Value("${com.payout.mode}")
  private String stage;

  @Autowired
  private UserWithDrawRepository userWithDrawRepository;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private UserTransactionService userTransactionService;

  @Override
  public UserWithdrawAmount saveUSerWithDrawAMount(UserWithdrawAmount userWithdrawAmount) {
    return userWithDrawRepository.save(userWithdrawAmount);
  }

  @Override
  public List<UserWithdrawAmount> getUserWithDraws(Long userId) {
    return userWithDrawRepository.getUserWithDraws(userId);
  }

  @Override
  public List<UserWithdrawAmount> getPendingWithDraws(String status) {
    return userWithDrawRepository.getPendingWithDraws(status);
  }

  @Override
  public UserWithdrawAmount findByWithdrawId(Long withdrawId) {
    Optional<UserWithdrawAmount> userWithdrawAmount = userWithDrawRepository.findById(withdrawId);
    if (userWithdrawAmount.isPresent()) {
      return userWithdrawAmount.get();
    }
    return null;
  }

  @Override
  public PaymentResponse amountSettlement(UserWithdrawAmount userWithdrawAmount, UserAccount userAccount) {
    PaymentResponse paymentRespopnse = new PaymentResponse();
    try {
      PaymentResponse paymentTokenRespopnse = paymentService.clientAuth(clientId, clientSecret, stage);
        if(paymentTokenRespopnse!=null && StringUtils.hasText(paymentTokenRespopnse.getResponseStatus()) && paymentTokenRespopnse.getResponseStatus().equals("200")) {
            if(StringUtils.hasText(paymentTokenRespopnse.getToken())) {
              PaymentResponse paymentSettlementResponse = paymentService.requestTransfer(userWithdrawAmount, userAccount, paymentTokenRespopnse.getToken(), stage);
                if(paymentSettlementResponse!=null && StringUtils.hasText(paymentSettlementResponse.getResponseStatus()) && paymentSettlementResponse.getResponseStatus().equals("200")) {
                    if(StringUtils.hasText(paymentSettlementResponse.getTransferId())) {
                      UserTransactions userTransactions = new UserTransactions();
                      userTransactions.setTransactionId(paymentSettlementResponse.getTransferId());
                      userTransactions.setPaymentMode("NET_BANKING");
                      userTransactions.setTransactionAmount(userWithdrawAmount.getAmount()+"");
                      userTransactions.setTransactionDate(new Date());
                      userTransactions.setTransactionType("Withdraws");
                      userTransactions.setUser(userWithdrawAmount.getUser());
                      userTransactionService.saveUserAmountTransaction(userTransactions);
                      userWithdrawAmount.setStatus("Done");
                      userWithDrawRepository.save(userWithdrawAmount);
                      return paymentSettlementResponse;
                    }
                } else {
                    return paymentSettlementResponse;
                }
            }
        }else {
            return paymentTokenRespopnse;
        }
        
    } catch (Exception e) {
        paymentRespopnse.setResponseStatus("400");
        paymentRespopnse.setReponseMessage(e.getMessage());
        return paymentRespopnse;
    }
    return null;
  }

}
