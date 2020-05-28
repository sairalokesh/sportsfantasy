package com.sports.fantasy.userservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserPoints;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UserTempParticipants;
import com.sports.fantasy.repository.GameParticipantPointsRepository;
import com.sports.fantasy.repository.GameParticipantScoreRepository;
import com.sports.fantasy.repository.UserAmountRepository;
import com.sports.fantasy.repository.UserSelectedTeamRepository;
import com.sports.fantasy.userservice.UserSelectedTeamService;

@Service
@Transactional
public class UserSelectedTeamServiceImpl implements UserSelectedTeamService {

  @Autowired
  private UserSelectedTeamRepository userSelectedTeamRepository;
  @Autowired
  private GameParticipantScoreRepository gameParticipantScoreRepository;
  @Autowired
  private GameParticipantPointsRepository gameParticipantPointsRepository;
  @Autowired
  private UserAmountRepository userAmountRepository;
  @Autowired
  private UserTeamDao userTeamDao;

  @Override
  public UserSelectedTeam getUserSelectTeam(UserTempParticipants userTempParticipants) {
    UserSelectedTeam selectedTeam = new UserSelectedTeam();
    selectedTeam.setAmountEntries(new AmountEntries(userTempParticipants.getAmountId()));
    selectedTeam.setCreatedDate(new Date());
    selectedTeam.setGameQuestions(new GameQuestions(userTempParticipants.getQuestionId()));
    selectedTeam.setPaymentDone("Not_Done");
    selectedTeam.setUser(new UserInfo(userTempParticipants.getUserId()));
    selectedTeam.setWinningAmount(0.00);
    selectedTeam.setCaptainId(userTempParticipants.getCapitanId());
    selectedTeam.setViceCaptainId(userTempParticipants.getViceCapitanId());
    selectedTeam.setSuppoterId(userTempParticipants.getSuppoterId());
    selectedTeam.setGameType(userTempParticipants.getGameType());
    List<Long> participantIds = Stream.of(userTempParticipants.getParticipants().split(","))
        .map(Long::valueOf).collect(Collectors.toList());
    /*
     * String participantsCommaSeparated = participantIds.stream() .filter(p ->
     * !p.toString().equalsIgnoreCase(userTempParticipants.getCapitanId().toString()) &&
     * !p.toString().equalsIgnoreCase(userTempParticipants.getViceCapitanId().toString()) &&
     * !p.toString().equalsIgnoreCase(userTempParticipants.getSuppoterId().toString())) .map(p ->
     * p.toString()).collect(Collectors.joining(","));
     * selectedTeam.setParticipants(participantsCommaSeparated);
     */

    List<Long> participants = participantIds.stream()
        .filter(p -> !p.toString().equalsIgnoreCase(userTempParticipants.getCapitanId().toString())
            && !p.toString().equalsIgnoreCase(userTempParticipants.getViceCapitanId().toString())
            && !p.toString().equalsIgnoreCase(userTempParticipants.getSuppoterId().toString()))
        .map(p -> p).collect(Collectors.toList());

    if (userTempParticipants.getGameType().equals("cricket")) {
      getGameParticipants(participants, selectedTeam, 8);
    }

    return selectedTeam;
  }

  private void getGameParticipants(List<Long> participants, UserSelectedTeam selectedTeam,
      int index) {
    for (int i = 0; i < index; i++) {
      if (i == 0) {
        selectedTeam.setParticipantOneId(participants.get(i));
      } else if (i == 1) {
        selectedTeam.setParticipantTwoId(participants.get(i));
      } else if (i == 2) {
        selectedTeam.setParticipantThreeId(participants.get(i));
      } else if (i == 3) {
        selectedTeam.setParticipantFourId(participants.get(i));
      } else if (i == 4) {
        selectedTeam.setParticipantFiveId(participants.get(i));
      } else if (i == 5) {
        selectedTeam.setParticipantSixId(participants.get(i));
      } else if (i == 6) {
        selectedTeam.setParticipantSevenId(participants.get(i));
      } else if (i == 7) {
        selectedTeam.setParticipantEightId(participants.get(i));
      }
    }
  }

  @Override
  public UserSelectedTeam save(UserSelectedTeam userSelectedTeam) {
    return userSelectedTeamRepository.save(userSelectedTeam);
  }

  @Override
  public List<SelectedTeam> getTeamSelectedEditParticipants(Long userId, String gameType, boolean isActive) {
    return userSelectedTeamRepository.getTeamSelectedEditParticipants(userId, gameType, isActive);
  }

  @Override
  public List<UserTeamParticipants> getTeamsEditParticipants(Long questionId, Long amountId, Long userId) {
    return userTeamDao.getTeamsEditParticipants(questionId, amountId, userId);
  }

  @Override
  public UserSelectedTeam findByTeamId(Long teamId, Long userId) {
    return userSelectedTeamRepository.findByTeamId(teamId, userId);
  }

  @Override
  public List<SelectedTeam> getTeamSelectedLiveParticipants(Long userId, String gametype,
      boolean isActive) {
    return userSelectedTeamRepository.getTeamSelectedLiveParticipants(userId, gametype, isActive);
  }

  @Override
  public List<SelectedTeam> getTeamSelectedCompletedParticipants(Long userId, String gametype,
      boolean isActive) {
    return userSelectedTeamRepository.getTeamSelectedCompletedParticipants(userId, gametype,
        isActive);
  }

  @Override
  public UserSelectedTeam getSelectedUserTeam(Long teamId, Long questionId, Long amountId,
      Long userId) {
    return userSelectedTeamRepository.getSelectedUserTeam(teamId, questionId, amountId, userId);
  }

  @Override
  public Map<String, List<GameParticipantScore>> getAllParticipantsScores(
      UserSelectedTeam userSelectedTeam, Long questionId) {
    List<Long> participants = new ArrayList<>();
    if (userSelectedTeam != null) {
      participants.add(userSelectedTeam.getCaptainId());
      participants.add(userSelectedTeam.getViceCaptainId());
      participants.add(userSelectedTeam.getSuppoterId());
      participants.add(userSelectedTeam.getParticipantOneId());
      participants.add(userSelectedTeam.getParticipantTwoId());
      participants.add(userSelectedTeam.getParticipantThreeId());
      participants.add(userSelectedTeam.getParticipantFourId());
      participants.add(userSelectedTeam.getParticipantFiveId());
      participants.add(userSelectedTeam.getParticipantSixId());
      participants.add(userSelectedTeam.getParticipantSevenId());
      participants.add(userSelectedTeam.getParticipantEightId());
    }
    List<GameParticipantScore> gameParticipantScores =
        gameParticipantScoreRepository.getSelectedPArticipantsScore(questionId, participants);
    if (gameParticipantScores != null && !gameParticipantScores.isEmpty()) {
      Map<String, List<GameParticipantScore>> mapraticipants = new TreeMap<>();
      for (GameParticipantScore gameParticipantScore : gameParticipantScores) {
        if (userSelectedTeam.getCaptainId() != null && gameParticipantScore.getGameParticipants()
            .getId().toString().equalsIgnoreCase(userSelectedTeam.getCaptainId().toString())) {
          gameParticipantScore.setCaptain(true);
        } else if (userSelectedTeam.getViceCaptainId() != null && gameParticipantScore
            .getGameParticipants().getId().toString().equalsIgnoreCase(userSelectedTeam.getViceCaptainId().toString())) {
          gameParticipantScore.setViceCaptain(true);
        } else if (userSelectedTeam.getSuppoterId() != null && gameParticipantScore
            .getGameParticipants().getId().toString().equalsIgnoreCase(userSelectedTeam.getSuppoterId().toString())) {
          gameParticipantScore.setSuppoter(true);
        }
        if (mapraticipants
            .containsKey(gameParticipantScore.getGameParticipants().getParticipantType())) {
          List<GameParticipantScore> list =
              mapraticipants.get(gameParticipantScore.getGameParticipants().getParticipantType());
          list.add(gameParticipantScore);
          mapraticipants.put(gameParticipantScore.getGameParticipants().getParticipantType(), list);
        } else {
          List<GameParticipantScore> list = new ArrayList<>();
          list.add(gameParticipantScore);
          mapraticipants.put(gameParticipantScore.getGameParticipants().getParticipantType(), list);
        }
      }

      return mapraticipants;
    }
    return null;
  }

  @Override
  public UserPoints getSelectedParticipantScores(UserSelectedTeam userSelectedTeam, Long participantId, Long questionId) {
    GameParticipantPoints gameParticipantPoints = gameParticipantPointsRepository.findParticipantPointsByQuestionIdAndParticipantId(participantId, questionId);
    GameParticipantScore gameParticipantScore = gameParticipantScoreRepository.findByQuestionIdAndParticipantId(questionId, participantId);
    if(gameParticipantScore!=null && gameParticipantScore.getGameParticipants()!=null && gameParticipantScore.getGameParticipants().getId()!=null) {
      if(userSelectedTeam.getCaptainId()!=null && gameParticipantScore.getGameParticipants().getId().toString().equalsIgnoreCase(userSelectedTeam.getCaptainId().toString())) {
        gameParticipantScore.setCaptain(true);
      } else if(userSelectedTeam.getViceCaptainId()!=null && gameParticipantScore.getGameParticipants().getId().toString().equalsIgnoreCase(userSelectedTeam.getViceCaptainId().toString())) {
        gameParticipantScore.setViceCaptain(true);
      }  else if(userSelectedTeam.getSuppoterId()!=null && gameParticipantScore.getGameParticipants().getId().toString().equalsIgnoreCase(userSelectedTeam.getSuppoterId().toString())) {
        gameParticipantScore.setSuppoter(true);
      }
    }
    UserPoints userPoints = new UserPoints();
    userPoints.setGameParticipantPoints(gameParticipantPoints);
    userPoints.setGameParticipantScore(gameParticipantScore);
    return userPoints;
  }

  @Override
  public void deleteUserTeam(Long userTeamId) {
    userSelectedTeamRepository.deleteById(userTeamId);
  }

  @Override
  public Long getUsersCount(String gameType, Long questionId, Long amountId) {
    return userSelectedTeamRepository.getUsersCount(gameType, questionId, amountId);
  }

  @Override
  public Long getSelectedUserCount(String gameType, Long questionId, Long amountId, Long userId) {
    return userSelectedTeamRepository.getSelectedUserCount(gameType, questionId, amountId, userId);
  }

  @Override
  public List<SelectedTeam> getTeamSettlements(String gametype) {
    return userSelectedTeamRepository.getTeamSettlements(gametype, true);
  }

  @Override
  public void updateUserSelectedTeam(Ranking ranking, Long questionId, Long amountId, String gametype, Integer spiltAmount) {
    userSelectedTeamRepository.updateUserSelectedTeam(ranking.getId(), ranking.getUserId(), questionId, amountId, gametype, (double)spiltAmount);
    userAmountRepository.updateUserAmount(ranking.getUserId(), (double)spiltAmount);
    
  }

}
