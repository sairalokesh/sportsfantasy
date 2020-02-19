package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlingScoreApi {

  @SerializedName(value = "W")
  private String wickets;
  @SerializedName(value = "M")
  private String madiens;
  private String pid;

  public String getWickets() {
    return wickets;
  }

  public void setWickets(String wickets) {
    this.wickets = wickets;
  }

  public String getMadiens() {
    return madiens;
  }

  public void setMadiens(String madiens) {
    this.madiens = madiens;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }
}
