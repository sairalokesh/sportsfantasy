package com.sports.fantasy.userservice.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UsersParticipantsScore;
import com.sports.fantasy.userservice.UserRankingService;

@Service
@Transactional
public class UserRankingServiceImpl implements UserRankingService {
  
  @Autowired
  private UserTeamDao userTeamDao;

  @Override
  public List<Ranking> getSelectedParticipantsScore(Long questionId, Long amountId, String gametype, UserInfo user) {
    List<UsersParticipantsScore> usersParticipantsScore =  userTeamDao.findParticipantsScoreByQuestionIdAndAmountId(questionId, amountId, gametype);
    List<Ranking> userRank = new ArrayList<>();
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
            userRank.addAll(ranking);
        }
    }
    return userRank;
  }

}
