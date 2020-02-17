package com.sports.fantasy.userservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sports.fantasy.model.CuponCodeUsers;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.repository.UserCuponCodeRepository;
import com.sports.fantasy.userservice.UserCouponService;

@Service
@Transactional
public class UserCouponServiceImpl implements UserCouponService {

  @Autowired
  private UserCuponCodeRepository userCuponCodeRepository;

  @Override
  public void saveCuponCodeUser(Long creatorUserId, Long utilizerUserId) {
    CuponCodeUsers cuponCodeUsers = new CuponCodeUsers();
    cuponCodeUsers.setCodeused(false);
    cuponCodeUsers.setCreatorUser(new UserInfo(creatorUserId));
    cuponCodeUsers.setUtilizeUser(new UserInfo(utilizerUserId));
    userCuponCodeRepository.save(cuponCodeUsers);
  }

  @Override
  public List<CuponCodeUsers> getAllUtilizerUsers(Long userId) {
    return userCuponCodeRepository.getAllUtilizerUsers(userId);
  }

  @Override
  public CuponCodeUsers getUserByUtilizerId(Long utilizerId) {
    return userCuponCodeRepository.getUserByUtilizerId(utilizerId, false);
  }

  @Override
  public void updateCuponCode(CuponCodeUsers codeUsers) {
    userCuponCodeRepository.save(codeUsers);

  }

}
