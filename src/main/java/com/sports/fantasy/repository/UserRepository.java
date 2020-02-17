package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {

	UserInfo findByEmail(String email);
	UserInfo findByPhoneNumber(String phoneNumber);
	UserInfo findByEmailAndEnabled(String email, boolean enabled);
	List<UserInfo> findAllByOrderByCreatedDateDesc();

	@Query("select us from UserInfo as us where us.cuponCode = ?1")
	UserInfo getUserByCuponCode(String cuponCode);

}
