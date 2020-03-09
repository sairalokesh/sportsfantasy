package com.sports.fantasy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users_selected_teams_score")
public class UserSelectedTeamsScore {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "winningresult")
  private String winningresult;

  @Column(name = "winningamount")
  private double winningamount;

  @Column(name = "userid")
  private Long userid;

  @Column(name = "amountid")
  private Long amountid;

  @Column(name = "questionid")
  private Long questionid;

  @Column(name = "username")
  private String username;

  @Column(name = "imageurl")
  private String imageurl;

  @Column(name = "captainid")
  private Long captainid;

  @Column(name = "vicecaptainid")
  private Long vicecaptainid;

  @Column(name = "suppoterid")
  private Long suppoterid;

  @Column(name = "participantoneid")
  private Long participantoneid;

  @Column(name = "participanttwoid")
  private Long participanttwoid;

  @Column(name = "participantthreeid")
  private Long participantthreeid;

  @Column(name = "participantfourid")
  private Long participantfourid;

  @Column(name = "participantfiveid")
  private Long participantfiveid;

  @Column(name = "participantsixid")
  private Long participantsixid;

  @Column(name = "participantsevenid")
  private Long participantsevenid;

  @Column(name = "participanteightid")
  private Long participanteightid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWinningresult() {
    return winningresult;
  }

  public void setWinningresult(String winningresult) {
    this.winningresult = winningresult;
  }

  public double getWinningamount() {
    return winningamount;
  }

  public void setWinningamount(double winningamount) {
    this.winningamount = winningamount;
  }

  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  public Long getAmountid() {
    return amountid;
  }

  public void setAmountid(Long amountid) {
    this.amountid = amountid;
  }

  public Long getQuestionid() {
    return questionid;
  }

  public void setQuestionid(Long questionid) {
    this.questionid = questionid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getImageurl() {
    return imageurl;
  }

  public void setImageurl(String imageurl) {
    this.imageurl = imageurl;
  }

  public Long getCaptainid() {
    return captainid;
  }

  public void setCaptainid(Long captainid) {
    this.captainid = captainid;
  }

  public Long getVicecaptainid() {
    return vicecaptainid;
  }

  public void setVicecaptainid(Long vicecaptainid) {
    this.vicecaptainid = vicecaptainid;
  }

  public Long getSuppoterid() {
    return suppoterid;
  }

  public void setSuppoterid(Long suppoterid) {
    this.suppoterid = suppoterid;
  }

  public Long getParticipantoneid() {
    return participantoneid;
  }

  public void setParticipantoneid(Long participantoneid) {
    this.participantoneid = participantoneid;
  }

  public Long getParticipanttwoid() {
    return participanttwoid;
  }

  public void setParticipanttwoid(Long participanttwoid) {
    this.participanttwoid = participanttwoid;
  }

  public Long getParticipantthreeid() {
    return participantthreeid;
  }

  public void setParticipantthreeid(Long participantthreeid) {
    this.participantthreeid = participantthreeid;
  }

  public Long getParticipantfourid() {
    return participantfourid;
  }

  public void setParticipantfourid(Long participantfourid) {
    this.participantfourid = participantfourid;
  }

  public Long getParticipantfiveid() {
    return participantfiveid;
  }

  public void setParticipantfiveid(Long participantfiveid) {
    this.participantfiveid = participantfiveid;
  }

  public Long getParticipantsixid() {
    return participantsixid;
  }

  public void setParticipantsixid(Long participantsixid) {
    this.participantsixid = participantsixid;
  }

  public Long getParticipantsevenid() {
    return participantsevenid;
  }

  public void setParticipantsevenid(Long participantsevenid) {
    this.participantsevenid = participantsevenid;
  }

  public Long getParticipanteightid() {
    return participanteightid;
  }

  public void setParticipanteightid(Long participanteightid) {
    this.participanteightid = participanteightid;
  }
}
