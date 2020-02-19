package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiData {
  private List<BattingApi> batting;
  private List<BowlingApi> bowling;
  private List<FieldingApi> fielding;

  public List<BattingApi> getBatting() {
    return batting;
  }

  public void setBatting(List<BattingApi> batting) {
    this.batting = batting;
  }

  public List<BowlingApi> getBowling() {
    return bowling;
  }

  public void setBowling(List<BowlingApi> bowling) {
    this.bowling = bowling;
  }

  public List<FieldingApi> getFielding() {
    return fielding;
  }

  public void setFielding(List<FieldingApi> fielding) {
    this.fielding = fielding;
  }



}
