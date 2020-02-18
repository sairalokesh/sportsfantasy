package com.sports.fantasy.apiservice.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sports.fantasy.api.ApiPlayers;
import com.sports.fantasy.api.ApiTeams;
import com.sports.fantasy.api.PlayerSquad;
import com.sports.fantasy.api.SquadPlayers;
import com.sports.fantasy.api.TeamSquadApi;
import com.sports.fantasy.apiservice.TeamApiService;
import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.Players;
import com.sports.fantasy.repository.GameParticipantPointsRepository;
import com.sports.fantasy.repository.GameParticipantRepository;
import com.sports.fantasy.repository.GameQuestionsRepository;
import com.sports.fantasy.repository.PlayersRepository;


@Service
@Transactional
public class TeamApiServiceImpl implements TeamApiService {

  @Autowired
  private PlayersRepository playersRepository;
  @Autowired
  private GameQuestionsRepository gameQuestionsRepository;
  @Autowired
  private GameParticipantRepository gameParticipantRepository;
  @Autowired
  private GameParticipantPointsRepository gameParticipantPointsRepository;

  @Override
  public List<ApiTeams> getApiTeams() throws UnirestException {
    List<ApiTeams> teams = new ArrayList<>();
    String url = "https://cricapi.com/api/matches?apikey=n9kDOo7oGAamwznKi9MXy1gxpmC3";
    HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
    JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("matches");
    if (jsonArray.length() > 0) {
      Gson gson = new Gson();
      Type type = new TypeToken<List<ApiTeams>>() {}.getType();
      teams = gson.fromJson(jsonArray.toString(), type);
    }
    return teams;
  }

  @Override
  public void getApiPlayers() throws UnirestException {
    String url =
        "https://cricket.sportmonks.com/api/v2.0/players?api_token=m9ilplT2UcE4oPRRsVbjntdjaIYCXO5mn5TXZSjDMPpmm01hA9KlPjoZ9AMz";
    HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
    JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("data");
    Gson gson = new Gson();
    Type type = new TypeToken<List<ApiPlayers>>() {}.getType();
    List<ApiPlayers> players = gson.fromJson(jsonArray.toString(), type);
    if (players != null && !players.isEmpty()) {
      for (ApiPlayers player : players) {
        Players dbplayer = new Players();
        dbplayer.setFullName(player.getFullname());
        dbplayer.setImagePath(player.getImagePath());
        dbplayer.setPosition(player.getPosition().getName());
        playersRepository.save(dbplayer);
      }
    }
  }

  @Override
  public void getAllApiParticipantsByQuestionId(Long questionId) throws UnirestException {
    GameQuestions gameQuestions = new GameQuestions();
    Optional<GameQuestions> dbGameQuestions = gameQuestionsRepository.findById(questionId);
    if (dbGameQuestions.isPresent()) {
      gameQuestions = dbGameQuestions.get();
    }

    if (gameQuestions != null && gameQuestions.getUniqueId() != null) {
      long count = gameParticipantRepository.getParticipantsCountByQuestionId(questionId);
      if (count == 0) {
        HttpResponse<String> response = Unirest.get(
            "https://cricapi.com/api/fantasySquad?apikey=n9kDOo7oGAamwznKi9MXy1gxpmC3&unique_id="
                + gameQuestions.getUniqueId())
            .asString();
        if (response.getStatus() == HttpStatus.OK.value()) {
          Gson gson = new Gson();
          Type type = new TypeToken<TeamSquadApi>() {}.getType();
          TeamSquadApi squads = gson.fromJson(response.getBody(), type);
          if (squads != null && squads.getSquad() != null && !squads.getSquad().isEmpty()) {
            for (PlayerSquad playerSquad : squads.getSquad()) {
              String country = playerSquad.getName();

              for (SquadPlayers squadPlayers : playerSquad.getPlayers()) {
                GameParticipants gameParticipants = new GameParticipants();
                gameParticipants.setParticipantName(squadPlayers.getName());
                gameParticipants.setParticipantGameType(country);
                gameParticipants.setParticipantEffect("rotateIn");
                List<Players> players =
                    playersRepository.getPlayerInfoByName(squadPlayers.getName());
                if (players != null && !players.isEmpty()) {
                  gameParticipants.setParticipantImage(players.get(0).getImagePath());
                  if (StringUtils.hasText(players.get(0).getPosition())
                      && players.get(0).getPosition().equalsIgnoreCase("Batsman")) {
                    gameParticipants.setParticipantType("BAT");
                  } else if (StringUtils.hasText(players.get(0).getPosition())
                      && players.get(0).getPosition().equalsIgnoreCase("Wicketkeeper")) {
                    gameParticipants.setParticipantType("WK");
                  } else if (StringUtils.hasText(players.get(0).getPosition())
                      && players.get(0).getPosition().equalsIgnoreCase("Allrounder")) {
                    gameParticipants.setParticipantType("AR");
                  } else if (StringUtils.hasText(players.get(0).getPosition())
                      && players.get(0).getPosition().equalsIgnoreCase("Bowler")) {
                    gameParticipants.setParticipantType("BOWL");
                  }
                }

                gameParticipants.setGameQuestions(new GameQuestions(questionId));
                gameParticipants.setParticipantPoints(0.0);
                gameParticipants.setParticipantRating(0.0);
                gameParticipants.setPid(squadPlayers.getPid());
                GameParticipants dbGameParticipants =
                    gameParticipantRepository.save(gameParticipants);
                saveParticipantsPoints(dbGameParticipants);
              }
            }
          }
        }
      }
    }
  }

  private void saveParticipantsPoints(GameParticipants dbGameParticipants) {
    if (dbGameParticipants != null && dbGameParticipants.getId() != null
        && dbGameParticipants.getGameQuestions() != null
        && dbGameParticipants.getGameQuestions().getId() != null) {
      GameParticipantPoints gameParticipantPoints = gameParticipantPointsRepository.findParticipantPointsByQuestionIdAndParticipantId(dbGameParticipants.getId(), dbGameParticipants.getGameQuestions().getId());
      if (gameParticipantPoints == null) {
        GameParticipantPoints saveGameParticipantPoints = new GameParticipantPoints();
        saveGameParticipantPoints.setBowlings(0);
        saveGameParticipantPoints.setCatches(0);
        saveGameParticipantPoints.setFifties(0);
        saveGameParticipantPoints.setFours(0);
        saveGameParticipantPoints.setGameParticipants(new GameParticipants(dbGameParticipants.getId()));
        saveGameParticipantPoints.setGameQuestions(new GameQuestions(dbGameParticipants.getGameQuestions().getId()));
        saveGameParticipantPoints.setHundries(0);
        saveGameParticipantPoints.setMaidens(0);
        saveGameParticipantPoints.setRunouts(0);
        saveGameParticipantPoints.setRuns(0);
        saveGameParticipantPoints.setSixes(0);
        saveGameParticipantPoints.setWickets(0);
        gameParticipantPointsRepository.save(saveGameParticipantPoints);
      }
    }
  }
}
