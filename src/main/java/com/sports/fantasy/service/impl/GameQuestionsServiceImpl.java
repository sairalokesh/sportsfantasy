package com.sports.fantasy.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.api.ApiTeams;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.repository.GameQuestionsRepository;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.DateUtil;

@Service
@Transactional
public class GameQuestionsServiceImpl implements GameQuestionsService {

  @Autowired
  private GameQuestionsRepository gameQuestionsRepository;

  @Override
  public List<GameQuestions> findAllGameQuestions() {
    return gameQuestionsRepository.findAllGameQuestions();
  }

  @Override
  public GameQuestions saveGameQuestion(GameQuestions gameQuestions) {
    if (StringUtils.hasText(gameQuestions.getExValidDate())) {
      Date validDate = DateUtil.stringToDate(gameQuestions.getExValidDate());
      gameQuestions.setValidDate(validDate);
    }

    if (StringUtils.hasText(gameQuestions.getExSpinDate())) {
      Date spinDate = DateUtil.stringToDate(gameQuestions.getExSpinDate());
      gameQuestions.setSpinDate(spinDate);
    }
    gameQuestions.setCreatedDate(new Date());
    return gameQuestionsRepository.save(gameQuestions);
  }

  @Override
  public GameQuestions findGameQuestionById(Long id) {
    GameQuestions gameQuestions = new GameQuestions();
    Optional<GameQuestions> dbGameQuestions = gameQuestionsRepository.findById(id);
    if (dbGameQuestions.isPresent()) {
      gameQuestions = dbGameQuestions.get();
    }

    if (gameQuestions != null && gameQuestions.getValidDate() != null) {
      String validDate = DateUtil.dateToString(gameQuestions.getValidDate());
      gameQuestions.setExValidDate(validDate);
    }

    if (gameQuestions != null && gameQuestions.getSpinDate() != null) {
      String spinDate = DateUtil.dateToString(gameQuestions.getSpinDate());
      gameQuestions.setExSpinDate(spinDate);
    }

    if (gameQuestions != null && gameQuestions.getCreatedDate() != null) {
      String createdDate = DateUtil.dateToString(gameQuestions.getCreatedDate());
      gameQuestions.setExCreatedDate(createdDate);
    }
    return gameQuestions;
  }

  @Override
  public GameQuestions updateGameQuestion(GameQuestions gameQuestions) {
    if (StringUtils.hasText(gameQuestions.getExValidDate())) {
      Date validDate = DateUtil.stringToDate(gameQuestions.getExValidDate());
      gameQuestions.setValidDate(validDate);
    }

    if (StringUtils.hasText(gameQuestions.getExSpinDate())) {
      Date spinDate = DateUtil.stringToDate(gameQuestions.getExSpinDate());
      gameQuestions.setSpinDate(spinDate);
    }

    if (StringUtils.hasText(gameQuestions.getExCreatedDate())) {
      Date createdDate = DateUtil.stringToDate(gameQuestions.getExCreatedDate());
      gameQuestions.setCreatedDate(createdDate);
    }
    return gameQuestionsRepository.save(gameQuestions);
  }

  @Override
  public List<GameQuestions> findAllGameQuestions(boolean isActive) {
    return gameQuestionsRepository.findAllGameQuestions(isActive);
  }

  @Override
  public List<GameQuestions> getGameQuestionsByGreaterthanCurrentDate(String gameType) {
    return gameQuestionsRepository.getGameQuestionsByGreaterthanCurrentDate(gameType, true);
  }

  @Override
  public GameQuestions getGameQuestionByQuestionId(Long questionId, String gameType) {
    return gameQuestionsRepository.getGameQuestionByQuestionId(questionId, gameType, true);
  }

  @Override
  public GameQuestions getCompletedGameQuestionByQuestionId(Long questionId, String gameType) {
    return gameQuestionsRepository.getGameQuestionByQuestionId(questionId, gameType, false);
  }

  @Override
  public void saveApiTeams(List<ApiTeams> teams) throws ParseException {
    for (ApiTeams team : teams) {
      if (team.isSquard() || team.isSquad()) {
        DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatterIST.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date validDate = formatterIST.parse(team.getDateTimeGMT());
        DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatterUTC.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        if (validDate.after(new Date())) {
          GameQuestions dbGameQuestions = gameQuestionsRepository.findByUniqueId(team.getUniqueId());
          if(dbGameQuestions == null) {
            GameQuestions gameQuestions = new GameQuestions();
            gameQuestions.setTeamOne(team.getTeamOne().toUpperCase());
            gameQuestions.setTeamTwo(team.getTeamTwo().toUpperCase());
            String question =
                team.getTeamOne().toUpperCase() + " V/S " + team.getTeamTwo().toUpperCase();
            gameQuestions.setQuestion(question);
            gameQuestions.setValidDate(validDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(validDate);
            calendar.add(Calendar.HOUR_OF_DAY, 6);
            Date spinDate = calendar.getTime();
            gameQuestions.setSpinDate(spinDate);
            gameQuestions.setCreatedDate(new Date());
            gameQuestions.setQuestionEffect("rotateIn");
            gameQuestions.setActive(true);
            gameQuestions.setChoices(11);
            gameQuestions.setQuestionType("cricket");
            gameQuestions.setUniqueId(team.getUniqueId());
            gameQuestions.setGameType(team.getType());
            gameQuestionsRepository.save(gameQuestions);
          }
        }
      }
    }
  }


}
