package com.sports.fantasy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sports.fantasy.model.CuponCodeUsers;

@Repository
public interface UserCuponCodeRepository extends CrudRepository<CuponCodeUsers, Long> {

	@Query("Select a from CuponCodeUsers as a where a.creatorUser.id = ?1")
	List<CuponCodeUsers> getAllUtilizerUsers(Long userId);

	@Query("Select a from CuponCodeUsers as a where a.utilizeUser.id = ?1 and a.codeused = ?2")
	CuponCodeUsers getUserByUtilizerId(Long utilizerId, boolean isFlag);
}
