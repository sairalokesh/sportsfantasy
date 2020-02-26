package com.sports.fantasy.domain;

import java.util.List;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameQuestions;

public class GameSelectedAmount {

	private boolean phonevalid;
	private List<AmountEntries> amountEntries;
	private GameQuestions gameQuestion;
	private Long questionId;
	private String gameType;

	/**
	 * @return the phonevalid
	 */
	public boolean isPhonevalid() {
		return phonevalid;
	}

	/**
	 * @param phonevalid
	 *            the phonevalid to set
	 */
	public void setPhonevalid(boolean phonevalid) {
		this.phonevalid = phonevalid;
	}

	/**
	 * @return the amountEntries
	 */
	public List<AmountEntries> getAmountEntries() {
		return amountEntries;
	}

	/**
	 * @param amountEntries
	 *            the amountEntries to set
	 */
	public void setAmountEntries(List<AmountEntries> amountEntries) {
		this.amountEntries = amountEntries;
	}

	/**
	 * @return the gameQuestion
	 */
	public GameQuestions getGameQuestion() {
		return gameQuestion;
	}

	/**
	 * @param gameQuestion
	 *            the gameQuestion to set
	 */
	public void setGameQuestion(GameQuestions gameQuestion) {
		this.gameQuestion = gameQuestion;
	}

	/**
	 * @return the questionId
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the gameType
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * @param gameType
	 *            the gameType to set
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

}
