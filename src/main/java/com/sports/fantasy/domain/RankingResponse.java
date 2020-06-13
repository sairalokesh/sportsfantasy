package com.sports.fantasy.domain;

import java.io.Serializable;
import java.util.List;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameQuestions;

public class RankingResponse implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -8004546620029511831L;
  private List<Ranking> rankings;
  private GameQuestions gameQuestion;
  private AmountEntries amountEntry;

  public List<Ranking> getRankings() {
    return rankings;
  }

  public void setRankings(List<Ranking> rankings) {
    this.rankings = rankings;
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
