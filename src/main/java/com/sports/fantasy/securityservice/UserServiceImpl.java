package com.sports.fantasy.securityservice;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.repository.UserRepository;
import com.sports.fantasy.util.AlphaNumericUtil;
import com.sports.fantasy.util.DateUtil;
import com.sports.fantasy.util.PasswordUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserInfo findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserInfo findByPhoneNumber(String phoneNumber) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}

	@Override
	public UserInfo getUserByCuponCode(String cuponCode) {
		return userRepository.getUserByCuponCode(cuponCode);
	}

	@Override
	public UserInfo save(UserInfo userInfo) {
		userInfo.setEnabled(true);
		userInfo.setRole("USER");
		userInfo.setCreatedDate(new Date());
		if (StringUtils.hasText(userInfo.getPassword())) {
			userInfo.setPassword(PasswordUtil.getEncodedPassword(userInfo.getPassword()));
		}
		if (StringUtils.hasText(userInfo.getFirstName())) {
			String cutName = userInfo.getFirstName().substring(0, 3);
			String randName = AlphaNumericUtil.getAlphaNumericValue(6);
			userInfo.setCuponCode((cutName + randName).toUpperCase());
		}
		return userRepository.save(userInfo);
	}

	@Override
	public UserInfo update(UserInfo dbUser) {
		return userRepository.save(dbUser);
	}

	@Override
	public List<UserInfo> findAllByOrderByCreatedDateDesc() {
		return userRepository.findAllByOrderByCreatedDateDesc();
	}

	@Override
	public void datetostringformat(UserInfo dbUser) {
		if (dbUser.getCreatedDate() != null) {
			String screatedDate = DateUtil.dateToString(dbUser.getCreatedDate());
			dbUser.setScreateddate(screatedDate);
		}

		if (dbUser.getLoginDate() != null) {
			String sloginDate = DateUtil.dateToString(dbUser.getLoginDate());
			dbUser.setSlogindate(sloginDate);
		}
	}

	@Override
	public void stringtodateformat(UserInfo user) {
		if (StringUtils.hasText(user.getScreateddate())) {
			Date createdDate = DateUtil.stringToDate(user.getScreateddate());
			user.setCreatedDate(createdDate);
		}

		if (StringUtils.hasText(user.getSlogindate())) {
			Date loginDate = DateUtil.stringToDate(user.getSlogindate());
			user.setLoginDate(loginDate);
		}
	}

  @Override
  public UserInfo findById(Long id) {
    Optional<UserInfo> userInfo = userRepository.findById(id);
    if (userInfo.isPresent()) {
      return userInfo.get();
    }
    return null;
  }
}
