package com.sports.fantasy.model;

import java.util.Date;
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
@Table(name = "match_payments")
public class MatchPayments {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @Column(name = "match_name")
  private String matchName;

  @Column(name = "added_amount")
  private double addedAmount;

  @Column(name = "bonus_amount")
  private double bonusAmount;

  @Column(name = "amount_type")
  private String amountType;

  @Column(name = "transaction_date")
  private Date transactionDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo user;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMatchName() {
    return matchName;
  }

  public void setMatchName(String matchName) {
    this.matchName = matchName;
  }

  public double getAddedAmount() {
    return addedAmount;
  }

  public void setAddedAmount(double addedAmount) {
    this.addedAmount = addedAmount;
  }

  public double getBonusAmount() {
    return bonusAmount;
  }

  public void setBonusAmount(double bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  public UserInfo getUser() {
    return user;
  }

  public void setUser(UserInfo user) {
    this.user = user;
  }

  public String getAmountType() {
    return amountType;
  }

  public void setAmountType(String amountType) {
    this.amountType = amountType;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }


}
