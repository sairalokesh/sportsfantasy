package com.sports.fantasy.userservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sports.fantasy.model.UserAccount;
import com.sports.fantasy.repository.UserAccountRepository;
import com.sports.fantasy.userservice.UserAccountService;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public UserAccount getUserAccountInfo(Long userId) {
		return userAccountRepository.getUserAccountInfo(userId);
	}

	@Override
	public UserAccount getAccountByAccountId(String accountNumber) {
		return userAccountRepository.getAccountByAccountId(accountNumber);
	}

	@Override
	public UserAccount updateUserAccount(UserAccount account) {
		return userAccountRepository.save(account);
	}

}
