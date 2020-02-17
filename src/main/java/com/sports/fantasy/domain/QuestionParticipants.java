package com.sports.fantasy.domain;

import java.util.ArrayList;
import java.util.List;

import com.sports.fantasy.model.GameParticipants;

public class QuestionParticipants {

	private Long questionId;
	private List<GameParticipants> gameParticipants = new ArrayList<GameParticipants>();

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<GameParticipants> getGameParticipants() {
		return gameParticipants;
	}

	public void setGameParticipants(List<GameParticipants> gameParticipants) {
		this.gameParticipants = gameParticipants;
	}

}
