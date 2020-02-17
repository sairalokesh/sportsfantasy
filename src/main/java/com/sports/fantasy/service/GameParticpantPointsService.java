package com.sports.fantasy.service;

import com.sports.fantasy.domain.QuestionParticipantPoints;

public interface GameParticpantPointsService {

	public QuestionParticipantPoints getAllParticipantpointsByQuestionId(Long questionId);
	public void updateQuestionParticipantPoints(QuestionParticipantPoints questionParticipantPoints);

}
