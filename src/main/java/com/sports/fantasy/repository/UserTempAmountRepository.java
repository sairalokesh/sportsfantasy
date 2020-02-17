package com.sports.fantasy.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.UserTempAmount;

@Repository
public interface UserTempAmountRepository extends CrudRepository<UserTempAmount, Long> {

	@Query("select utp from UserTempAmount as utp where utp.orderId = ?1")
	UserTempAmount getUserTempAmount(String orderId);

	@Modifying
	@Query("delete from UserTempAmount as p where p.orderId = ?1")
	void deleteTempUserSelectedAmount(String orderId);

}
