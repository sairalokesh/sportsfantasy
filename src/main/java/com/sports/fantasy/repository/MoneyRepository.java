package com.sports.fantasy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.GameMoney;

@Repository
public interface MoneyRepository extends CrudRepository<GameMoney, Long> {

  @Query("select m from GameMoney as m where m.amount = ?1 and m.persons = ?2")
  GameMoney findByAmountAndPersons(Double amount, Integer persons);

}
