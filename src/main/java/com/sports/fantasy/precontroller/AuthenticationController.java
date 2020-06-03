package com.sports.fantasy.precontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.fantasy.domain.UserTokenInfo;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.security.JwtTokenUtil;
import com.sports.fantasy.security.JwtUser;
import com.sports.fantasy.securityservice.UserService;
import com.sports.fantasy.userservice.UserAmountService;
import com.sports.fantasy.userservice.UserCouponService;

@RestController
@RequestMapping(value = "/api")
public class AuthenticationController {

  @Value("${jwt.header}")
  private String tokenHeader;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private UserService userService;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private UserCouponService userCouponService;
  @Autowired
  private UserAmountService userAmountService;

  @PostMapping({"/signup"})
  public ResponseEntity<UserTokenInfo> usersignup(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
    try {
      UserInfo user = userService.findByEmail(userInfo.getEmail());
      if (user != null && user.getEmail().equalsIgnoreCase(userInfo.getEmail())) {
        return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Email is Already exist."), HttpStatus.FOUND);
      }

      UserInfo usermobile = userService.findByPhoneNumber(userInfo.getPhoneNumber());
      if (usermobile != null && usermobile.getPhoneNumber().equalsIgnoreCase(userInfo.getPhoneNumber())) {
        return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Phone Number is Already exist."), HttpStatus.FOUND);
      }

      UserInfo cuponUser = new UserInfo();
      if (userInfo != null && StringUtils.hasText(userInfo.getCuponCode())) {
        cuponUser = userService.getUserByCuponCode(userInfo.getCuponCode());
        if (cuponUser == null || cuponUser.getId() == null) {
          return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Invalid Cuponcode"), HttpStatus.NOT_FOUND);
        }
      }

      UserInfo dbUserInfo = userService.save(userInfo);
      if (dbUserInfo != null && dbUserInfo.getId() != null) {
        userAmountService.saveUserAmount(dbUserInfo.getId());
      }
      if (cuponUser != null && cuponUser.getId() != null && dbUserInfo != null && dbUserInfo.getId() != null) {
        userCouponService.saveCuponCodeUser(cuponUser.getId(), dbUserInfo.getId());
      }
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("User Registered Successfully!"), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping({"/login"})
  public ResponseEntity<UserTokenInfo> createAuthenticationToken(@RequestBody UserInfo user,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (user != null && StringUtils.hasText(user.getEmail())) {
        UserInfo dbUser = userService.findByEmail(user.getEmail());
        if (dbUser != null && StringUtils.hasText(dbUser.getRole())
            && dbUser.getRole().equalsIgnoreCase("ADMIN")) {
          return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Access Deined"),
              HttpStatus.FORBIDDEN);
        }

        if (user != null && StringUtils.hasText(user.getPassword())) {
          final Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
          final JwtUser userDetails = (JwtUser) authentication.getPrincipal();
          SecurityContextHolder.getContext().setAuthentication(authentication);
          final String token = jwtTokenUtil.generateToken(userDetails);
          response.setHeader("Token", token);
          return ResponseEntity.ok(new UserTokenInfo(userDetails.getUser(), token));
        } else {
          Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
          grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + dbUser.getRole()));
          Authentication authentication =
              new UsernamePasswordAuthenticationToken(user.getEmail(), null, grantedAuthorities);
          request.getSession().setAttribute(
              HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
              SecurityContextHolder.getContext());
          final JwtUser userDetails =
              new JwtUser(dbUser.getId(), dbUser, dbUser.getEmail(), dbUser.getPassword(),
                  mapToGrantedAuthorities(
                      new ArrayList<String>(Arrays.asList("ROLE_" + dbUser.getRole()))),
                  dbUser.isEnabled(), null);
          SecurityContextHolder.getContext().setAuthentication(authentication);
          final String token = jwtTokenUtil.generateToken(userDetails);
          response.setHeader("Token", token);
          return ResponseEntity.ok(new UserTokenInfo(userDetails.getUser(), token));
        }
      } else {
        return new ResponseEntity<UserTokenInfo>(new UserTokenInfo("Email is Manditory"),
            HttpStatus.EXPECTATION_FAILED);
      }
    } catch (Exception e) {
      return new ResponseEntity<UserTokenInfo>(new UserTokenInfo(e.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
    return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/logout")
  public ResponseEntity<UserTokenInfo> restlogout(HttpServletRequest request,
      HttpServletResponse response) {
    String authToken = request.getHeader(tokenHeader);
    if (authToken != null && authToken.length() > 7) {
      authToken = authToken.substring(7);
    }
    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.canTokenBeRefreshed(authToken, userDetails.getLastPasswordResetDate())) {
      String refreshedToken = jwtTokenUtil.refreshToken(authToken);
      response.setHeader("Token", refreshedToken);
      return ResponseEntity.ok(new UserTokenInfo(userDetails.getUser(), refreshedToken));
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping(value = "/refresh")
  public ResponseEntity<UserTokenInfo> refreshAndGetAuthenticationToken(HttpServletRequest request,
      HttpServletResponse response) {
    String authToken = request.getHeader(tokenHeader);
    if (authToken != null && authToken.length() > 7) {
      authToken = authToken.substring(7);
    }
    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.isTokenExpired(authToken)) {
      String refreshedToken = jwtTokenUtil.refreshToken(authToken);
      response.setHeader("Token", refreshedToken);
      return ResponseEntity.ok(new UserTokenInfo(userDetails.getUser(), refreshedToken));
    } else {
      return ResponseEntity.ok(new UserTokenInfo(userDetails.getUser(), authToken));
    }
  }
}
