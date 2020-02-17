package com.sports.fantasy.userservice;

import java.util.List;
import java.util.Map;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTempParticipants;

public interface UserTempParticipantService {

  public UserTempParticipants save(SelectedMembers selectedMembers, UserInfo user);
  public Map<String, List<GameParticipants>> getAllTempParticipantsByQuestionId(Long tempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public SelectedMembers findById(Long tempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public UserTempParticipants findCurrentParticipantById(Long tempId, Long questionId, Long amountId, String gameType, UserInfo user);
  public UserTempParticipants update(SelectedMembers selectedMembers, UserTempParticipants userTempParticipants);
  public void deleteTempUserSelectedParticipantsById(Long userTempId);
  public UserTempParticipants editsave(SelectedMembers selectedMembers, UserInfo user);

}
