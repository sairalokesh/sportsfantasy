package com.sports.fantasy.userservice;

import java.util.List;
import java.util.Map;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.domain.UserPoints;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.UserSelectedTeam;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UserTempParticipants;

public interface UserSelectedTeamService {

  public UserSelectedTeam getUserSelectTeam(UserTempParticipants userTempParticipants);
  public UserSelectedTeam save(UserSelectedTeam selectedTeam);
  
  public List<SelectedTeam> getTeamSelectedEditParticipants(Long userId, String gameType, boolean isActive);
  public List<UserTeamParticipants> getTeamsEditParticipants(Long questionId, Long amountId, Long userId);
  UserSelectedTeam findByTeamId(Long teamId, Long userId);
  public List<SelectedTeam> getTeamSelectedLiveParticipants(Long userId, String gametype, boolean isActive);
  public List<SelectedTeam> getTeamSelectedCompletedParticipants(Long userId, String gametype, boolean isActive);
  public UserSelectedTeam getSelectedUserTeam(Long teamId, Long questionId, Long amountId, Long userId);
  public Map<String, List<GameParticipantScore>> getAllParticipantsScores(UserSelectedTeam userSelectedTeam, Long questionId);
  public UserPoints getSelectedParticipantScores(UserSelectedTeam userSelectedTeam, Long participantId, Long questionId);

}
