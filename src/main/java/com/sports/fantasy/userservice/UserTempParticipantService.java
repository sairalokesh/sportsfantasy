package com.sports.fantasy.userservice;

import java.util.List;
import java.util.Map;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTempAmount;
import com.sports.fantasy.model.UserTempParticipants;

public interface UserTempParticipantService {

  public UserTempParticipants save(SelectedMembers selectedMembers, UserInfo user);
  public Map<String, List<GameParticipants>> getAllTempParticipantsByQuestionId(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public SelectedMembers findById(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public UserTempParticipants findCurrentParticipantById(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public UserTempParticipants update(SelectedMembers selectedMembers, UserTempParticipants userTempParticipants);
  public void deleteTempUserSelectedParticipantsById(Long userTempId);
  public UserTempParticipants editsave(SelectedMembers selectedMembers, UserInfo user);
  public UserTempAmount saveUserAmount(UserAmount userAmount, String orderId);
  public UserTempAmount getUserTempAmount(String orderId);
  public void deleteTempUserSelectedAmountById(Long id);
  public void deleteTempUserSelectedAmount(String orderId);

}
