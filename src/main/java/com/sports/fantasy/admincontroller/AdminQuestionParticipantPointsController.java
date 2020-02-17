package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.domain.QuestionParticipantPoints;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameParticpantPointsService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminQuestionParticipantPointsController {

	@Autowired private GameQuestionsService gameQuestionsService;
	@Autowired private GameParticipantService gameParticipantService;
	@Autowired private GameParticpantPointsService gameParticpantPointsService;
	
	@ModelAttribute
	public void adminparticipantstitle(Model model) {
		model.addAttribute("title", "adminparticipantpoints");
	}

	@GetMapping(value = "/questionparticipantpoints")
	public String questionparticipantpoints(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		model.addAttribute("gameQuestions", gameQuestions);
		return "view/admin/questionparticipantpoints";
	}

	@GetMapping(value = "/questionparticipantpoints/{questionId}")
	public String questionparticipantpoints(@PathVariable Long questionId, Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}

		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		QuestionParticipantPoints questionParticipantPoints = gameParticpantPointsService.getAllParticipantpointsByQuestionId(questionId);
		List<GameParticipants> gameParticipants = gameParticipantService.getQuestionParticipantsByQuestionId(questionId);
		model.addAttribute("questionParticipantPoints", questionParticipantPoints);
		model.addAttribute("gameQuestions", gameQuestions);
		model.addAttribute("gameParticipants", gameParticipants);
		return "view/admin/questionparticipantpointslist";
	}

	@PostMapping(value = "/updateQuestionParticpantpoints")
	public String updateQuestionParticpantpoints(@ModelAttribute QuestionParticipantPoints questionParticipantPoints,
			Principal principal, Model model, RedirectAttributes redirectAttributes) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		gameParticpantPointsService.updateQuestionParticipantPoints(questionParticipantPoints);
		redirectAttributes.addFlashAttribute("successmessage",
				"Question Participant Points Entries Updated Successfully!");
		return "redirect:/admin/questionparticipantpoints/" + questionParticipantPoints.getQuestionId();
	}
}
