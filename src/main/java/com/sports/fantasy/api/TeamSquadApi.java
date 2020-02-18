package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamSquadApi {

  private List<PlayerSquad> squad;

  public List<PlayerSquad> getSquad() {
    return squad;
  }

  public void setSquad(List<PlayerSquad> squad) {
    this.squad = squad;
  }


}
