package com.sports.fantasy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.domain.SelectedParticipants;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTempParticipants;

public class DataMapper {

  private DataMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static SelectedMembers getRedirectTeamSelection(UserTempParticipants dbTempParticipants,
      UserInfo user) {
    SelectedMembers selectedMembers = new SelectedMembers();
    if (dbTempParticipants != null && StringUtils.hasText(dbTempParticipants.getParticipants())) {
      List<String> participantIds =
          Stream.of(dbTempParticipants.getParticipants().split(",")).collect(Collectors.toList());
      if (participantIds != null && !participantIds.isEmpty()) {
        List<Long> teamIds =
            participantIds.stream().map(p -> Long.parseLong(p)).collect(Collectors.toList());
        selectedMembers.setSelectedMembers(teamIds);
      }
    }

    if (dbTempParticipants != null && dbTempParticipants.getAmountId() != null) {
      selectedMembers.setAmountId(dbTempParticipants.getAmountId());
    }

    if (dbTempParticipants != null && dbTempParticipants.getQuestionId() != null) {
      selectedMembers.setQuestionId(dbTempParticipants.getQuestionId());
    }

    if (dbTempParticipants != null && dbTempParticipants.getId() != null) {
      selectedMembers.setUserTempId(dbTempParticipants.getId());
    }

    if (dbTempParticipants != null && StringUtils.hasText(dbTempParticipants.getGameType())) {
      selectedMembers.setGameType(dbTempParticipants.getGameType());
    }

    if (dbTempParticipants != null && dbTempParticipants.getCapitanId() != null) {
      selectedMembers.setCapitanId(dbTempParticipants.getCapitanId());
    }

    if (dbTempParticipants != null && dbTempParticipants.getViceCapitanId() != null) {
      selectedMembers.setViceCapitanId(dbTempParticipants.getViceCapitanId());
    }

    if (dbTempParticipants != null && dbTempParticipants.getSuppoterId() != null) {
      selectedMembers.setSuppoterId(dbTempParticipants.getSuppoterId());
    }

    return selectedMembers;
  }

  /* Player Country Count (INDIA, WESTINDIES) */
  public static Map<String, Integer> getGameTypeCounts(
      Map<String, List<GameParticipants>> gameParticipants, SelectedMembers selectedMembers) {
    Map<String, Integer> gametypecounts = new TreeMap<>();
    if (gameParticipants != null && gameParticipants.size() > 0) {
      for (Entry<String, List<GameParticipants>> entry : gameParticipants.entrySet()) {
        List<GameParticipants> gameParticipantss = entry.getValue();
        for (GameParticipants gameParticipant : gameParticipantss) {
          if (selectedMembers.getSelectedMembers().contains(gameParticipant.getId())) {
            if (gametypecounts.containsKey(gameParticipant.getParticipantGameType())) {
              gametypecounts.put(gameParticipant.getParticipantGameType(),
                  gametypecounts.get(gameParticipant.getParticipantGameType()) + 1);
            } else {
              gametypecounts.put(gameParticipant.getParticipantGameType(), 1);
            }
          }
        }
      }
    }
    return gametypecounts;
  }

  /* Player Types Count (WK, AR, BOWL, BAT) */
  public static Map<String, Integer> getGamePlayerCounts(
      Map<String, List<GameParticipants>> gameParticipants, SelectedMembers selectedMembers) {
    Map<String, Integer> playertypecounts = new TreeMap<>();
    if (gameParticipants != null && gameParticipants.size() > 0) {
      for (Entry<String, List<GameParticipants>> entry : gameParticipants.entrySet()) {
        List<GameParticipants> gameParticipantss = entry.getValue();
        for (GameParticipants gameParticipant : gameParticipantss) {
          if (selectedMembers.getSelectedMembers().contains(gameParticipant.getId())) {
            if (playertypecounts.containsKey(gameParticipant.getParticipantType())) {
              playertypecounts.put(gameParticipant.getParticipantType(),
                  playertypecounts.get(gameParticipant.getParticipantType()) + 1);
            } else {
              playertypecounts.put(gameParticipant.getParticipantType(), 1);
            }
          }
        }
      }
    }
    return playertypecounts;
  }


  public static Map<String, List<GameParticipants>> getGameParticipants(
      List<GameParticipants> gameParticipants, SelectedParticipants participants) {
    Map<String, List<GameParticipants>> mapPameParticipants = new TreeMap<>();
    if (gameParticipants != null && !gameParticipants.isEmpty()) {
      for (GameParticipants gameParticipant : gameParticipants) {
        if (participants != null && gameParticipant != null) {
          if (participants.getCaptainId() != null && gameParticipant.getId() != null
              && participants.getCaptainId().toString().equalsIgnoreCase(gameParticipant.getId().toString())) {
            gameParticipant.setCapitanId(participants.getCaptainId());
          }
          
          if (participants.getViceCaptainId() != null && gameParticipant.getId() != null
              && participants.getViceCaptainId().toString().equalsIgnoreCase(gameParticipant.getId().toString())) {
            gameParticipant.setViceCapitanId(participants.getViceCaptainId());
          }
          if (participants.getSuppoterId() != null && gameParticipant.getId() != null
              && participants.getSuppoterId().toString().equalsIgnoreCase(gameParticipant.getId().toString())) {
            gameParticipant.setSuppoterId(participants.getSuppoterId());
          }
        }

        if (mapPameParticipants.containsKey(gameParticipant.getParticipantType())) { // Means WK,
                                                                                     // BATS, BOWL,
                                                                                     // AR
          List<GameParticipants> list =
              mapPameParticipants.get(gameParticipant.getParticipantType());
          list.add(gameParticipant);
          mapPameParticipants.put(gameParticipant.getParticipantType(), list);
        } else {
          List<GameParticipants> list = new ArrayList<>();
          list.add(gameParticipant);
          mapPameParticipants.put(gameParticipant.getParticipantType(), list);
        }
      }
    }
    return mapPameParticipants;
  }


  public static SelectedMembers getSelectedMembers(UserSelectedTeam userSelectedTeam) {
    SelectedMembers selectedMembers = new SelectedMembers();
    if (StringUtils.hasText(userSelectedTeam.getGameType())
        && userSelectedTeam.getGameType().equalsIgnoreCase("cricket")) {
      List<Long> participants = getGameParticipants(userSelectedTeam, 8);
      participants.add(userSelectedTeam.getCaptainId());
      participants.add(userSelectedTeam.getViceCaptainId());
      participants.add(userSelectedTeam.getSuppoterId());
      selectedMembers.setSelectedMembers(participants);
    }

    /*
     * if (userSelectedTeam != null && StringUtils.hasText(userSelectedTeam.getParticipants())) {
     * List<String> participantIds =
     * Stream.of(userSelectedTeam.getParticipants().split(",")).collect(Collectors.toList()); if
     * (participantIds != null && !participantIds.isEmpty()) { List<Long> teamIds =
     * participantIds.stream().map(p -> Long.parseLong(p)).collect(Collectors.toList());
     * teamIds.add(userSelectedTeam.getCaptainId());
     * teamIds.add(userSelectedTeam.getViceCaptainId());
     * teamIds.add(userSelectedTeam.getSuppoterId()); selectedMembers.setSelectedMembers(teamIds); }
     * }
     */

    if (userSelectedTeam != null && userSelectedTeam.getCaptainId() != null) {
      selectedMembers.setCapitanId(userSelectedTeam.getCaptainId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getViceCaptainId() != null) {
      selectedMembers.setViceCapitanId(userSelectedTeam.getViceCaptainId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getSuppoterId() != null) {
      selectedMembers.setSuppoterId(userSelectedTeam.getSuppoterId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getAmountEntries() != null
        && userSelectedTeam.getAmountEntries().getId() != null) {
      selectedMembers.setAmountId(userSelectedTeam.getAmountEntries().getId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getGameQuestions() != null
        && userSelectedTeam.getGameQuestions().getId() != null) {
      selectedMembers.setQuestionId(userSelectedTeam.getGameQuestions().getId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getId() != null) {
      selectedMembers.setUserTeamId(userSelectedTeam.getId());
    }

    if (userSelectedTeam != null && userSelectedTeam.getGameQuestions() != null
        && StringUtils.hasText(userSelectedTeam.getGameQuestions().getQuestionType())) {
      selectedMembers.setGameType(userSelectedTeam.getGameQuestions().getQuestionType());
    }

    return selectedMembers;
  }

  private static List<Long> getGameParticipants(UserSelectedTeam selectedTeam, int index) {
    List<Long> participants = new ArrayList<>();
    for (int i = 0; i < index; i++) {
      if (i == 0) {
        participants.add(selectedTeam.getParticipantOneId());
      } else if (i == 1) {
        participants.add(selectedTeam.getParticipantTwoId());
      } else if (i == 2) {
        participants.add(selectedTeam.getParticipantThreeId());
      } else if (i == 3) {
        participants.add(selectedTeam.getParticipantFourId());
      } else if (i == 4) {
        participants.add(selectedTeam.getParticipantFiveId());
      } else if (i == 5) {
        participants.add(selectedTeam.getParticipantSixId());
      } else if (i == 6) {
        participants.add(selectedTeam.getParticipantSevenId());
      } else if (i == 7) {
        participants.add(selectedTeam.getParticipantEightId());
      }
    }
    return participants;
  }
}
