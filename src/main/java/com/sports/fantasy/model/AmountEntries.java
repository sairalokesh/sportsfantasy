package com.sports.fantasy.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "amount_entries")
public class AmountEntries implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2794868281823253523L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @Column(name = "amount")
  private String amount;

  @Column(name = "effect")
  private String effect;

  @Column(name = "persons")
  private Integer persons;

  @Column(name = "is_active")
  private boolean active = true;

  @Column(name = "amount_description")
  private String amountDescription;

  public AmountEntries() {
    super();
  }

  public AmountEntries(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public Integer getPersons() {
    return persons;
  }

  public void setPersons(Integer persons) {
    this.persons = persons;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getAmountDescription() {
    return amountDescription;
  }

  public void setAmountDescription(String amountDescription) {
    this.amountDescription = amountDescription;
  }

}
