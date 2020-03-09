package com.sports.fantasy.domain;

import java.util.List;
import java.util.Map;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;

public class GameParticipantsData {
  private GameQuestions gameQuestion;
  private Map<String, List<GameParticipants>> gameParticipants;
  private AmountEntries amountEntries;
  private SelectedMembers selectedMembers;
  private List<String> gameTypes;
  private Map<String, Integer> gametypecounts;
  private Integer gamecount = 0;
  private Map<String, Integer> typecount;
  private String gameType;
  private Long questionId;
  private Long amountId;
  private Long userTempId;
  private double bonusAmount = 0.00;
  private double cashAmount = 0.00;
  private String message;
  private String lastCountry;

  /**
   * @return the gameQuestion
   */
  public GameQuestions getGameQuestion() {
    return gameQuestion;
  }

  /**
   * @param gameQuestion the gameQuestion to set
   */
  public void setGameQuestion(GameQuestions gameQuestion) {
    this.gameQuestion = gameQuestion;
  }

  /**
   * @return the gameParticipants
   */
  public Map<String, List<GameParticipants>> getGameParticipants() {
    return gameParticipants;
  }

  /**
   * @param gameParticipants the gameParticipants to set
   */
  public void setGameParticipants(Map<String, List<GameParticipants>> gameParticipants) {
    this.gameParticipants = gameParticipants;
  }

  /**
   * @return the selectedMembers
   */
  public SelectedMembers getSelectedMembers() {
    return selectedMembers;
  }

  /**
   * @param selectedMembers the selectedMembers to set
   */
  public void setSelectedMembers(SelectedMembers selectedMembers) {
    this.selectedMembers = selectedMembers;
  }

  /**
   * @return the gameTypes
   */
  public List<String> getGameTypes() {
    return gameTypes;
  }

  /**
   * @param gameTypes the gameTypes to set
   */
  public void setGameTypes(List<String> gameTypes) {
    this.gameTypes = gameTypes;
  }

  /**
   * @return the gametypecounts
   */
  public Map<String, Integer> getGametypecounts() {
    return gametypecounts;
  }

  /**
   * @param gametypecounts the gametypecounts to set
   */
  public void setGametypecounts(Map<String, Integer> gametypecounts) {
    this.gametypecounts = gametypecounts;
  }

  /**
   * @return the gamecount
   */
  public Integer getGamecount() {
    return gamecount;
  }

  /**
   * @param gamecount the gamecount to set
   */
  public void setGamecount(Integer gamecount) {
    this.gamecount = gamecount;
  }

  /**
   * @return the typecount
   */
  public Map<String, Integer> getTypecount() {
    return typecount;
  }

  /**
   * @param typecount the typecount to set
   */
  public void setTypecount(Map<String, Integer> typecount) {
    this.typecount = typecount;
  }

  /**
   * @return the gameType
   */
  public String getGameType() {
    return gameType;
  }

  /**
   * @param gameType the gameType to set
   */
  public void setGameType(String gameType) {
    this.gameType = gameType;
  }

  /**
   * @return the questionId
   */
  public Long getQuestionId() {
    return questionId;
  }

  /**
   * @param questionId the questionId to set
   */
  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  /**
   * @return the amountId
   */
  public Long getAmountId() {
    return amountId;
  }

  /**
   * @param amountId the amountId to set
   */
  public void setAmountId(Long amountId) {
    this.amountId = amountId;
  }

  /**
   * @return the userTempId
   */
  public Long getUserTempId() {
    return userTempId;
  }

  /**
   * @param userTempId the userTempId to set
   */
  public void setUserTempId(Long userTempId) {
    this.userTempId = userTempId;
  }

  /**
   * @return the amountEntries
   */
  public AmountEntries getAmountEntries() {
    return amountEntries;
  }

  /**
   * @param amountEntries the amountEntries to set
   */
  public void setAmountEntries(AmountEntries amountEntries) {
    this.amountEntries = amountEntries;
  }

  /**
   * @return the bonusAmount
   */
  public double getBonusAmount() {
    return bonusAmount;
  }

  /**
   * @param bonusAmount the bonusAmount to set
   */
  public void setBonusAmount(double bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  /**
   * @return the cashAmount
   */
  public double getCashAmount() {
    return cashAmount;
  }

  /**
   * @param cashAmount the cashAmount to set
   */
  public void setCashAmount(double cashAmount) {
    this.cashAmount = cashAmount;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  public String getLastCountry() {
    return lastCountry;
  }

  public void setLastCountry(String lastCountry) {
    this.lastCountry = lastCountry;
  }



}
