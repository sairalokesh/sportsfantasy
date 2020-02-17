package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.GameQuestions;

@Repository
public interface GameQuestionsRepository extends CrudRepository<GameQuestions, Long> {

	@Query("Select g from GameQuestions as g order by g.id desc")
	List<GameQuestions> findAllGameQuestions();

	@Query("Select g from GameQuestions as g where g.questionType = ?1 and g.active = ?2 and g.validDate >= now() order by g.validDate asc")
	List<GameQuestions> getGameQuestionsByGreaterthanCurrentDate(String gameType, boolean isActive);

	@Query("Select g from GameQuestions as g where g.id = ?1 and g.questionType = ?2 and g.active = ?3")
	GameQuestions getGameQuestionByQuestionId(Long questionId, String gameType, boolean isActive);

	@Query("Select g from GameQuestions as g where g.active = ?1 order by g.id desc")
	List<GameQuestions> findAllGameQuestions(boolean isActive);
}