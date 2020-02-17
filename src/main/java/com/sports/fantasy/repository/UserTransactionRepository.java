package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.UserTransactions;

@Repository
public interface UserTransactionRepository extends CrudRepository<UserTransactions, Long> {

	@Query(value = "select t FROM UserTransactions as t where t.user.id = ?1 order by t.transactionDate desc")
	Page<UserTransactions> getRecentTransactionsByUserId(Long userId, Pageable pageable);
	
	@Query(value = "select t FROM UserTransactions as t where t.user.id = ?1 and t.transactionType = ?2 order by t.transactionDate desc")
    List<UserTransactions> getAllUserPaymentTransactions(Long userId, String transactionType);

}
