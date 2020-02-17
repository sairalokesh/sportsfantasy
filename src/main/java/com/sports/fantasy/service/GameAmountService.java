package com.sports.fantasy.service;

import java.util.List;

import com.sports.fantasy.model.AmountEntries;

public interface GameAmountService {
	
	public List<AmountEntries> findAllAmountEntries();
	public AmountEntries saveAmountEntry(AmountEntries amountEntry);
	public AmountEntries findByAmountId(Long id);
	public List<AmountEntries> findAllActiveAmountEntries();
}
