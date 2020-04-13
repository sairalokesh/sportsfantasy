package com.sports.fantasy.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.GameMoneyRange;

@Repository
public interface MoneyRangeRepository extends CrudRepository<GameMoneyRange, Long> {

  @Query("select m from GameMoneyRange as m where m.gameMoney.amount = ?1 and m.gameMoney.persons = ?2")
  List<GameMoneyRange> getGameMoneyRange(Double amount, Integer persons);

  @Modifying
  @Query("delete from GameMoneyRange as g where g.gameMoney.id = ?1 and g.id not in ?2")
  void deleteGameMoney(Long id, Collection<Long> moneyIds);

}
