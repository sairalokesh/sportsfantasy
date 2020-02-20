package com.sports.fantasy.dao;

import java.util.List;
import com.sports.fantasy.model.UserDashboardTeamParticipants;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UsersParticipantsScore;

public interface UserTeamDao {

  List<UserTeamParticipants> getTeamsEditParticipants(Long questionId, Long amountId, Long userId);
  List<UsersParticipantsScore> findParticipantsScoreByQuestionIdAndAmountId(Long questionId, Long amountId, String gametype);
  List<UserDashboardTeamParticipants> getRecentParticipantsByUserId(Long userId);

}
