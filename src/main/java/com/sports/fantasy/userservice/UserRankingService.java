package com.sports.fantasy.userservice;

import java.util.List;
import com.sports.fantasy.domain.Ranking;
import com.sports.fantasy.model.UserInfo;

public interface UserRankingService {

  List<Ranking> getSelectedParticipantsScore(Long questionId, Long amountId, String gametype, UserInfo user, boolean isRequired);

}
