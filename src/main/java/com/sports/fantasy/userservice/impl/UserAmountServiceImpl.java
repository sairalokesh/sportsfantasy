package com.sports.fantasy.userservice.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.sports.fantasy.domain.Response;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTempAmount;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.repository.UserAmountRepository;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserTempAmountService;
import com.sports.fantasy.userservice.UserTransactionService;
import com.sports.fantasy.util.AlphaNumericUtil;

@Service
@Transactional
public class UserAmountServiceImpl implements UserAmountService {

  @Value("${com.payment.gateway.appId}")
  private String appId;
  @Value("${com.payment.gateway.secretKey}")
  private String secretKey;
  @Value("${com.website.url}")
  private String websiteurl;

  @Autowired
  private UserAmountRepository userAmountRepository;
  @Autowired
  private UserTempAmountService userTempAmountService;
  @Autowired
  private UserTransactionService userTransactionService;


  @Override
  public UserAmount saveUserAmount(Long userId) {
    UserAmount userAmount = new UserAmount();
    userAmount.setBonusAmount(100);
    userAmount.setAddedAmount(0);
    userAmount.setUser(new UserInfo(userId));
    return userAmountRepository.save(userAmount);
  }

  @Override
  public UserAmount getUserAmount(Long userId) {
    return userAmountRepository.getUserAmount(userId);
  }

  @Override
  public void addAmount(UserAmount userAmount, UserInfo user, String returnUrl, Model model)
      throws NoSuchAlgorithmException, InvalidKeyException {
    String orderId = AlphaNumericUtil.getOrderNumber();
    UserTempAmount dbUserAmount = userTempAmountService.saveUserTempAmount(userAmount, orderId);
    if (dbUserAmount != null) {
      Map<String, String> parameters = new HashMap<>();
      parameters.put("appId", appId);
      parameters.put("orderId", orderId);
      parameters.put("orderAmount", userAmount.getAddedAmount() + "");
      parameters.put("orderCurrency", "INR");
      parameters.put("customerName", user.getFirstName() + " " + user.getLastName());
      parameters.put("customerEmail", user.getEmail());
      parameters.put("customerPhone", user.getPhoneNumber());
      parameters.put("returnUrl", websiteurl + returnUrl);
      StringBuilder data = new StringBuilder();
      SortedSet<String> keys = new TreeSet<>(parameters.keySet());
      for (String key : keys) {
        data.append(key + parameters.get(key));
      }
      Mac sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretkeyspec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
      sha256HMAC.init(secretkeyspec);
      String signature = Base64.getEncoder().encodeToString(sha256HMAC.doFinal(data.toString().getBytes()));
      model.addAttribute("appId", appId);
      model.addAttribute("orderId", orderId);
      model.addAttribute("orderAmount", userAmount.getAddedAmount() + "");
      model.addAttribute("orderCurrency", "INR");
      model.addAttribute("customerName", user.getFirstName() + " " + user.getLastName());
      model.addAttribute("customerEmail", user.getEmail());
      model.addAttribute("customerPhone", user.getPhoneNumber());
      model.addAttribute("returnUrl", websiteurl + returnUrl);
      model.addAttribute("signature", signature);
    }
  }

  @Override
  public Response saveUserAmountTransactions(HttpServletRequest request) {
    Response response = new Response();
    Map<String, String[]> mapData = request.getParameterMap();
    TreeMap<String, String> parameters = new TreeMap<>();
    mapData.forEach((key, val) -> parameters.put(key, val[0]));
    String orderId = request.getParameter("orderId");
    UserTempAmount userTempAmount = userTempAmountService.getUserTempAmount(orderId);
    if (userTempAmount != null && userTempAmount.getUserId() != null) {
      try {
        String txStatus = request.getParameter("txStatus");
        if (txStatus.equals("SUCCESS")) {
          UserAmount userAmount = userAmountRepository.getUserAmount(userTempAmount.getUserId());
          if (userAmount != null) {
            double addedamount = userAmount.getAddedAmount() + userTempAmount.getAmount();
            userAmount.setAddedAmount(addedamount);
            UserAmount dbUserAmount = userAmountRepository.save(userAmount);
            if (dbUserAmount != null && dbUserAmount.getId() != null) {
              UserTransactions userTransactions = getUserTransactions(parameters, userTempAmount);
              if (userTransactions != null) {
                userTransactionService.saveUserAmountTransaction(userTransactions);
                deleteTempUserSelectionAmount(userTempAmount, orderId);
                response.setMessage("success");
                return response;
              }
            }
          }

        } else {
          deleteTempUserSelectionAmount(userTempAmount, orderId);
          response.setMessage("failure");
          response.setUserId(userTempAmount.getUserId());
          return response;
        }
      } catch (Exception e) {
        deleteTempUserSelectionAmount(userTempAmount, orderId);
        response.setMessage("failure");
        response.setUserId(userTempAmount.getUserId());
        return response;
      }
    }
    return null;
  }

  private UserTransactions getUserTransactions(TreeMap<String, String> parameters,
      UserTempAmount userTempAmount) {
    UserTransactions transactions = new UserTransactions();
    transactions.setUser(new UserInfo(userTempAmount.getUserId()));
    transactions.setTransactionType("Payment");
    if (parameters.containsKey("referenceId")) {
      transactions.setTransactionId(parameters.get("referenceId"));
    }
    if (parameters.containsKey("paymentMode")) {
      transactions.setPaymentMode(parameters.get("paymentMode"));
    }
    if (parameters.containsKey("orderAmount")) {
      transactions.setTransactionAmount(parameters.get("orderAmount"));
    }
    if (parameters.containsKey("txTime")) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        Date date = dateFormat.parse(parameters.get("txTime"));
        transactions.setTransactionDate(date);
      } catch (ParseException e) {

      }

    }

    return transactions;
  }

  private void deleteTempUserSelectionAmount(UserTempAmount dbUserTempAmount, String orderId) {
    if (dbUserTempAmount != null && dbUserTempAmount.getId() != null) {
      userTempAmountService.deleteTempUserSelectedAmountById(dbUserTempAmount.getId());
    } else {
      userTempAmountService.deleteTempUserSelectedAmount(orderId);
    }
  }

  @Override
  public UserAmount updateUserAmount(UserAmount userAmount) {
    return userAmountRepository.save(userAmount);
  }
}
