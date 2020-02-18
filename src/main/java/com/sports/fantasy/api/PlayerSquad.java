package com.sports.fantasy.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerSquad {
  private String name;
  private List<SquadPlayers> players;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SquadPlayers> getPlayers() {
    return players;
  }

  public void setPlayers(List<SquadPlayers> players) {
    this.players = players;
  }



}
