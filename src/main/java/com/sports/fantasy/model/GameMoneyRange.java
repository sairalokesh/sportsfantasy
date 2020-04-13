package com.sports.fantasy.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "game_money_range")
public class GameMoneyRange implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6886135503901133870L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @Column(name = "person_from")
  private Integer personFrom;

  @Column(name = "person_to")
  private Integer personTo;

  @Column(name = "amount")
  private double amount;

  @ManyToOne
  @JoinColumn(name = "game_money_id")
  private GameMoney gameMoney;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPersonFrom() {
    return personFrom;
  }

  public void setPersonFrom(Integer personFrom) {
    this.personFrom = personFrom;
  }

  public Integer getPersonTo() {
    return personTo;
  }

  public void setPersonTo(Integer personTo) {
    this.personTo = personTo;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public GameMoney getGameMoney() {
    return gameMoney;
  }

  public void setGameMoney(GameMoney gameMoney) {
    this.gameMoney = gameMoney;
  }
}
