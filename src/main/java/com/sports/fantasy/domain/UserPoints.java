package com.sports.fantasy.domain;

import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipantScore;

public class UserPoints {
  private GameParticipantPoints gameParticipantPoints;
  private GameParticipantScore gameParticipantScore;

  public GameParticipantPoints getGameParticipantPoints() {
    return gameParticipantPoints;
  }

  public void setGameParticipantPoints(GameParticipantPoints gameParticipantPoints) {
    this.gameParticipantPoints = gameParticipantPoints;
  }

  public GameParticipantScore getGameParticipantScore() {
    return gameParticipantScore;
  }

  public void setGameParticipantScore(GameParticipantScore gameParticipantScore) {
    this.gameParticipantScore = gameParticipantScore;
  }
}
