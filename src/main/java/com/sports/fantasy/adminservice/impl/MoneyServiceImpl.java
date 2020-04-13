package com.sports.fantasy.adminservice.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.adminservice.MoneyService;
import com.sports.fantasy.domain.GameMoneyData;
import com.sports.fantasy.model.GameMoney;
import com.sports.fantasy.model.GameMoneyRange;
import com.sports.fantasy.repository.MoneyRangeRepository;
import com.sports.fantasy.repository.MoneyRepository;

@Service
@Transactional
public class MoneyServiceImpl implements MoneyService {

  @Autowired
  private MoneyRepository moneyRepository;

  @Autowired
  private MoneyRangeRepository moneyRangeRepository;

  @Override
  public List<GameMoneyRange> getGameMoneyRange(Double amount, Integer persons) {
    return moneyRangeRepository.getGameMoneyRange(amount, persons);
  }

  @Override
  public void updateQuestionParticipants(GameMoneyData gameMoneyData) {
    if (gameMoneyData != null && gameMoneyData.getAmount() != null
        && gameMoneyData.getPersons() != null && gameMoneyData.getGameMoneyRanges() != null
        && gameMoneyData.getGameMoneyRanges().size() > 0) {
      GameMoney gameMoney = moneyRepository.findByAmountAndPersons(gameMoneyData.getAmount(),
          gameMoneyData.getPersons());
      if (gameMoney == null) {
        gameMoney = new GameMoney();
        gameMoney.setAmount(gameMoneyData.getAmount());
        gameMoney.setPersons(gameMoneyData.getPersons());
        gameMoney = moneyRepository.save(gameMoney);
      }

      Collection<Long> moneyIds = new ArrayList<>();
      for (GameMoneyRange gameMoneyRange : gameMoneyData.getGameMoneyRanges()) {
        if (gameMoneyRange != null && gameMoneyRange.getId() != null) {
          moneyIds.add(gameMoneyRange.getId());
        }
      }
      if (moneyIds == null || moneyIds.isEmpty()) {
        moneyIds.add(0L);
      }
      moneyRangeRepository.deleteGameMoney(gameMoney.getId(), moneyIds);


      for (GameMoneyRange gameMoneyRange : gameMoneyData.getGameMoneyRanges()) {
        if (gameMoneyRange != null) {
          if (gameMoneyRange.getGameMoney() == null) {
            gameMoneyRange.setGameMoney(gameMoney);
          }
          moneyRangeRepository.save(gameMoneyRange);

        }
      }
    }
  }

}
