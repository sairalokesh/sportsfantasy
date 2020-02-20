package com.sports.fantasy.userservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.model.UserDashboardTeamParticipants;
import com.sports.fantasy.model.UserTransactions;
import com.sports.fantasy.repository.UserAccountRepository;
import com.sports.fantasy.repository.UserSelectedTeamRepository;
import com.sports.fantasy.repository.UserTransactionRepository;
import com.sports.fantasy.userservice.UserDashboardService;

@Service
@Transactional
public class UserDashboardServiceImpl implements UserDashboardService {

	@Autowired
	private UserSelectedTeamRepository userSelectedTeamRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private UserTransactionRepository userTransactionRepository;
	@Autowired
    private UserTeamDao userTeamDao;
	

	@Override
	public Long getCountOfGameEntries(Long userId) {
		return userSelectedTeamRepository.getCountOfGameEntries(userId);
	}

	@Override
	public Double getSumOfEntriesAmount(Long userId) {
		return userSelectedTeamRepository.getSumOfEntriesAmount(userId);
	}

	@Override
	public Long getCountOfWinningEntries(Long userId) {
		return userSelectedTeamRepository.getCountOfWinningEntries(userId, "Winner");
	}

	@Override
	public Double getSumOfWinningAmount(Long userId) {
		return userSelectedTeamRepository.getSumOfWinningAmount(userId, "Winner");
	}

	@Override
	public String getAccountStatusByUserId(Long userId) {
		return userAccountRepository.getAccountStatusByUserId(userId);
	}

	@Override
	public List<UserTransactions> getRecentTransactionsByUserId(Long userId) {
		Page<UserTransactions> transactionsPage = userTransactionRepository.getRecentTransactionsByUserId(userId,PageRequest.of(0, 2));
		return transactionsPage.getContent();
	}

  @Override
  public List<UserDashboardTeamParticipants> getRecentParticipantsByUserId(Long userId) {
    return userTeamDao.getRecentParticipantsByUserId(userId);
  }

}
