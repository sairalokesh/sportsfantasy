package com.sports.fantasy.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.GameParticipants;

@Repository
public interface GameParticipantRepository extends CrudRepository<GameParticipants, Long> {

  @Query("select p from GameParticipants as p where p.gameQuestions.id = ?1")
  List<GameParticipants> getAllParticipantsByQuestionId(Long questionId);

  @Modifying
  @Query("delete from GameParticipants as p where p.gameQuestions.id = ?2 and p.id not in ?1")
  void deleteQuestionParticipants(Collection<Long> participantIds, Long questionId);

  @Query("select p from GameParticipants as p where p.gameQuestions.id = ?1 and p.id in (?2)")
  List<GameParticipants> getAllParticipantsByQuestionIdAndParticipantsIds(Long questionId, List<Long> participantIds);

  @Query("select distinct p.participantGameType from GameParticipants as p where p.gameQuestions.id = ?1")
  List<String> getAllParticipantTypesByQuestionId(Long questionId);

  @Query("select count(p) from GameParticipants as p where p.gameQuestions.id = ?1")
  Long getParticipantsCountByQuestionId(Long questionId);

}
