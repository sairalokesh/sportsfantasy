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
import com.sports.fantasy.api.ApiScore;
import com.sports.fantasy.api.ApiTeams;
import com.sports.fantasy.api.BattingApi;
import com.sports.fantasy.api.BattingScoreApi;
import com.sports.fantasy.api.BowlingApi;
import com.sports.fantasy.api.BowlingScoreApi;
import com.sports.fantasy.api.FieldingApi;
import com.sports.fantasy.api.FieldingScoreApi;
import com.sports.fantasy.api.PlayerSquad;
import com.sports.fantasy.api.SquadPlayers;
import com.sports.fantasy.api.TeamSquadApi;
import com.sports.fantasy.apiservice.TeamApiService;
import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.Players;
import com.sports.fantasy.repository.GameParticipantPointsRepository;
import com.sports.fantasy.repository.GameParticipantRepository;
import com.sports.fantasy.repository.GameParticipantScoreRepository;
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
  @Autowired 
  private GameParticipantScoreRepository gameParticipantScoreRepository;

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
              String countryCode = "";
              String coun[] = country.split(" ");
              if(coun.length>=3) {
                countryCode = (coun[0].substring(0,1)+coun[1].substring(0,1)+coun[2].substring(0, 1)).trim().toUpperCase();
              } else if (coun.length==2) {
                countryCode = (coun[0].substring(0,2)+coun[1].substring(0,1)).trim().toUpperCase();
              } else if (coun.length==1) {
                countryCode = (coun[0].substring(0,3)).trim().toUpperCase();
              }

              for (SquadPlayers squadPlayers : playerSquad.getPlayers()) {
                GameParticipants gameParticipants = new GameParticipants();
                gameParticipants.setParticipantName(squadPlayers.getName());
                gameParticipants.setParticipantGameType(countryCode);
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
      GameParticipantPoints dbGameParticipantPoints = gameParticipantPointsRepository.findParticipantPointsByQuestionIdAndParticipantId(dbGameParticipants.getId(), dbGameParticipants.getGameQuestions().getId());
      if (dbGameParticipantPoints == null) {
        GameParticipantPoints saveGameParticipantPoints = new GameParticipantPoints();
        saveGameParticipantPoints.setBowleds(0);
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
        saveGameParticipantPoints.setLbws(0);
        saveGameParticipantPoints.setStumpeds(0);
        GameParticipantPoints gameParticipantPoints = gameParticipantPointsRepository.save(saveGameParticipantPoints);
        if(gameParticipantPoints!=null) {
          GameParticipantScore gameParticipantScore = new GameParticipantScore();
          double score = 
              (1 * gameParticipantPoints.getRuns()) + 
              (1 * gameParticipantPoints.getFours()) + 
              (2 * gameParticipantPoints.getSixes()) + 
              (6 * gameParticipantPoints.getFifties()) + 
              (12 * gameParticipantPoints.getHundries()) + 
              (10 * gameParticipantPoints.getWickets()) + 
              (5 * gameParticipantPoints.getMaidens()) + 
              (7 * gameParticipantPoints.getCatches()) +
              (6 * gameParticipantPoints.getStumpeds()) +
              (6 * gameParticipantPoints.getRunouts()) +
              (6 * gameParticipantPoints.getBowleds()) +
              (6 * gameParticipantPoints.getLbws());
          /*
           * if(StringUtils.hasText(gameParticipantPoints.getGameParticipants().getAvailability())
           * &&
           * gameParticipantPoints.getGameParticipants().getAvailability().equalsIgnoreCase("YES"))
           * { score = score + 2; }
           */
          gameParticipantScore.setScore(score);
          double captainScore = 3 * score;
          gameParticipantScore.setCaptainScore(captainScore);
          double viceCaptainScore = 2 * score;
          gameParticipantScore.setViceCaptainScore(viceCaptainScore);
          double suppoterScore = (1.5) * score;
          gameParticipantScore.setSuppoterScore(suppoterScore);
          gameParticipantScore.setGameQuestions(new GameQuestions(dbGameParticipants.getGameQuestions().getId()));
          gameParticipantScore.setGameParticipants(gameParticipantPoints.getGameParticipants());
          GameParticipantScore dbGameParticipantScore = gameParticipantScoreRepository.findByQuestionIdAndParticipantId(dbGameParticipants.getGameQuestions().getId(), gameParticipantPoints.getGameParticipants().getId());
          if(dbGameParticipantScore!=null && dbGameParticipantScore.getId()!=null) {
              gameParticipantScore.setId(dbGameParticipantScore.getId());
          }
          gameParticipantScoreRepository.save(gameParticipantScore);
        }  
      }
    }
  }

  @Override
  public void getAllApiParticipantPointsByQuestionId(Long questionId) throws UnirestException {
    GameQuestions gameQuestions = new GameQuestions();
    Optional<GameQuestions> dbGameQuestions = gameQuestionsRepository.findById(questionId);
    if (dbGameQuestions.isPresent()) {
      gameQuestions = dbGameQuestions.get();
    }

    if (gameQuestions != null && gameQuestions.getUniqueId() != null) {
      HttpResponse<String> response = Unirest.get("https://cricapi.com/api/fantasySummary?apikey=quyJMO90QecLkBoY8RtAiMH8Xj52&unique_id=" + gameQuestions.getUniqueId()).asString();
      if (response.getStatus() == HttpStatus.OK.value()) {
        Gson gson = new Gson();
        Type type = new TypeToken<ApiScore>() {}.getType();
        ApiScore scores = gson.fromJson(response.getBody(), type);
        if (scores != null && scores.getData() != null) {
          
          if (scores.getData().getBatting()!=null && !scores.getData().getBatting().isEmpty()) {
            for(BattingApi battingApi: scores.getData().getBatting()) {
              if(battingApi.getScores()!=null && !battingApi.getScores().isEmpty()) {
                
                for(BattingScoreApi battingScore: battingApi.getScores()) {
                  Long pid = Long.parseLong(battingScore.getPid());
                  GameParticipantPoints dbGameParticipantPoints = gameParticipantPointsRepository.getSelectedApiPArticipantPoints(questionId, pid);
                  if(dbGameParticipantPoints!=null) {
                    if(StringUtils.hasText(battingScore.getFours())) {
                      Integer fours = Integer.parseInt(battingScore.getFours());
                      dbGameParticipantPoints.setFours(fours);
                    }
                    if(StringUtils.hasText(battingScore.getSixes())) {
                      Integer sixes = Integer.parseInt(battingScore.getSixes());
                      dbGameParticipantPoints.setSixes(sixes);
                    }
                    if(StringUtils.hasText(battingScore.getRuns())) {
                      Integer runs = Integer.parseInt(battingScore.getRuns());
                      dbGameParticipantPoints.setRuns(runs);
                      if(runs>=50 && runs<100) {
                        dbGameParticipantPoints.setFifties(1);
                      } else if(runs>=100) {
                        dbGameParticipantPoints.setHundries(1);
                      }
                    }
                    gameParticipantPointsRepository.save(dbGameParticipantPoints);
                  }
                }
                
              }
            }
          }
          
          if (scores.getData().getBowling()!=null && !scores.getData().getBowling().isEmpty()) {
            for(BowlingApi bowlingApi: scores.getData().getBowling()) {
              if(bowlingApi.getScores()!=null && !bowlingApi.getScores().isEmpty()) {
                
                for(BowlingScoreApi bowlingScoreApi: bowlingApi.getScores()) {
                  Long pid = Long.parseLong(bowlingScoreApi.getPid());
                  GameParticipantPoints dbGameParticipantPoints = gameParticipantPointsRepository.getSelectedApiPArticipantPoints(questionId, pid);
                  if(dbGameParticipantPoints!=null) {
                    if(StringUtils.hasText(bowlingScoreApi.getWickets())) {
                      Integer wickets = Integer.parseInt(bowlingScoreApi.getWickets());
                      dbGameParticipantPoints.setWickets(wickets);
                    }

                    if(StringUtils.hasText(bowlingScoreApi.getMadiens())) {
                      Integer madiens = Integer.parseInt(bowlingScoreApi.getMadiens());
                      dbGameParticipantPoints.setMaidens(madiens);
                    }
                    
                    gameParticipantPointsRepository.save(dbGameParticipantPoints);
                  }
                }
                
              }
            }
          }
          
          if (scores.getData().getFielding()!=null && !scores.getData().getFielding().isEmpty()) {
            for(FieldingApi fieldingApi: scores.getData().getFielding()) {
              if(fieldingApi.getScores()!=null && !fieldingApi.getScores().isEmpty()) {
                
                for(FieldingScoreApi fieldingScoreApi: fieldingApi.getScores()) {
                  Long pid = Long.parseLong(fieldingScoreApi.getPid());
                  GameParticipantPoints dbGameParticipantPoints = gameParticipantPointsRepository.getSelectedApiPArticipantPoints(questionId, pid);
                  if(dbGameParticipantPoints!=null) {
                    if(StringUtils.hasText(fieldingScoreApi.getCatches())) {
                      Integer catches = Integer.parseInt(fieldingScoreApi.getCatches());
                      dbGameParticipantPoints.setCatches(catches);
                    }

                    if(StringUtils.hasText(fieldingScoreApi.getLbw())) {
                      Integer lbws = Integer.parseInt(fieldingScoreApi.getLbw());
                      dbGameParticipantPoints.setLbws(lbws);
                    }
                    if(StringUtils.hasText(fieldingScoreApi.getBowled())) {
                      Integer bowleds = Integer.parseInt(fieldingScoreApi.getBowled());
                      dbGameParticipantPoints.setBowleds(bowleds);
                    }
                    if(StringUtils.hasText(fieldingScoreApi.getStumped())) {
                      Integer stumpeds = Integer.parseInt(fieldingScoreApi.getStumped());
                      dbGameParticipantPoints.setStumpeds(stumpeds);
                    }
                    
                    gameParticipantPointsRepository.save(dbGameParticipantPoints);
                  }
                }
                
              }
            }
          }
          updateQuestionParticipantsScore(questionId);
        }
      }
      
    } 
  }
  
  private void updateQuestionParticipantsScore(Long questionId) {
    List<GameParticipantPoints> dbGameParticipantPoints = gameParticipantPointsRepository.getAllParticipantPointsByQuestionId(questionId);
    if(dbGameParticipantPoints!=null && !dbGameParticipantPoints.isEmpty()) {
        for(GameParticipantPoints gameParticipantPoints : dbGameParticipantPoints) {
            if(gameParticipantPoints.getGameParticipants()!=null && gameParticipantPoints.getGameParticipants().getId()!=null && gameParticipantPoints.getGameQuestions()!=null && gameParticipantPoints.getGameQuestions().getId()!=null) {
                GameParticipantScore gameParticipantScore = new GameParticipantScore();
                double score = 
                        (1 * gameParticipantPoints.getRuns()) + 
                        (1 * gameParticipantPoints.getFours()) + 
                        (2 * gameParticipantPoints.getSixes()) + 
                        (6 * gameParticipantPoints.getFifties()) + 
                        (12 * gameParticipantPoints.getHundries()) + 
                        (10 * gameParticipantPoints.getWickets()) + 
                        (5 * gameParticipantPoints.getMaidens()) + 
                        (7 * gameParticipantPoints.getCatches()) +
                        (6 * gameParticipantPoints.getStumpeds()) +
                        (6 * gameParticipantPoints.getRunouts()) +
                        (6 * gameParticipantPoints.getBowleds()) +
                        (6 * gameParticipantPoints.getLbws());
                        
                gameParticipantScore.setScore(score);
                double captainScore = 3 * score;
                gameParticipantScore.setCaptainScore(captainScore);
                double viceCaptainScore = 2 * score;
                gameParticipantScore.setViceCaptainScore(viceCaptainScore);
                double suppoterScore = (1.5) * score;
                gameParticipantScore.setSuppoterScore(suppoterScore);
                gameParticipantScore.setGameQuestions(new GameQuestions(questionId));
                gameParticipantScore.setGameParticipants(gameParticipantPoints.getGameParticipants());
                GameParticipantScore dbGameParticipantScore = gameParticipantScoreRepository.findByQuestionIdAndParticipantId(questionId, gameParticipantPoints.getGameParticipants().getId());
                if(dbGameParticipantScore!=null && dbGameParticipantScore.getId()!=null) {
                    gameParticipantScore.setId(dbGameParticipantScore.getId());
                }
                gameParticipantScoreRepository.save(gameParticipantScore);
            }
        }
    }
} 
}
