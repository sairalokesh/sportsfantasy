package com.sports.fantasy.restcontroller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.securityservice.UserService;

@RestController
@RequestMapping(value = "/user/api/")
public class ProfileRestController {
  
  @Autowired
  private UserService userService;
  
  @GetMapping(value = "profile")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserInfo> profile(Principal principal, Model model) {
    UserInfo user = userService.findByEmail(principal.getName());
    return new ResponseEntity<UserInfo>(user, HttpStatus.OK);
  }

}
