package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.UserAccount;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

	@Query("select u from UserAccount as u where u.user.id = ?1")
	UserAccount getUserAccountInfo(Long userId);
	
	@Query("select u from UserAccount as u where u.accountNumber = ?1")
	UserAccount getAccountByAccountId(String accountNumber);

	@Query("select u.status from UserAccount as u where u.user.id = ?1")
	String getAccountStatusByUserId(Long userId);

	@Query("select u from UserAccount as u where u.status = ?1 or u.status is null order by u.id asc")
	List<UserAccount> getpendingaccounts(String status);
}
