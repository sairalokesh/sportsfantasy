package com.sports.fantasy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.domain.SelectedTeam;
import com.sports.fantasy.model.UserSelectedTeam;

@Repository
public interface UserSelectedTeamRepository extends CrudRepository<UserSelectedTeam, Long> {

  @Query("select count(p.id) from UserSelectedTeam as p where p.user.id = ?1")
  Long getCountOfGameEntries(Long userId);

  @Query("select sum(p.amountEntries.amount) from UserSelectedTeam as p where p.user.id = ?1")
  Double getSumOfEntriesAmount(Long userId);

  @Query("select count(p.id) from UserSelectedTeam as p where p.user.id = ?1 and p.playerResult = ?2")
  Long getCountOfWinningEntries(Long userId, String status);

  @Query("select sum(p.winningAmount) from UserSelectedTeam as p where p.user.id = ?1 and p.playerResult = ?2")
  Double getSumOfWinningAmount(Long userId, String status);

  @Query("select new com.sports.fantasy.domain.SelectedTeam(ust.amountEntries.id, ust.amountEntries.amount, ust.gameQuestions.id, ust.gameQuestions.teamOne, ust.gameQuestions.teamOneImage, ust.gameQuestions.teamTwo, ust.gameQuestions.teamTwoImage, count(ust.id), ust.id, ust.gameQuestions.questionEffect, ust.gameQuestions.validDate) from UserSelectedTeam as ust where ust.user.id = ?1 and ust.gameQuestions.active = ?3 and ust.gameQuestions.questionType = ?2 and ust.gameQuestions.validDate >= now() group by ust.amountEntries.id, ust.gameQuestions.id order by ust.gameQuestions.validDate asc")
  List<SelectedTeam> getTeamSelectedEditParticipants(Long userId, String gameType, boolean isActive);
  
  @Query("select ust from UserSelectedTeam as ust where ust.id = ?1 and ust.user.id = ?2")
  UserSelectedTeam findByTeamId(Long teamId, Long userId);
  
  @Query("select new com.sports.fantasy.domain.SelectedTeam(ust.amountEntries.id, ust.amountEntries.amount, ust.gameQuestions.id, ust.gameQuestions.teamOne, ust.gameQuestions.teamOneImage, ust.gameQuestions.teamTwo, ust.gameQuestions.teamTwoImage, count(ust.id), ust.id, ust.gameQuestions.questionEffect, ust.gameQuestions.validDate) from UserSelectedTeam as ust where ust.user.id = ?1 and ust.gameQuestions.active = ?3 and ust.gameQuestions.questionType = ?2 and ust.gameQuestions.validDate <= now() and ust.gameQuestions.spinDate >= now() group by ust.amountEntries.id, ust.gameQuestions.id order by ust.gameQuestions.validDate asc")
  List<SelectedTeam> getTeamSelectedLiveParticipants(Long userId, String gametype, boolean isActive);
  
  @Query("select new com.sports.fantasy.domain.SelectedTeam(ust.amountEntries.id, ust.amountEntries.amount, ust.gameQuestions.id, ust.gameQuestions.teamOne, ust.gameQuestions.teamOneImage, ust.gameQuestions.teamTwo, ust.gameQuestions.teamTwoImage, count(ust.id), ust.id, ust.gameQuestions.questionEffect, ust.gameQuestions.validDate) from UserSelectedTeam as ust where ust.user.id = ?1 and ust.gameQuestions.active = ?3 and ust.gameQuestions.questionType = ?2 group by ust.amountEntries.id, ust.gameQuestions.id order by ust.gameQuestions.validDate desc")
  List<SelectedTeam> getTeamSelectedCompletedParticipants(Long userId, String gametype, boolean isActive);

  @Query("select ust from UserSelectedTeam as ust where ust.id = ?1 and ust.gameQuestions.id = ?2 and ust.amountEntries.id = ?3 and ust.user.id = ?4")
  UserSelectedTeam getSelectedUserTeam(Long teamId, Long questionId, Long amountId, Long userId);

  @Query("select count(p.id) from UserSelectedTeam as p where p.gameType = ?1 and p.gameQuestions.id = ?2 and p.amountEntries.id = ?3")
  Long getUsersCount(String gameType, Long questionId, Long amountId);

  @Query("select count(p.id) from UserSelectedTeam as p where p.gameType = ?1 and p.gameQuestions.id = ?2 and p.amountEntries.id = ?3 and p.user.id = ?4")
  Long getSelectedUserCount(String gameType, Long questionId, Long amountId, Long userId);

  @Query("select new com.sports.fantasy.domain.SelectedTeam(ust.amountEntries.id, ust.amountEntries.amount, ust.gameQuestions.id, ust.gameQuestions.teamOne, ust.gameQuestions.teamOneImage, ust.gameQuestions.teamTwo, ust.gameQuestions.teamTwoImage, count(ust.id), ust.id, ust.gameQuestions.questionEffect, ust.gameQuestions.validDate) from UserSelectedTeam as ust where ust.gameQuestions.active = ?2 and ust.gameQuestions.questionType = ?1 and ust.gameQuestions.spinDate <= now() group by ust.amountEntries.id, ust.gameQuestions.id order by ust.gameQuestions.validDate asc")
  List<SelectedTeam> getTeamSettlements(String gametype, boolean isActive);

  @Modifying
  @Query("update UserSelectedTeam set playerResult = 'Winning', winningAmount = ?6, paymentDone = 'Done' where amountEntries.id = ?4 and gameQuestions.id = ?3 and id = ?1 and paymentDone = 'Not_Done' and user.id = ?2 and gameType = ?5")
  void updateUserSelectedTeam(Long id, Long userId, Long questionId, Long amountId, String gametype, Double spiltAmount);
  
}
