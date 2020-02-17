package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminEntriesController {

	@Autowired
	private GameAmountService gameAmountService;
	
	@ModelAttribute
	public void adminmounttitle(Model model) {
		model.addAttribute("title", "amountentries");
	}

	@GetMapping(value = "/amountentries")
	public String amountentries(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<AmountEntries> amountEntries = gameAmountService.findAllAmountEntries();
		if (amountEntries != null && amountEntries.size() > 0) {
			amountComparator(amountEntries);
		}

		model.addAttribute("amountEntry", new AmountEntries());
		model.addAttribute("amountEntries", amountEntries);
		model.addAttribute("isAdd", true);
		model.addAttribute("isdisplay", false);
		return "view/admin/adminamountentries";
	}

	@GetMapping(value = "/addamountentry")
	public String addamountentry(Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<AmountEntries> amountEntries = gameAmountService.findAllAmountEntries();
		if (amountEntries != null && amountEntries.size() > 0) {
			amountComparator(amountEntries);
		}
		model.addAttribute("amountEntry", new AmountEntries());
		model.addAttribute("amountEntries", amountEntries);
		model.addAttribute("isAdd", true);
		model.addAttribute("isdisplay", true);
		return "view/admin/adminamountentries";
	}

	@PostMapping(value = "/saveamountentry")
	public String saveamountentry(@ModelAttribute AmountEntries amountEntry, Principal principal, Model model,
			RedirectAttributes redirectAttributes) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		AmountEntries dbAmountEntry = gameAmountService.saveAmountEntry(amountEntry);
		if (dbAmountEntry != null) {
			redirectAttributes.addFlashAttribute("successmessage", "Amount Entry Saved Successfully!");
			return "redirect:/admin/amountentries";
		} else {
			model.addAttribute("errormessage", "Amount Entry not save! Please try again!");
			return "view/admin/adminamountentries";
		}
	}

	@GetMapping(value = "/editamountentry/{id}")
	public String editamountentry(@PathVariable Long id, Principal principal, Model model) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		List<AmountEntries> amountEntries = gameAmountService.findAllAmountEntries();
		if (amountEntries != null && amountEntries.size() > 0) {
			amountComparator(amountEntries);
		}
		AmountEntries amountEntry = gameAmountService.findByAmountId(id);
		model.addAttribute("amountEntry", amountEntry);
		model.addAttribute("amountEntries", amountEntries);
		model.addAttribute("isAdd", false);
		model.addAttribute("isdisplay", true);
		return "view/admin/adminamountentries";
	}

	@PostMapping(value = "/updateamountentry")
	public String updateamountentry(@ModelAttribute AmountEntries amountEntry, Principal principal, Model model,
			RedirectAttributes redirectAttributes) {
		if (!LoginUtil.getAuthentication(principal)) {
			return "redirect:/signin";
		}
		AmountEntries dbAmountEntry = gameAmountService.saveAmountEntry(amountEntry);
		if (dbAmountEntry != null) {
			redirectAttributes.addFlashAttribute("successmessage", "Amount Entry Updated Successfully!");
			return "redirect:/admin/amountentries";
		} else {
			model.addAttribute("errormessage", "Amount Entry not update! Please try again!");
			return "view/admin/adminamountentries";
		}
	}
	
	private void amountComparator(List<AmountEntries> amountEntries) {
		Collections.sort(amountEntries, new Comparator<AmountEntries>() {
			@Override
			public int compare(AmountEntries o1, AmountEntries o2) {
				if (StringUtils.hasText(o1.getAmount()) && StringUtils.hasText(o2.getAmount())) {
					Float amount1 = Float.parseFloat(o1.getAmount());
					Float amount2 = Float.parseFloat(o2.getAmount());
					return amount1.compareTo(amount2);
				}
				return 0;
			}
		});
	}
}
