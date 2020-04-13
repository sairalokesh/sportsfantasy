package com.sports.fantasy.domain;

public class PaymentResponse {

  private String responseStatus;
  private String reponseMessage;
  private String token;
  private String benefId;
  private String transferId;

  public String getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
  }

  public String getReponseMessage() {
    return reponseMessage;
  }

  public void setReponseMessage(String reponseMessage) {
    this.reponseMessage = reponseMessage;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBenefId() {
    return benefId;
  }

  public void setBenefId(String benefId) {
    this.benefId = benefId;
  }

  public String getTransferId() {
    return transferId;
  }

  public void setTransferId(String transferId) {
    this.transferId = transferId;
  }



}
