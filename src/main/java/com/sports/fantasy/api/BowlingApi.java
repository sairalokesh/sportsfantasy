package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlingApi {
  private String title;
  private List<BowlingScoreApi> scores;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<BowlingScoreApi> getScores() {
    return scores;
  }

  public void setScores(List<BowlingScoreApi> scores) {
    this.scores = scores;
  }



}
