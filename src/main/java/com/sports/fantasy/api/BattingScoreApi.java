package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BattingScoreApi {
  @SerializedName(value = "6s")
  private String sixes;
  @SerializedName(value = "4s")
  private String fours;
  @SerializedName(value = "R")
  private String runs;
  private String pid;

  public String getSixes() {
    return sixes;
  }

  public void setSixes(String sixes) {
    this.sixes = sixes;
  }

  public String getFours() {
    return fours;
  }

  public void setFours(String fours) {
    this.fours = fours;
  }

  public String getRuns() {
    return runs;
  }

  public void setRuns(String runs) {
    this.runs = runs;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }


}
