package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BattingApi {
  private String title;
  private List<BattingScoreApi> scores;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<BattingScoreApi> getScores() {
    return scores;
  }

  public void setScores(List<BattingScoreApi> scores) {
    this.scores = scores;
  }


}
