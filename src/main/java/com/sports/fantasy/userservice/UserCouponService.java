package com.sports.fantasy.userservice;

import java.util.List;

import com.sports.fantasy.model.CuponCodeUsers;

public interface UserCouponService {

  void saveCuponCodeUser(Long creatorUserId, Long utilizerUserId);
  List<CuponCodeUsers> getAllUtilizerUsers(Long userId);
  CuponCodeUsers getUserByUtilizerId(Long utilizerId);
  void updateCuponCode(CuponCodeUsers codeUsers);

}
