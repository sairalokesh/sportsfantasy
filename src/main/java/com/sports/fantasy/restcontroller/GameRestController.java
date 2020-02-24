package com.sports.fantasy.restcontroller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameQuestionsService;

@RestController
@RequestMapping(value = "/user/api/")
public class GameRestController {
	@Autowired private GameQuestionsService gameQuestionsService;
	
	@GetMapping(value="/game/{gameType}/entry")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<GameQuestions>> gameentry(@PathVariable String gameType, Principal principal) {
		List<GameQuestions> gameQuestions = gameQuestionsService.getGameQuestionsByGreaterthanCurrentDate(gameType);
		return new ResponseEntity<List<GameQuestions>>(gameQuestions, HttpStatus.OK);
	}	
}
