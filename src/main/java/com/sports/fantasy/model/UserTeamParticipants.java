package com.sports.fantasy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_team_participants")
public class UserTeamParticipants {

  @Id
  @Column(name = "userselectedteamid")
  private Long userselectedteamid;

  @Column(name = "captain")
  private String captain;

  @Column(name = "captainimage")
  private String captainimage;

  @Column(name = "vicecaptain")
  private String vicecaptain;

  @Column(name = "vicecaptainimage")
  private String vicecaptainimage;

  @Column(name = "suppoter")
  private String suppoter;

  @Column(name = "suppoterimage")
  private String suppoterimage;

  public Long getUserselectedteamid() {
    return userselectedteamid;
  }

  public void setUserselectedteamid(Long userselectedteamid) {
    this.userselectedteamid = userselectedteamid;
  }

  public String getCaptain() {
    return captain;
  }

  public void setCaptain(String captain) {
    this.captain = captain;
  }

  public String getCaptainimage() {
    return captainimage;
  }

  public void setCaptainimage(String captainimage) {
    this.captainimage = captainimage;
  }

  public String getVicecaptain() {
    return vicecaptain;
  }

  public void setVicecaptain(String vicecaptain) {
    this.vicecaptain = vicecaptain;
  }

  public String getVicecaptainimage() {
    return vicecaptainimage;
  }

  public void setVicecaptainimage(String vicecaptainimage) {
    this.vicecaptainimage = vicecaptainimage;
  }

  public String getSuppoter() {
    return suppoter;
  }

  public void setSuppoter(String suppoter) {
    this.suppoter = suppoter;
  }

  public String getSuppoterimage() {
    return suppoterimage;
  }

  public void setSuppoterimage(String suppoterimage) {
    this.suppoterimage = suppoterimage;
  }
}
