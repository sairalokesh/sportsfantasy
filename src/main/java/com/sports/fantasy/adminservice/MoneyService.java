package com.sports.fantasy.adminservice;

import java.util.List;
import com.sports.fantasy.domain.GameMoneyData;
import com.sports.fantasy.model.GameMoneyRange;

public interface MoneyService {

  List<GameMoneyRange> getGameMoneyRange(Double amount, Integer persons);
  void updateQuestionParticipants(GameMoneyData gameMoneyData);

}
