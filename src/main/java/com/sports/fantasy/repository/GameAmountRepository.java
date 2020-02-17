package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.AmountEntries;

@Repository
public interface GameAmountRepository extends CrudRepository<AmountEntries, Long> {

  @Query("Select a from AmountEntries as a order by a.amount asc")
  List<AmountEntries> findAllAmountEntries();

  @Query("Select a from AmountEntries as a where a.active = ?1 order by a.amount asc")
  List<AmountEntries> findAllActiveAmountEntries(boolean isActive);

}
