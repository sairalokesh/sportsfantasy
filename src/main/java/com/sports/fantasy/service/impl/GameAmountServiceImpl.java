package com.sports.fantasy.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.repository.GameAmountRepository;
import com.sports.fantasy.service.GameAmountService;

@Service
@Transactional
public class GameAmountServiceImpl implements GameAmountService {

  @Autowired
  private GameAmountRepository gameAmountRepository;

  @Override
  public List<AmountEntries> findAllAmountEntries() {
    return gameAmountRepository.findAllAmountEntries();
  }

  @Override
  public AmountEntries saveAmountEntry(AmountEntries amountEntry) {
    return gameAmountRepository.save(amountEntry);
  }

  @Override
  public AmountEntries findByAmountId(Long id) {
    Optional<AmountEntries> dbAmountEntries = gameAmountRepository.findById(id);
    if (dbAmountEntries.isPresent()) {
      return dbAmountEntries.get();
    }
    return null;
  }

  @Override
  public List<AmountEntries> findAllActiveAmountEntries() {
    List<AmountEntries> amountEntries = gameAmountRepository.findAllActiveAmountEntries(true);
    Collections.sort(amountEntries, new Comparator<AmountEntries>() {
      @Override
      public int compare(AmountEntries o1, AmountEntries o2) {
        if (StringUtils.hasText(o1.getAmount()) && StringUtils.hasText(o2.getAmount())) {
          Float amount1 = Float.parseFloat(o1.getAmount());
          Float amount2 = Float.parseFloat(o2.getAmount());
          return amount1.compareTo(amount2);
        }
        return 0;
      }
    });
    return amountEntries;
  }
}
