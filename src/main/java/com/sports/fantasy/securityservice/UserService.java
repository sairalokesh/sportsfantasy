package com.sports.fantasy.securityservice;

import java.util.List;

import com.sports.fantasy.model.UserInfo;

public interface UserService {

	UserInfo findByEmail(String email);
	UserInfo findByPhoneNumber(String phoneNumber);
	UserInfo getUserByCuponCode(String cuponCode);
	UserInfo save(UserInfo userInfo);
	UserInfo update(UserInfo dbUser);
	
	/* Admin Side Functionality */
	List<UserInfo> findAllByOrderByCreatedDateDesc();
	
	
	void datetostringformat(UserInfo dbUser);
	void stringtodateformat(UserInfo user);
  UserInfo findById(Long id);
	
	
	
}
