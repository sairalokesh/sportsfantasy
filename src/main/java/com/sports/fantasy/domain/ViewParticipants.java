package com.sports.fantasy.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.sports.fantasy.model.GameParticipantScore;

public class ViewParticipants implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -343909610275211280L;
  private Map<String, List<GameParticipantScore>> teamParticipants;
  private String lastCountry;
  private String message;

  public Map<String, List<GameParticipantScore>> getTeamParticipants() {
    return teamParticipants;
  }

  public void setTeamParticipants(Map<String, List<GameParticipantScore>> teamParticipants) {
    this.teamParticipants = teamParticipants;
  }

  public String getLastCountry() {
    return lastCountry;
  }

  public void setLastCountry(String lastCountry) {
    this.lastCountry = lastCountry;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
