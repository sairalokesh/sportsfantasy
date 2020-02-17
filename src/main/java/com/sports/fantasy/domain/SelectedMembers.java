package com.sports.fantasy.domain;

import java.util.ArrayList;
import java.util.List;

public class SelectedMembers {

  private List<Long> selectedMembers = new ArrayList<>();
  private Long questionId;
  private Long amountId;
  private String gameType;
  private Long capitanId;
  private Long viceCapitanId;
  private Long suppoterId;
  private Long userTempId;
  private String orderId;
  private String message;
  private Integer index;
  private Long userTeamId;

  public SelectedMembers() {
    super();
  }

  public SelectedMembers(Long questionId, Long amountId, String gameType) {
    super();
    this.questionId = questionId;
    this.amountId = amountId;
    this.gameType = gameType;
  }

  public List<Long> getSelectedMembers() {
    return selectedMembers;
  }

  public void setSelectedMembers(List<Long> selectedMembers) {
    this.selectedMembers = selectedMembers;
  }

  public Long getAmountId() {
    return amountId;
  }

  public void setAmountId(Long amountId) {
    this.amountId = amountId;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public String getGameType() {
    return gameType;
  }

  public void setGameType(String gameType) {
    this.gameType = gameType;
  }

  public Long getCapitanId() {
    return capitanId;
  }

  public void setCapitanId(Long capitanId) {
    this.capitanId = capitanId;
  }

  public Long getViceCapitanId() {
    return viceCapitanId;
  }

  public void setViceCapitanId(Long viceCapitanId) {
    this.viceCapitanId = viceCapitanId;
  }

  public Long getSuppoterId() {
    return suppoterId;
  }

  public void setSuppoterId(Long suppoterId) {
    this.suppoterId = suppoterId;
  }

  public Long getUserTempId() {
    return userTempId;
  }

  public void setUserTempId(Long userTempId) {
    this.userTempId = userTempId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public Long getUserTeamId() {
    return userTeamId;
  }

  public void setUserTeamId(Long userTeamId) {
    this.userTeamId = userTeamId;
  }
}
