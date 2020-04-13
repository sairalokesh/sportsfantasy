package com.sports.fantasy.userservice.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.PaymentResponse;
import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.model.UserWithdrawAmount;
import com.sports.fantasy.userservice.PaymentService;


@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

  @Override
  public PaymentResponse clientAuth(String clientId, String clientSecret, String stage)
      throws ParseException {
    PaymentResponse paymentRespopnse = new PaymentResponse();
    HashMap<String, String> headers = new HashMap<String, String>();
    String postData = "";
    headers.put("X-Client-Id", clientId);
    headers.put("X-Client-Secret", clientSecret);
    String link = "";
    if (stage.equalsIgnoreCase("TEST")) {
      link = "https://payout-gamma.cashfree.com/payout/v1/authorize";
    } else if (stage.equalsIgnoreCase("PROD")) {
      link = "https://payout-api.cashfree.com/payout/v1/authorize";
    }

    String response = makePostCall(link, headers, postData);
    JSONParser parser = new JSONParser();
    JSONObject responseObj = (JSONObject) parser.parse(response);
    String status = (String) responseObj.get("status");
    String message = ((String) responseObj.get("message"));
    if (status.equals("ERROR")) {
      paymentRespopnse.setResponseStatus("400");
      paymentRespopnse.setReponseMessage(message);
      return paymentRespopnse;
    } else {
      String token = (String) ((JSONObject) responseObj.get("data")).get("token");
      paymentRespopnse.setResponseStatus("200");
      paymentRespopnse.setReponseMessage(message);
      paymentRespopnse.setToken(token);
      return paymentRespopnse;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public PaymentResponse addBeneficiary(UserAccount userAccount, String token, String stage)
      throws ParseException {
    PaymentResponse paymentResponse = new PaymentResponse();
    if (userAccount == null || userAccount.getUser() == null) {
      paymentResponse.setResponseStatus("400");
      paymentResponse.setReponseMessage("Mandatory parameters missing");
      return paymentResponse;
    } else if (!StringUtils.hasText(userAccount.getUser().getFirstName())
        || !StringUtils.hasText(userAccount.getUser().getLastName())
        || !StringUtils.hasText(userAccount.getUser().getPhoneNumber())
        || !StringUtils.hasText(userAccount.getAccountNumber())
        || !StringUtils.hasText(userAccount.getIfscCode())) {
      paymentResponse.setResponseStatus("400");
      paymentResponse.setReponseMessage("Mandatory parameters missing");
      return paymentResponse;
    } else {
      HashMap<String, String> userParam = new HashMap<String, String>();
      String benefId = getBenefId(userAccount.getUser().getFirstName());
      userParam.put("beneId", benefId);
      userParam.put("name", userAccount.getCardHolderName());
      userParam.put("email", userAccount.getUser().getEmail());
      userParam.put("phone", userAccount.getUser().getPhoneNumber());
      userParam.put("bankAccount", userAccount.getAccountNumber());
      userParam.put("ifsc", userAccount.getIfscCode());
      userParam.put("address1", "Guntakrinda palli, srikalahasti, chittoor, andhra pradesh");

      JSONObject userParamJson = new JSONObject();
      userParamJson.putAll(userParam);

      HashMap<String, String> headers = new HashMap<String, String>();
      headers.put("Content-Type", "application/json");
      headers.put("Authorization", "Bearer " + token);
      String link = "";
      if (stage.equals("TEST")) {
        link = "https://payout-gamma.cashfree.com/payout/v1/addBeneficiary";
      } else if (stage.equals("PROD")) {
        link = "https://payout-api.cashfree.com/payout/v1/addBeneficiary";
      }

      String response = makePostCall(link, headers, userParamJson.toString());
      JSONParser parser = new JSONParser();
      JSONObject responseObj = (JSONObject) parser.parse(response);
      String status = (String) responseObj.get("status");
      String message = ((String) responseObj.get("message"));
      if (status.equals("ERROR")) {
        paymentResponse.setResponseStatus("400");
        paymentResponse.setReponseMessage(message);
        return paymentResponse;
      } else {
        paymentResponse.setResponseStatus("200");
        paymentResponse.setReponseMessage(message);
        paymentResponse.setBenefId(benefId);
        return paymentResponse;
      }

    }

  }
  
  @SuppressWarnings("unchecked")
  @Override
  public PaymentResponse requestTransfer(UserWithdrawAmount userWithdrawAmount, UserAccount userAccount, String token, String stage) throws ParseException {
    PaymentResponse paymentRespopnse = new PaymentResponse();
    if (userAccount == null || userWithdrawAmount == null){
        paymentRespopnse.setResponseStatus("400");
        paymentRespopnse.setReponseMessage("Mandatory parameters missing");
        return paymentRespopnse;
    } else if (userWithdrawAmount.getAmount() <= 0 || !StringUtils.hasText(userAccount.getBeneId())) {
        paymentRespopnse.setResponseStatus("400");
        paymentRespopnse.setReponseMessage("Mandatory parameters missing");
        return paymentRespopnse;
    }
    else{
        String transferId = getTransactionId();
        HashMap<String,String> userRequestParam =new HashMap<String,String>();
        userRequestParam.put("beneId", userAccount.getBeneId());
        userRequestParam.put("amount", userWithdrawAmount.getAmount()+"");
        userRequestParam.put("transferId", transferId);
        userRequestParam.put("transferMode", "banktransfer");
        userRequestParam.put("remarks", "Amount Settlement");

        JSONObject userRequestParamJson = new JSONObject();
        userRequestParamJson.putAll(userRequestParam);

        HashMap<String,String> headers =new HashMap<String,String>();
        headers.put("Authorization","Bearer " + token);
        String link = "";
        if (stage.equals("TEST")) {
            link = "https://payout-gamma.cashfree.com/payout/v1/requestTransfer";
        }
        else if (stage.equals("PROD")){
            link = "https://payout-api.cashfree.com/payout/v1/requestTransfer";
        }

        
        String response = makePostCall(link, headers, userRequestParamJson.toString());

        JSONParser parser = new JSONParser();
        JSONObject responseObj = (JSONObject) parser.parse(response);
        String status = (String) responseObj.get("status");
        String message = ((String)responseObj.get("message"));
        if (status.equals("ERROR")){
            paymentRespopnse.setResponseStatus("400");
            paymentRespopnse.setReponseMessage(message);
            return paymentRespopnse;
        }
        else {
            paymentRespopnse.setResponseStatus("200");
            paymentRespopnse.setReponseMessage(message);
            paymentRespopnse.setTransferId(transferId);
            return paymentRespopnse;
        }
    }
  }

  private String makePostCall(String link, Map<String, String> headers, String postData) {
    String response = "";
    try {
      URL myURL = new URL(link);
      HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
      for (Map.Entry<String, String> m : headers.entrySet()) {
        conn.setRequestProperty(m.getKey().toString(), m.getValue().toString());
      }

      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Content-Length", "" + postData.getBytes().length);
      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);

      OutputStream os = conn.getOutputStream();
      os.write(postData.getBytes());
      os.flush();

      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

      String output;
      while ((output = br.readLine()) != null) {
        response += output;
      }

      conn.disconnect();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

  private String getBenefId(String firstName) {
    Random randomGenerator = new Random();
    String orderId = firstName + "_" + randomGenerator.nextInt(1000000);
    return orderId;
  }
  
  private String getTransactionId() {
    Random r = new Random(System.currentTimeMillis());
      String number = (1000000000 + r.nextInt(2000000000))+"";
      String transId = number.replaceAll("-", "");
      return transId;
}
}
