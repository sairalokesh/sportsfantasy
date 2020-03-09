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

import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.service.GameQuestionsService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminGameQuestionsController {
	
	@Autowired
	private GameQuestionsService gameQuestionsService;
	
	@ModelAttribute
	public void admindquestiontitle(Model model) {
		model.addAttribute("title", "adminquestion");
	}

	@GetMapping(value="/questions")
	public String questions(Principal principal, Model model) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		model.addAttribute("gameQuestion", new GameQuestions());
		model.addAttribute("gameQuestions", gameQuestions);
		model.addAttribute("isAdd", true);
		model.addAttribute("isdisplay", false);
		return "view/admin/gamequestions";
	}
	
	@GetMapping(value="/addgamequestion")
	public String addgamequestion(Principal principal, Model model) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		model.addAttribute("gameQuestion", new GameQuestions());
		model.addAttribute("gameQuestions", gameQuestions);
		model.addAttribute("isAdd", true);
		model.addAttribute("isdisplay", true);
		return "view/admin/gamequestions";
	}
	
	@PostMapping(value="/savegamequestion")
	public String savegamequestion(@ModelAttribute GameQuestions gameQuestions, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		GameQuestions dbGameQuestions = gameQuestionsService.saveGameQuestion(gameQuestions);
		if(dbGameQuestions!=null) {
			redirectAttributes.addFlashAttribute("successmessage", "Game Question Saved Successfully!");
			return "redirect:/admin/questions";
		} else {
			model.addAttribute("errormessage", "Game Question not save! Please try again!");
			return "view/admin/gamequestions";
		}
	}
	
	@GetMapping(value="/editgamequestion/{id}")
	public String editgamequestion(@PathVariable Long id,Principal principal, Model model) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<GameQuestions> gameQuestions = gameQuestionsService.findAllGameQuestions(true);
		GameQuestions gameQuestion = gameQuestionsService.findGameQuestionById(id);
		model.addAttribute("gameQuestions", gameQuestions);
		model.addAttribute("gameQuestion", gameQuestion);
		model.addAttribute("isAdd", false);
		model.addAttribute("isdisplay", true);
		return "view/admin/gamequestions";
	}	
	
	@PostMapping(value="/updategamequestion")
	public String updategamequestion(@ModelAttribute GameQuestions gameQuestions, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		if(!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		
		GameQuestions dbGameQuestions = gameQuestionsService.updateGameQuestion(gameQuestions);
		if(dbGameQuestions!=null) {
			redirectAttributes.addFlashAttribute("successmessage", "Game Question Updated Successfully!");
			return "redirect:/admin/questions";
		} else {
			model.addAttribute("errormessage", "Game Question not update! Please try again!");
			return "view/admin/gamequestions";
		}
	}

}
