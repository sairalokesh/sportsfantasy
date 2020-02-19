package com.sports.fantasy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.GameParticipantScore;

@Repository
public interface GameParticipantScoreRepository extends CrudRepository<GameParticipantScore, Long> {

  @Query("select gps from GameParticipantScore as gps where gps.gameQuestions.id = ?1 and gps.gameParticipants.id = ?2")
  GameParticipantScore findByQuestionIdAndParticipantId(Long questionId, Long participantId);

  @Query("select gps from GameParticipantScore as gps where gps.gameQuestions.id = ?1 and gps.gameParticipants.id in (?2)")
  List<GameParticipantScore> getSelectedPArticipantsScore(Long questionId, List<Long> participants);
}
