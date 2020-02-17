package com.sports.fantasy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.UserTempParticipants;

@Repository
public interface UserTempParticipantRepository extends CrudRepository<UserTempParticipants, Long> {
  
  @Query("select utp from UserTempParticipants as utp where utp.id = ?1 and utp.questionId = ?2 and utp.amountId = ?3 and utp.gameType = ?4 and utp.userId = ?5")
  UserTempParticipants findByTempData(Long tempId, Long questionId, Long amountId, String gameType, Long userId);


}
