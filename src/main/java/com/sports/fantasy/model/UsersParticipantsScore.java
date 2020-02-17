package com.sports.fantasy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users_participants_score")
public class UsersParticipantsScore {

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

  @Column(name = "totalscore")
  private Double totalscore;

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

  public Double getTotalscore() {
    return totalscore;
  }

  public void setTotalscore(Double totalscore) {
    this.totalscore = totalscore;
  }

}
