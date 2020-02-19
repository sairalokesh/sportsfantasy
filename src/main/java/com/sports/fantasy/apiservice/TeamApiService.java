package com.sports.fantasy.apiservice;

import java.util.List;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sports.fantasy.api.ApiTeams;

public interface TeamApiService {

  public List<ApiTeams> getApiTeams() throws UnirestException;
  public void getApiPlayers() throws UnirestException;
  public void getAllApiParticipantsByQuestionId(Long questionId) throws UnirestException;
  public void getAllApiParticipantPointsByQuestionId(Long questionId) throws UnirestException;

}
