package com.sports.fantasy.service;

import java.text.ParseException;
import java.util.List;
import com.sports.fantasy.api.ApiTeams;
import com.sports.fantasy.model.GameQuestions;

public interface GameQuestionsService {

  List<GameQuestions> findAllGameQuestions();
  GameQuestions saveGameQuestion(GameQuestions gameQuestions);
  GameQuestions findGameQuestionById(Long id);
  GameQuestions updateGameQuestion(GameQuestions gameQuestions);
  List<GameQuestions> findAllGameQuestions(boolean isActive);
  List<GameQuestions> getGameQuestionsByGreaterthanCurrentDate(String gameType);
  GameQuestions getGameQuestionByQuestionId(Long questionId, String gameType);
  void saveApiTeams(List<ApiTeams> teams)  throws ParseException;
  GameQuestions getCompletedGameQuestionByQuestionId(Long questionId, String gametype);
  List<GameQuestions> getGameQuestionsByGreaterthanCurrentDatewithLimit(String gameType);



}
