package com.sports.fantasy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
		if(gameParticipantPoints!=null && !gameParticipantPoints.isEmpty()) {
			questionParticipants.setGameParticipantPoints(gameParticipantPoints);
		} else {
			questionParticipants.setGameParticipantPoints(new ArrayList<GameParticipantPoints>());
		}
		
		return questionParticipants;
	}

	@Override
	public void updateQuestionParticipantPoints(QuestionParticipantPoints questionParticipantPoints) {
		if(questionParticipantPoints!=null && questionParticipantPoints.getGameParticipantPoints()!=null && !questionParticipantPoints.getGameParticipantPoints().isEmpty()) {
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
                        (7 * gameParticipantPoints.getCatches()) +
                        (6 * gameParticipantPoints.getStumpeds()) +
                        (6 * gameParticipantPoints.getRunouts()) +
                        (6 * gameParticipantPoints.getBowleds()) + // bowled
                        (6 * gameParticipantPoints.getLbws()); 
          /*
           * if(StringUtils.hasText(gameParticipantPoints.getGameParticipants().getAvailability())
           * &&
           * gameParticipantPoints.getGameParticipants().getAvailability().equalsIgnoreCase("YES"))
           * { score = score + 2; }
           */
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
