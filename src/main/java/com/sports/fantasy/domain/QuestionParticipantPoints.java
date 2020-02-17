package com.sports.fantasy.domain;

import java.util.ArrayList;
import java.util.List;

import com.sports.fantasy.model.GameParticipantPoints;

public class QuestionParticipantPoints {

	private Long questionId;
	private List<GameParticipantPoints> gameParticipantPoints = new ArrayList<GameParticipantPoints>();

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<GameParticipantPoints> getGameParticipantPoints() {
		return gameParticipantPoints;
	}

	public void setGameParticipantPoints(List<GameParticipantPoints> gameParticipantPoints) {
		this.gameParticipantPoints = gameParticipantPoints;
	}

}
