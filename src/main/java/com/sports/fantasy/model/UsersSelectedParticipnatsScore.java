package com.sports.fantasy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users_selected_participnats_score")
public class UsersSelectedParticipnatsScore {

  @Id
  @Column(name = "participantid")
  private Long participantid;

  @Column(name = "score")
  private double score = 0.00;

  public Long getParticipantid() {
    return participantid;
  }

  public void setParticipantid(Long participantid) {
    this.participantid = participantid;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }



}
