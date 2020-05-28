package com.sports.fantasy.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.UserAmount;

@Repository
public interface UserAmountRepository extends CrudRepository<UserAmount, Long> {

  @Query("Select a from UserAmount as a  where a.user.id = ?1")
  UserAmount getUserAmount(Long userId);

  @Modifying
  @Query("update UserAmount set addedAmount = addedAmount + ?2 where user.id = ?1")
  void updateUserAmount(Long userId, Double spiltAmount);

}
