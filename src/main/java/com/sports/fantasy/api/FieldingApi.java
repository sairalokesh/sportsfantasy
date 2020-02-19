package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldingApi {

  private String title;
  private List<FieldingScoreApi> scores;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<FieldingScoreApi> getScores() {
    return scores;
  }

  public void setScores(List<FieldingScoreApi> scores) {
    this.scores = scores;
  }



}
