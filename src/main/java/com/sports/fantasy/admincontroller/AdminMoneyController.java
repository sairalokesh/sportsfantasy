package com.sports.fantasy.admincontroller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sports.fantasy.adminservice.MoneyService;
import com.sports.fantasy.domain.GameMoneyData;
import com.sports.fantasy.domain.Money;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameMoneyRange;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.service.GameAmountService;
import com.sports.fantasy.util.AmountComparatorUtil;
import com.sports.fantasy.util.LoginUtil;

@Controller
@RequestMapping(value = "/admin")
public class AdminMoneyController {

  @Autowired
  private UserService userService;

  @Autowired
  private GameAmountService gameAmountService;

  @Autowired
  private MoneyService moneyService;

  @ModelAttribute
  public void admindquestiontitle(Model model) {
    model.addAttribute("title", "adminmoney");
  }

  @GetMapping(value = "/gamemoney")
  public String points(Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser != null && !dbUser.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }

    setMoney(model);
    return "view/admin/adminmoney";
  }

  @GetMapping(value = "/getmoney")
  public String savemoney(@RequestParam String amount, @RequestParam Integer persons,
      Principal principal, Model model) {
    if (!LoginUtil.getAuthentication(principal)) {
      return "redirect:/signin";
    }

    UserInfo dbUser = userService.findByEmail(principal.getName());
    if (dbUser != null && !dbUser.getRole().equalsIgnoreCase("ADMIN")) {
      return "redirect:/accessdenied";
    }

    Double parseAmount = Double.parseDouble(amount);
    List<GameMoneyRange> gameMoneyRanges = moneyService.getGameMoneyRange(parseAmount, persons);
    GameMoneyData gameMoneyData = new GameMoneyData();
    gameMoneyData.setAmount(parseAmount);
    gameMoneyData.setGameMoneyRanges(gameMoneyRanges);
    gameMoneyData.setPersons(persons);
    setMoney(model);
    
    Money money = new Money();
    money.setAmount(amount);
    money.setPersons(persons);
    
    model.addAttribute("money", money);
    model.addAttribute("gameMoneyData", gameMoneyData);
    return "view/admin/adminmoneylist";
  }
  
  
  @PostMapping(value="/updateGameMoney")
  public String updateGameMoney(@ModelAttribute GameMoneyData gameMoneyData, Principal principal, Model model, RedirectAttributes redirectAttributes) {
      if(!LoginUtil.getAuthentication(principal)) {
          return "redirect:/signin";
      }
      moneyService.updateQuestionParticipants(gameMoneyData);
      return "redirect:/admin/getmoney?amount="+(gameMoneyData.getAmount()+"0") + "&persons="+ gameMoneyData.getPersons();
  }   

  private void setMoney(Model model) {
    List<AmountEntries> amountEntries = gameAmountService.findAllAmountEntries();
    if (amountEntries != null && amountEntries.size() > 0) {
      AmountComparatorUtil.amountComparator(amountEntries);
    }

    List<Integer> persons = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      persons.add(i);
    }
    model.addAttribute("amountEntries", amountEntries);
    model.addAttribute("persons", persons);
  }
}
