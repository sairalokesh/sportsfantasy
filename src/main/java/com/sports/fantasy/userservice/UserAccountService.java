package com.sports.fantasy.userservice;

import com.sports.fantasy.model.UserAccount;

public interface UserAccountService {

	UserAccount getUserAccountInfo(Long userId);
	UserAccount getAccountByAccountId(String accountNumber);
	UserAccount updateUserAccount(UserAccount account);

}
