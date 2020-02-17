package com.sports.fantasy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sports.fantasy.domain.QuestionParticipantPoints;
import com.sports.fantasy.model.GameParticipantPoints;
import com.sports.fantasy.model.GameParticipantScore;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.repository.GameParticipantPointsRepository;
import com.sports.fantasy.repository.GameParticipantScoreRepository;
import com.sports.fantasy.service.GameParticpantPointsService;

@Service
@Transactional
public class GameParticpantPointsServiceImpl implements GameParticpantPointsService {
	
	@Autowired private GameParticipantPointsRepository gameParticipantPointsRepository;
	@Autowired private GameParticipantScoreRepository gameParticipantScoreRepository;
	

	@Override
	public QuestionParticipantPoints getAllParticipantpointsByQuestionId(Long questionId) {
		List<GameParticipantPoints> gameParticipantPoints = gameParticipantPointsRepository.getAllParticipantPointsByQuestionId(questionId);
		QuestionParticipantPoints questionParticipants = new QuestionParticipantPoints();
		questionParticipants.setQuestionId(questionId);
		if(gameParticipantPoints!=null && gameParticipantPoints.size()>0) {
			questionParticipants.setGameParticipantPoints(gameParticipantPoints);
		} else {
			questionParticipants.setGameParticipantPoints(new ArrayList<GameParticipantPoints>());
		}
		
		return questionParticipants;
	}

	@Override
	public void updateQuestionParticipantPoints(QuestionParticipantPoints questionParticipantPoints) {
		if(questionParticipantPoints!=null && questionParticipantPoints.getGameParticipantPoints()!=null && questionParticipantPoints.getGameParticipantPoints().size()>0) {
			for(GameParticipantPoints gameParticipantPoints : questionParticipantPoints.getGameParticipantPoints()) {
				if(gameParticipantPoints.getGameParticipants()!=null && gameParticipantPoints.getGameParticipants().getId()!=null && gameParticipantPoints.getGameQuestions()!=null && gameParticipantPoints.getGameQuestions().getId()!=null) {
					gameParticipantPointsRepository.save(gameParticipantPoints);
					GameParticipantScore gameParticipantScore = new GameParticipantScore();
					double score = 
							(1 * gameParticipantPoints.getRuns()) + 
							(1 * gameParticipantPoints.getFours()) + 
							(2 * gameParticipantPoints.getSixes()) + 
							(6 * gameParticipantPoints.getFifties()) + 
							(12 * gameParticipantPoints.getHundries()) + 
							(10 * gameParticipantPoints.getWickets()) + 
							(5 * gameParticipantPoints.getMaidens()) + 
							(6 * gameParticipantPoints.getBowlings()) + // bowled
							(7 * gameParticipantPoints.getCatches()) + 
							(1 * gameParticipantPoints.getRunouts()); // thrower - 3, stumped - 2 
					if(StringUtils.hasText(gameParticipantPoints.getGameParticipants().getAvailability()) && gameParticipantPoints.getGameParticipants().getAvailability().equalsIgnoreCase("YES")) {
						score = score + 2;
					}
					gameParticipantScore.setScore(score);
					double captainScore = 3 * score;
					gameParticipantScore.setCaptainScore(captainScore);
					double viceCaptainScore = 2 * score;
					gameParticipantScore.setViceCaptainScore(viceCaptainScore);
					double suppoterScore = (1.5) * score;
					gameParticipantScore.setSuppoterScore(suppoterScore);
					gameParticipantScore.setGameQuestions(new GameQuestions(questionParticipantPoints.getQuestionId()));
					gameParticipantScore.setGameParticipants(gameParticipantPoints.getGameParticipants());
					GameParticipantScore dbGameParticipantScore = gameParticipantScoreRepository.findByQuestionIdAndParticipantId(questionParticipantPoints.getQuestionId(), gameParticipantPoints.getGameParticipants().getId());
					if(dbGameParticipantScore!=null && dbGameParticipantScore.getId()!=null) {
						gameParticipantScore.setId(dbGameParticipantScore.getId());
					}
					gameParticipantScoreRepository.save(gameParticipantScore);
				}
			}
		}
	}
}
