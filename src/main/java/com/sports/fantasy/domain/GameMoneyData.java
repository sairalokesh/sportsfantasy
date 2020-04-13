package com.sports.fantasy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.sports.fantasy.model.GameMoneyRange;

public class GameMoneyData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5842042901249191654L;
  private List<GameMoneyRange> gameMoneyRanges = new ArrayList<>();
  private Integer persons;
  private Double amount;

  public List<GameMoneyRange> getGameMoneyRanges() {
    return gameMoneyRanges;
  }

  public void setGameMoneyRanges(List<GameMoneyRange> gameMoneyRanges) {
    this.gameMoneyRanges = gameMoneyRanges;
  }

  public Integer getPersons() {
    return persons;
  }

  public void setPersons(Integer persons) {
    this.persons = persons;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }



}
