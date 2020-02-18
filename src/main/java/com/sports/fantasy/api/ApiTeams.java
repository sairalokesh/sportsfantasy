package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTeams {

  @SerializedName(value = "unique_id")
  private Long uniqueId;
  private String date;
  private String dateTimeGMT;
  @SerializedName(value = "team-1")
  private String teamOne;
  @SerializedName(value = "team-2")
  private String teamTwo;
  private String type;
  private boolean squard;
  private boolean squad;
  private boolean matchStarted;

  public Long getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(Long uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDateTimeGMT() {
    return dateTimeGMT;
  }

  public void setDateTimeGMT(String dateTimeGMT) {
    this.dateTimeGMT = dateTimeGMT;
  }

  public String getTeamOne() {
    return teamOne;
  }

  public void setTeamOne(String teamOne) {
    this.teamOne = teamOne;
  }

  public String getTeamTwo() {
    return teamTwo;
  }

  public void setTeamTwo(String teamTwo) {
    this.teamTwo = teamTwo;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isSquard() {
    return squard;
  }

  public void setSquard(boolean squard) {
    this.squard = squard;
  }

  public boolean isMatchStarted() {
    return matchStarted;
  }

  public void setMatchStarted(boolean matchStarted) {
    this.matchStarted = matchStarted;
  }

  public boolean isSquad() {
    return squad;
  }

  public void setSquad(boolean squad) {
    this.squad = squad;
  }


}
