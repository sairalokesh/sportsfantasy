package com.sports.fantasy.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_temp_participants")
public class UserTempParticipants {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "question_id")
  private Long questionId;

  @Column(name = "amount_id")
  private Long amountId;

  @Column(name = "participants")
  private String participants;

  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "capitan_id")
  private Long capitanId;

  @Column(name = "vice_capitan_id")
  private Long viceCapitanId;

  @Column(name = "supporter_id")
  private Long suppoterId;

  @Column(name = "game_type")
  private String gameType;

  @Column(name = "user_team_id")
  private Long userTeamId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public Long getAmountId() {
    return amountId;
  }

  public void setAmountId(Long amountId) {
    this.amountId = amountId;
  }

  public String getParticipants() {
    return participants;
  }

  public void setParticipants(String participants) {
    this.participants = participants;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
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

  public String getGameType() {
    return gameType;
  }

  public void setGameType(String gameType) {
    this.gameType = gameType;
  }

  public Long getUserTeamId() {
    return userTeamId;
  }

  public void setUserTeamId(Long userTeamId) {
    this.userTeamId = userTeamId;
  }

}
