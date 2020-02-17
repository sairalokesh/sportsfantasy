package com.sports.fantasy.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.GameParticipantPoints;

@Repository
public interface GameParticipantPointsRepository extends CrudRepository<GameParticipantPoints, Long> {

	@Query("select p from GameParticipantPoints as p where p.gameQuestions.id = ?1")
	List<GameParticipantPoints> getAllParticipantPointsByQuestionId(Long questionId);

	@Modifying
	@Query("delete from GameParticipantPoints as p where p.gameQuestions.id = ?2 and p.gameParticipants.id not in ?1")
	void deleteParticipantPoints(Collection<Long> participantIds, Long questionId);

	@Query("select p from GameParticipantPoints as p where p.gameQuestions.id = ?2 and p.gameParticipants.id = ?1")
	GameParticipantPoints findParticipantPointsByQuestionIdAndParticipantId(Long participantId, Long questionId);

}
