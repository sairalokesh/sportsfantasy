package com.sports.fantasy.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.QuestionParticipants;
import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.repository.GameParticipantPointsRepository;
import com.sports.fantasy.repository.GameParticipantRepository;
import com.sports.fantasy.service.GameParticipantService;

@Service
@Transactional
public class GameParticipantServiceImpl implements GameParticipantService {

	@Autowired
	private GameParticipantRepository gameParticipantRepository;
	@Autowired
	private GameParticipantPointsRepository gameParticipantPointsRepository;

	@Override
	public QuestionParticipants getAllParticipantsByQuestionId(Long questionId) {
		List<GameParticipants> gameParticipants = gameParticipantRepository.getAllParticipantsByQuestionId(questionId);
		QuestionParticipants questionParticipants = new QuestionParticipants();
		questionParticipants.setQuestionId(questionId);
		if (gameParticipants != null && !gameParticipants.isEmpty()) {
			questionParticipants.setGameParticipants(gameParticipants);
		} else {
			questionParticipants.setGameParticipants(new ArrayList<GameParticipants>());
		}

		return questionParticipants;
	}

	@Override
	public void updateQuestionParticipants(QuestionParticipants questionParticipants) {
		if (questionParticipants != null && questionParticipants.getGameParticipants() != null
				&& !questionParticipants.getGameParticipants().isEmpty()) {
			Collection<Long> participantIds = new ArrayList<>();
			for (GameParticipants gameParticipants : questionParticipants.getGameParticipants()) {
				if (gameParticipants != null && gameParticipants.getId() != null) {
					participantIds.add(gameParticipants.getId());
				}
			}
			if (participantIds == null || participantIds.isEmpty()) {
				participantIds.add(0L);
			}
			gameParticipantRepository.deleteQuestionParticipants(participantIds, questionParticipants.getQuestionId());
			gameParticipantPointsRepository.deleteParticipantPoints(participantIds,
					questionParticipants.getQuestionId());
			for (GameParticipants gameParticipants : questionParticipants.getGameParticipants()) {
				if (gameParticipants != null && StringUtils.hasText(gameParticipants.getParticipantName())) {
					if (gameParticipants.getGameQuestions() != null
							&& gameParticipants.getGameQuestions().getId() != null) {
						GameParticipants dbGameParticipants = gameParticipantRepository.save(gameParticipants);
						saveParticipantsPoints(dbGameParticipants);
					} else {
						gameParticipants.setGameQuestions(new GameQuestions(questionParticipants.getQuestionId()));
						GameParticipants dbGameParticipants = gameParticipantRepository.save(gameParticipants);
						saveParticipantsPoints(dbGameParticipants);
					}
				}
			}
		}
	}

	private void saveParticipantsPoints(GameParticipants dbGameParticipants) {
		if (dbGameParticipants != null && dbGameParticipants.getId() != null
				&& dbGameParticipants.getGameQuestions() != null
				&& dbGameParticipants.getGameQuestions().getId() != null) {
			GameParticipantPoints gameParticipantPoints = gameParticipantPointsRepository
					.findParticipantPointsByQuestionIdAndParticipantId(dbGameParticipants.getId(),
							dbGameParticipants.getGameQuestions().getId());
			if (gameParticipantPoints == null) {
				GameParticipantPoints saveGameParticipantPoints = new GameParticipantPoints();
				saveGameParticipantPoints.setBowleds(0);
				saveGameParticipantPoints.setCatches(0);
				saveGameParticipantPoints.setFifties(0);
				saveGameParticipantPoints.setFours(0);
				saveGameParticipantPoints.setGameParticipants(new GameParticipants(dbGameParticipants.getId()));
				saveGameParticipantPoints.setGameQuestions(new GameQuestions(dbGameParticipants.getGameQuestions().getId()));
				saveGameParticipantPoints.setHundries(0);
				saveGameParticipantPoints.setMaidens(0);
				saveGameParticipantPoints.setRunouts(0);
				saveGameParticipantPoints.setRuns(0);
				saveGameParticipantPoints.setSixes(0);
				saveGameParticipantPoints.setWickets(0);
				saveGameParticipantPoints.setLbws(0);
				saveGameParticipantPoints.setStumpeds(0);
				gameParticipantPointsRepository.save(saveGameParticipantPoints);
			}
		}

	}

	@Override
	public List<GameParticipants> getQuestionParticipantsByQuestionId(Long questionId) {
		return gameParticipantRepository.getAllParticipantsByQuestionId(questionId);
	}

	
	/* user side functionality */
	@Override
	public Map<String, List<GameParticipants>> getAllActiveParticipantsByQuestionId(Long questionId) {
		List<GameParticipants> gameParticipants = gameParticipantRepository.getAllParticipantsByQuestionId(questionId);
		Map<String, List<GameParticipants>> mapPameParticipants = new TreeMap<>();
		if (gameParticipants != null && !gameParticipants.isEmpty()) {
			for (GameParticipants gameParticipant : gameParticipants) {
				if (mapPameParticipants.containsKey(gameParticipant.getParticipantType())) { // Means WK, BATS, BOWL, AR
					List<GameParticipants> list = mapPameParticipants.get(gameParticipant.getParticipantType());
					list.add(gameParticipant);
					mapPameParticipants.put(gameParticipant.getParticipantType(), list);
				} else {
					List<GameParticipants> list = new ArrayList<>();
					list.add(gameParticipant);
					mapPameParticipants.put(gameParticipant.getParticipantType(), list);
				}
			}
		}
		return mapPameParticipants;
	}

	@Override
	public List<String> getAllParticipantTypesByQuestionId(Long questionId) {
		return gameParticipantRepository.getAllParticipantTypesByQuestionId(questionId);
	}

	@Override
	public List<GameParticipants> getAllParticipantsByQuestionIdAndParticipantIds(Long questionId,
			List<Long> participantIds) {
		return gameParticipantRepository.getAllParticipantsByQuestionIdAndParticipantsIds(questionId, participantIds);
	}

	@Override
	public Map<String, List<GameParticipants>> covertObjectToMap(List<GameParticipants> gameParticipants) {
		Map<String, List<GameParticipants>> mapPameParticipants = new TreeMap<>();
		if (gameParticipants != null && !gameParticipants.isEmpty()) {
			for (GameParticipants gameParticipant : gameParticipants) {
				if (mapPameParticipants.containsKey(gameParticipant.getParticipantType())) {
					List<GameParticipants> list = mapPameParticipants.get(gameParticipant.getParticipantType());
					list.add(gameParticipant);
					mapPameParticipants.put(gameParticipant.getParticipantType(), list);
				} else {
					List<GameParticipants> list = new ArrayList<>();
					list.add(gameParticipant);
					mapPameParticipants.put(gameParticipant.getParticipantType(), list);
				}
			}
		}
		return mapPameParticipants;
	}

}
