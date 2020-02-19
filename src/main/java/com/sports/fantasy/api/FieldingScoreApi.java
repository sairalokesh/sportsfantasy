package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldingScoreApi {
  @SerializedName(value = "catch")
  private String catches;
  private String lbw;
  private String bowled;
  private String stumped;
  private String pid;

  public String getCatches() {
    return catches;
  }

  public void setCatches(String catches) {
    this.catches = catches;
  }

  public String getLbw() {
    return lbw;
  }

  public void setLbw(String lbw) {
    this.lbw = lbw;
  }

  public String getBowled() {
    return bowled;
  }

  public void setBowled(String bowled) {
    this.bowled = bowled;
  }

  public String getStumped() {
    return stumped;
  }

  public void setStumped(String stumped) {
    this.stumped = stumped;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }


}
