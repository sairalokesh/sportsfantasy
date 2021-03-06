package com.sports.fantasy.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.model.UserDashboardTeamParticipants;
import com.sports.fantasy.model.UserSelectedTeamsScore;
import com.sports.fantasy.model.UserTeamParticipants;
import com.sports.fantasy.model.UsersParticipantsScore;
import com.sports.fantasy.model.UsersSelectedParticipnatsScore;

@Repository
@SuppressWarnings("unchecked")
public class UserTeamDaoImpl implements UserTeamDao {
  
  @PersistenceContext   
  private EntityManager entityManager;

  @Override
  public List<UserTeamParticipants> getTeamsEditParticipants(Long questionId, Long amountId, Long userId) {
    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("user_team_participants", UserTeamParticipants.class);
   
    // set parameters
    storedProcedure.registerStoredProcedureParameter("question_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("amount_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("user_id", Long.class, ParameterMode.IN);
    
    storedProcedure.setParameter("question_id", questionId);
    storedProcedure.setParameter("amount_id", amountId);
    storedProcedure.setParameter("user_id", userId);
    
    // execute SP
    storedProcedure.execute();
    
    List<UserTeamParticipants> teamParticipants = (List<UserTeamParticipants>) storedProcedure.getResultList();
    return teamParticipants;
  }

  @Override
  public List<UsersParticipantsScore> findParticipantsScoreByQuestionIdAndAmountId(Long questionId, Long amountId, String gametype) {
    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("users_participants_score", UsersParticipantsScore.class);
    // set parameters
    storedProcedure.registerStoredProcedureParameter("question_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("amount_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("game_type", String.class, ParameterMode.IN);
    
    storedProcedure.setParameter("question_id", questionId);
    storedProcedure.setParameter("amount_id", amountId);
    storedProcedure.setParameter("game_type", gametype);
    
    // execute SP
    storedProcedure.execute();
    List<UsersParticipantsScore> usersParticipantsScores = (List<UsersParticipantsScore>)storedProcedure.getResultList();
    return usersParticipantsScores;
  }

  @Override
  public List<UserDashboardTeamParticipants> getRecentParticipantsByUserId(Long userId) {
    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("user_dashboard_team_participants", UserDashboardTeamParticipants.class);
    storedProcedure.registerStoredProcedureParameter("user_id", Long.class, ParameterMode.IN);
    storedProcedure.setParameter("user_id", userId);
    storedProcedure.execute();
    List<UserDashboardTeamParticipants> userDashboardTeamParticipants = (List<UserDashboardTeamParticipants>) storedProcedure.getResultList();
    return userDashboardTeamParticipants;
  }

  @Override
  public List<UserSelectedTeamsScore> findUsersSeletedTeamsScoreByQuestionIdAndAmountId(
      Long questionId, Long amountId, String gametype) {
    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("users_selected_teams_score", UserSelectedTeamsScore.class);
    // set parameters
    storedProcedure.registerStoredProcedureParameter("question_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("amount_id", Long.class, ParameterMode.IN);
    storedProcedure.registerStoredProcedureParameter("game_type", String.class, ParameterMode.IN);
    
    storedProcedure.setParameter("question_id", questionId);
    storedProcedure.setParameter("amount_id", amountId);
    storedProcedure.setParameter("game_type", gametype);
    
    // execute SP
    storedProcedure.execute();
    List<UserSelectedTeamsScore> userSelectedTeamsScores = (List<UserSelectedTeamsScore>)storedProcedure.getResultList();
    return userSelectedTeamsScores;
  }

  @Override
  public List<UsersSelectedParticipnatsScore> getAllParticipantsScoreByQuestionId(Long questionId) {
    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("users_selected_participnats_score", UsersSelectedParticipnatsScore.class);
    
    // set parameters
    storedProcedure.registerStoredProcedureParameter("question_id", Long.class, ParameterMode.IN);
    storedProcedure.setParameter("question_id", questionId);
    
    // execute SP
    storedProcedure.execute();
    List<UsersSelectedParticipnatsScore> userSelectedTeamsScores = (List<UsersSelectedParticipnatsScore>)storedProcedure.getResultList();
    return userSelectedTeamsScores;
  }

}
