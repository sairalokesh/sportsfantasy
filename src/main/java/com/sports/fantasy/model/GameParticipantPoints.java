package com.sports.fantasy.model;

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
@Table(name = "game_participant_points")
public class GameParticipantPoints {

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

  @Column(name = "runs")
  private Integer runs = 0;

  @Column(name = "wickets")
  private Integer wickets = 0;

  @Column(name = "sixes")
  private Integer sixes = 0;

  @Column(name = "fours")
  private Integer fours = 0;

  @Column(name = "maidens")
  private Integer maidens = 0;

  @Column(name = "catches")
  private Integer catches = 0;

  @Column(name = "fifties")
  private Integer fifties = 0;

  @Column(name = "hundries")
  private Integer hundries = 0;

  @Column(name = "runouts")
  private Integer runouts = 0;

  @Column(name = "bowleds")
  private Integer bowleds = 0;

  @Column(name = "stumpeds")
  private Integer stumpeds = 0;

  @Column(name = "lbws")
  private Integer lbws = 0;

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

  public Integer getRuns() {
    return runs;
  }

  public void setRuns(Integer runs) {
    this.runs = runs;
  }

  public Integer getWickets() {
    return wickets;
  }

  public void setWickets(Integer wickets) {
    this.wickets = wickets;
  }

  public Integer getSixes() {
    return sixes;
  }

  public void setSixes(Integer sixes) {
    this.sixes = sixes;
  }

  public Integer getFours() {
    return fours;
  }

  public void setFours(Integer fours) {
    this.fours = fours;
  }

  public Integer getMaidens() {
    return maidens;
  }

  public void setMaidens(Integer maidens) {
    this.maidens = maidens;
  }

  public Integer getCatches() {
    return catches;
  }

  public void setCatches(Integer catches) {
    this.catches = catches;
  }

  public Integer getFifties() {
    return fifties;
  }

  public void setFifties(Integer fifties) {
    this.fifties = fifties;
  }

  public Integer getHundries() {
    return hundries;
  }

  public void setHundries(Integer hundries) {
    this.hundries = hundries;
  }

  public Integer getRunouts() {
    return runouts;
  }

  public void setRunouts(Integer runouts) {
    this.runouts = runouts;
  }

  public Integer getBowleds() {
    return bowleds;
  }

  public void setBowleds(Integer bowleds) {
    this.bowleds = bowleds;
  }

  public Integer getStumpeds() {
    return stumpeds;
  }

  public void setStumpeds(Integer stumpeds) {
    this.stumpeds = stumpeds;
  }

  public Integer getLbws() {
    return lbws;
  }

  public void setLbws(Integer lbws) {
    this.lbws = lbws;
  }
}
