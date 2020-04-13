package com.sports.fantasy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.UserWithdrawAmount;

@Repository
public interface UserWithDrawRepository extends CrudRepository<UserWithdrawAmount, Long> {

  @Query("select u from UserWithdrawAmount as u where u.user.id = ?1 order by u.id asc")
  List<UserWithdrawAmount> getUserWithDraws(Long userId);

  @Query("select u from UserWithdrawAmount as u where u.status = ?1 order by u.id asc")
  List<UserWithdrawAmount> getPendingWithDraws(String status);

}
