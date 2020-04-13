package com.sports.fantasy.userservice.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserSelectedTeamsScore;
import com.sports.fantasy.model.UsersParticipantsScore;
import com.sports.fantasy.model.UsersSelectedParticipnatsScore;
import com.sports.fantasy.userservice.UserRankingService;

@Service
@Transactional
public class UserRankingServiceImpl implements UserRankingService {
  
  @Autowired
  private UserTeamDao userTeamDao;

  @Override
  public List<Ranking> getSelectedParticipantsScore(Long questionId, Long amountId, String gametype, UserInfo user) {
    // List<UsersParticipantsScore> usersParticipantsScore =  userTeamDao.findParticipantsScoreByQuestionIdAndAmountId(questionId, amountId, gametype);
    List<Ranking> userRank = new ArrayList<>();
    List<UserSelectedTeamsScore> userSelectedTeamsScores = userTeamDao.findUsersSeletedTeamsScoreByQuestionIdAndAmountId(questionId, amountId, gametype);
    if(userSelectedTeamsScores != null && !userSelectedTeamsScores.isEmpty()) {
      List<UsersSelectedParticipnatsScore> usersSelectedParticipnatsScores = userTeamDao.getAllParticipantsScoreByQuestionId(questionId);
      if(usersSelectedParticipnatsScores != null && !usersSelectedParticipnatsScores.isEmpty()) {
        Map<Long, Double> scores = getParticipantsScore(usersSelectedParticipnatsScores);
        if(scores != null && scores.size() > 0) {
          List<UsersParticipantsScore> usersParticipantsScore = getUsersTeamsScore(userSelectedTeamsScores, scores);
          if(usersParticipantsScore!=null && !usersParticipantsScore.isEmpty()) {
            double[] score = { Integer.MIN_VALUE };
            int[] no = { 0 };
            int[] rank = { 0 };
            
            List<Ranking> ranking = usersParticipantsScore.stream().sorted((a, b) -> b.getTotalscore().compareTo(a.getTotalscore())).map(p -> {
                ++no[0];
                if (score[0] != p.getTotalscore())
                    rank[0] = no[0];
                if(p.getUserid() == user.getId()) {
                    userRank.add(new Ranking(rank[0], score[0] = p.getTotalscore(), p.getUsername(), p.getUserid(), p.getAmountid(), p.getQuestionid(), p.getId(), p.getWinningamount(), p.getWinningresult(), p.getImageurl()));
                    return null;
                } else {
                    return new Ranking(rank[0], score[0] = p.getTotalscore(), p.getUsername(), p.getUserid(), p.getAmountid(), p.getQuestionid(), p.getId(), p.getWinningamount(), p.getWinningresult(), p.getImageurl());
                }
                
            }).collect(Collectors.toList());
            
            if(ranking!=null && !ranking.isEmpty()) {
                 ranking.removeAll(Collections.singletonList(null));
                 List<Ranking> sortedRanking =  ranking.stream().limit(100).collect(Collectors.toList());
                userRank.addAll(sortedRanking);
            }
          }
        }
      }
    }
    return userRank;
  }

  private List<UsersParticipantsScore> getUsersTeamsScore(List<UserSelectedTeamsScore> userSelectedTeamsScores, Map<Long, Double> scores) {
    List<UsersParticipantsScore> participantsScores = new ArrayList<>();
    for(UserSelectedTeamsScore userSelectedTeamsScore: userSelectedTeamsScores) {
      UsersParticipantsScore participantsScore = new UsersParticipantsScore();
      participantsScore.setAmountid(userSelectedTeamsScore.getAmountid());
      participantsScore.setId(userSelectedTeamsScore.getId());
      participantsScore.setImageurl(userSelectedTeamsScore.getImageurl());
      participantsScore.setQuestionid(userSelectedTeamsScore.getQuestionid());
      participantsScore.setUserid(userSelectedTeamsScore.getUserid());
      participantsScore.setUsername(userSelectedTeamsScore.getUsername());
      participantsScore.setWinningamount(userSelectedTeamsScore.getWinningamount());
      participantsScore.setWinningresult(userSelectedTeamsScore.getWinningresult());
      double score = 0.00;
      score = score + (3 * scores.get(userSelectedTeamsScore.getCaptainid())); 
      score = score + (2 * scores.get(userSelectedTeamsScore.getVicecaptainid())); 
      score = score + (1.5 * scores.get(userSelectedTeamsScore.getSuppoterid())); 
      score = score + scores.get(userSelectedTeamsScore.getParticipantoneid());
      score = score + scores.get(userSelectedTeamsScore.getParticipanttwoid());
      score = score + scores.get(userSelectedTeamsScore.getParticipantthreeid());
      score = score + scores.get(userSelectedTeamsScore.getParticipantfourid());
      score = score + scores.get(userSelectedTeamsScore.getParticipantfiveid());
      score = score + scores.get(userSelectedTeamsScore.getParticipantsixid());
      score = score + scores.get(userSelectedTeamsScore.getParticipantsevenid());
      score = score + scores.get(userSelectedTeamsScore.getParticipanteightid());
      participantsScore.setTotalscore(score);
      participantsScores.add(participantsScore);
    }
    return participantsScores;
  }

  private Map<Long, Double> getParticipantsScore(List<UsersSelectedParticipnatsScore> usersSelectedParticipnatsScores) {
    Map<Long, Double> scores = new TreeMap<>();
    for(UsersSelectedParticipnatsScore participnatsScore : usersSelectedParticipnatsScores) {
      scores.put(participnatsScore.getParticipantid(), participnatsScore.getScore());
    }
    return scores;
  }

}
