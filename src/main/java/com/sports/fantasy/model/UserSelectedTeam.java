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
@Table(name = "user_selected_team")
public class UserSelectedTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo user;

  @ManyToOne
  @JoinColumn(name = "game_questions_id")
  private GameQuestions gameQuestions;

  @ManyToOne
  @JoinColumn(name = "amount_entries_id")
  private AmountEntries amountEntries;

  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "player_result")
  private String playerResult;

  @Column(name = "winning_amount")
  private double winningAmount;

  @Column(name = "payment_done")
  private String paymentDone = "Not_Done";

  @Column(name = "captain_id")
  private Long captainId;

  @Column(name = "vice_captain_id")
  private Long viceCaptainId;

  @Column(name = "suppoter_id")
  private Long suppoterId;

  @Column(name = "participant_one_id")
  private Long participantOneId;

  @Column(name = "participant_two_id")
  private Long participantTwoId;

  @Column(name = "participant_three_id")
  private Long participantThreeId;

  @Column(name = "participant_four_id")
  private Long participantFourId;

  @Column(name = "participant_five_id")
  private Long participantFiveId;

  @Column(name = "participant_six_id")
  private Long participantSixId;

  @Column(name = "participant_seven_id")
  private Long participantSevenId;

  @Column(name = "participant_eight_id")
  private Long participantEightId;

  @Column(name = "game_type")
  private String gameType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserInfo getUser() {
    return user;
  }

  public void setUser(UserInfo user) {
    this.user = user;
  }

  public GameQuestions getGameQuestions() {
    return gameQuestions;
  }

  public void setGameQuestions(GameQuestions gameQuestions) {
    this.gameQuestions = gameQuestions;
  }

  public AmountEntries getAmountEntries() {
    return amountEntries;
  }

  public void setAmountEntries(AmountEntries amountEntries) {
    this.amountEntries = amountEntries;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getPlayerResult() {
    return playerResult;
  }

  public void setPlayerResult(String playerResult) {
    this.playerResult = playerResult;
  }

  public double getWinningAmount() {
    return winningAmount;
  }

  public void setWinningAmount(double winningAmount) {
    this.winningAmount = winningAmount;
  }

  public String getPaymentDone() {
    return paymentDone;
  }

  public void setPaymentDone(String paymentDone) {
    this.paymentDone = paymentDone;
  }

  public Long getCaptainId() {
    return captainId;
  }

  public void setCaptainId(Long captainId) {
    this.captainId = captainId;
  }

  public Long getViceCaptainId() {
    return viceCaptainId;
  }

  public void setViceCaptainId(Long viceCaptainId) {
    this.viceCaptainId = viceCaptainId;
  }

  public Long getSuppoterId() {
    return suppoterId;
  }

  public void setSuppoterId(Long suppoterId) {
    this.suppoterId = suppoterId;
  }

  public Long getParticipantOneId() {
    return participantOneId;
  }

  public void setParticipantOneId(Long participantOneId) {
    this.participantOneId = participantOneId;
  }

  public Long getParticipantTwoId() {
    return participantTwoId;
  }

  public void setParticipantTwoId(Long participantTwoId) {
    this.participantTwoId = participantTwoId;
  }

  public Long getParticipantThreeId() {
    return participantThreeId;
  }

  public void setParticipantThreeId(Long participantThreeId) {
    this.participantThreeId = participantThreeId;
  }

  public Long getParticipantFourId() {
    return participantFourId;
  }

  public void setParticipantFourId(Long participantFourId) {
    this.participantFourId = participantFourId;
  }

  public Long getParticipantFiveId() {
    return participantFiveId;
  }

  public void setParticipantFiveId(Long participantFiveId) {
    this.participantFiveId = participantFiveId;
  }

  public Long getParticipantSixId() {
    return participantSixId;
  }

  public void setParticipantSixId(Long participantSixId) {
    this.participantSixId = participantSixId;
  }

  public Long getParticipantSevenId() {
    return participantSevenId;
  }

  public void setParticipantSevenId(Long participantSevenId) {
    this.participantSevenId = participantSevenId;
  }

  public Long getParticipantEightId() {
    return participantEightId;
  }

  public void setParticipantEightId(Long participantEightId) {
    this.participantEightId = participantEightId;
  }

  public String getGameType() {
    return gameType;
  }

  public void setGameType(String gameType) {
    this.gameType = gameType;
  }
}
