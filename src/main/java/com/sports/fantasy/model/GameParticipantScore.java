package com.sports.fantasy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "game_participant_score")
public class GameParticipantScore {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private GameQuestions gameQuestions;

  @ManyToOne
  @JoinColumn(name = "participant_id")
  private GameParticipants gameParticipants;

  @Column(name = "score")
  private double score;

  @Column(name = "captain_score")
  private double captainScore;

  @Column(name = "vice_captain_score")
  private double viceCaptainScore;

  @Column(name = "suppoter_score")
  private double suppoterScore;

  @Transient
  private boolean captain;
  @Transient
  private boolean viceCaptain;
  @Transient
  private boolean suppoter;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GameQuestions getGameQuestions() {
    return gameQuestions;
  }

  public void setGameQuestions(GameQuestions gameQuestions) {
    this.gameQuestions = gameQuestions;
  }

  public GameParticipants getGameParticipants() {
    return gameParticipants;
  }

  public void setGameParticipants(GameParticipants gameParticipants) {
    this.gameParticipants = gameParticipants;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public double getCaptainScore() {
    return captainScore;
  }

  public void setCaptainScore(double captainScore) {
    this.captainScore = captainScore;
  }

  public double getViceCaptainScore() {
    return viceCaptainScore;
  }

  public void setViceCaptainScore(double viceCaptainScore) {
    this.viceCaptainScore = viceCaptainScore;
  }

  public double getSuppoterScore() {
    return suppoterScore;
  }

  public void setSuppoterScore(double suppoterScore) {
    this.suppoterScore = suppoterScore;
  }

  public boolean isCaptain() {
    return captain;
  }

  public void setCaptain(boolean captain) {
    this.captain = captain;
  }

  public boolean isViceCaptain() {
    return viceCaptain;
  }

  public void setViceCaptain(boolean viceCaptain) {
    this.viceCaptain = viceCaptain;
  }

  public boolean isSuppoter() {
    return suppoter;
  }

  public void setSuppoter(boolean suppoter) {
    this.suppoter = suppoter;
  }



}
