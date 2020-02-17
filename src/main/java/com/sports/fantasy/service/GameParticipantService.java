package com.sports.fantasy.service;

import java.util.List;
import java.util.Map;

import com.sports.fantasy.domain.QuestionParticipants;
import com.sports.fantasy.model.GameParticipants;

public interface GameParticipantService {

	public QuestionParticipants getAllParticipantsByQuestionId(Long questionId);
	public void updateQuestionParticipants(QuestionParticipants questionParticipants);
	public List<GameParticipants> getQuestionParticipantsByQuestionId(Long questionId);
	
	public Map<String, List<GameParticipants>> getAllActiveParticipantsByQuestionId(Long questionId);
	public List<String> getAllParticipantTypesByQuestionId(Long questionId);
	public List<GameParticipants> getAllParticipantsByQuestionIdAndParticipantIds(Long questionId, List<Long> selectedMembers);
	public Map<String, List<GameParticipants>> covertObjectToMap(List<GameParticipants> gamePlayers);

}
