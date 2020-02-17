package com.sports.fantasy.domain;

import java.util.Date;

public class SelectedTeam {

  private Long amountId;
  private String amount;
  private Long questionId;
  private String teamOne;
  private String teamOneImage;
  private String teamTwo;
  private String teamTwoImage;
  private Long teamCount;
  private Long userTeamId;
  private String questionEffect;
  private Date validDate;

  public SelectedTeam() {
    super();
  }

  public SelectedTeam(Long amountId, String amount, Long questionId, String teamOne,
      String teamOneImage, String teamTwo, String teamTwoImage, Long teamCount, Long userTeamId,
      String questionEffect, Date validDate) {
    super();
    this.amountId = amountId;
    this.amount = amount;
    this.questionId = questionId;
    this.teamOne = teamOne;
    this.teamOneImage = teamOneImage;
    this.teamTwo = teamTwo;
    this.teamTwoImage = teamTwoImage;
    this.teamCount = teamCount;
    this.userTeamId = userTeamId;
    this.questionEffect = questionEffect;
    this.validDate = validDate;
  }

  public Long getAmountId() {
    return amountId;
  }

  public void setAmountId(Long amountId) {
    this.amountId = amountId;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public String getTeamOne() {
    return teamOne;
  }

  public void setTeamOne(String teamOne) {
    this.teamOne = teamOne;
  }

  public String getTeamOneImage() {
    return teamOneImage;
  }

  public void setTeamOneImage(String teamOneImage) {
    this.teamOneImage = teamOneImage;
  }

  public String getTeamTwo() {
    return teamTwo;
  }

  public void setTeamTwo(String teamTwo) {
    this.teamTwo = teamTwo;
  }

  public String getTeamTwoImage() {
    return teamTwoImage;
  }

  public void setTeamTwoImage(String teamTwoImage) {
    this.teamTwoImage = teamTwoImage;
  }

  public Long getTeamCount() {
    return teamCount;
  }

  public void setTeamCount(Long teamCount) {
    this.teamCount = teamCount;
  }

  public Long getUserTeamId() {
    return userTeamId;
  }

  public void setUserTeamId(Long userTeamId) {
    this.userTeamId = userTeamId;
  }

  public String getQuestionEffect() {
    return questionEffect;
  }

  public void setQuestionEffect(String questionEffect) {
    this.questionEffect = questionEffect;
  }

  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
  }

}
