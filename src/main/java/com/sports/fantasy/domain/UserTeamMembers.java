package com.sports.fantasy.domain;

import java.io.Serializable;
import java.util.List;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserTeamParticipants;

public class UserTeamMembers implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1579759337584423068L;
  private List<UserTeamParticipants> userTeamParticipants;
  private GameQuestions gameQuestion;
  private AmountEntries amountEntry;

  public List<UserTeamParticipants> getUserTeamParticipants() {
    return userTeamParticipants;
  }

  public void setUserTeamParticipants(List<UserTeamParticipants> userTeamParticipants) {
    this.userTeamParticipants = userTeamParticipants;
  }

  public GameQuestions getGameQuestion() {
    return gameQuestion;
  }

  public void setGameQuestion(GameQuestions gameQuestion) {
    this.gameQuestion = gameQuestion;
  }

  public AmountEntries getAmountEntry() {
    return amountEntry;
  }

  public void setAmountEntry(AmountEntries amountEntry) {
    this.amountEntry = amountEntry;
  }



}
