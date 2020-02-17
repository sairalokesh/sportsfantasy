package com.sports.fantasy.userservice;

import java.util.List;

import com.sports.fantasy.model.UserTransactions;

public interface UserDashboardService {

	Long getCountOfGameEntries(Long userId);
	Double getSumOfEntriesAmount(Long userId);
	Long getCountOfWinningEntries(Long userId);
	Double getSumOfWinningAmount(Long userId);
	String getAccountStatusByUserId(Long userId);
	List<UserTransactions> getRecentTransactionsByUserId(Long userId);

}
