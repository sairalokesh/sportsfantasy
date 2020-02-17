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

import com.sports.fantasy.domain.QuestionParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameParticipantService;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminQuestionParticipantsController {
	
	@Autowired private GameQuestionsService gameQuestionsService;
	@Autowired private GameParticipantService gameParticipantService;
	
	@ModelAttribute
	public void adminparticipantstitle(Model model) {
		model.addAttribute("title", "adminparticipants");
	}
	
	@GetMapping(value="/questionparticipants")
	public String points(Principal principal, Model model) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		model.addAttribute("gameQuestions", gameQuestions);
		return "view/admin/questionparticipants";
	}
	
	@GetMapping(value="/questionparticipants/{questionId}")
	public String questionparticipants(@PathVariable Long questionId, Principal principal, Model model) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		QuestionParticipants questionParticipants = gameParticipantService.getAllParticipantsByQuestionId(questionId);
		model.addAttribute("questionParticipants", questionParticipants);
		model.addAttribute("gameQuestions", gameQuestions);
		return "view/admin/questionparticipantslist";
	}
	
	
	@PostMapping(value="/updateQuestionParticpants")
	public String updateQuestionParticpants(@ModelAttribute QuestionParticipants questionParticipants, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		gameParticipantService.updateQuestionParticipants(questionParticipants);
		redirectAttributes.addFlashAttribute("successmessage", "Question Participant Entries Updated Successfully!");
		return "redirect:/admin/questionparticipants/"+questionParticipants.getQuestionId();
	}	
}
