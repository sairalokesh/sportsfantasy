package com.sports.fantasy.userservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sports.fantasy.domain.SelectedMembers;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.UserAmount;
import com.sports.fantasy.model.UserInfo;
import com.sports.fantasy.model.UserTempAmount;
import com.sports.fantasy.model.UserTempParticipants;
import com.sports.fantasy.repository.GameParticipantRepository;
import com.sports.fantasy.repository.UserTempAmountRepository;
import com.sports.fantasy.repository.UserTempParticipantRepository;
import com.sports.fantasy.userservice.UserTempParticipantService;

@Service
@Transactional
public class UserTempParticipantServiceImpl implements UserTempParticipantService {

  @Autowired
  private UserTempParticipantRepository userTempParticipantRepository;
  @Autowired private GameParticipantRepository gameParticipantRepository;
  @Autowired private UserTempAmountRepository userTempAmountRepository;

  @Override
  public UserTempParticipants save(SelectedMembers selectedMembers, UserInfo user) {
    UserTempParticipants userTempParticipants = new UserTempParticipants();
    if (selectedMembers != null && selectedMembers.getQuestionId() != null) {
      userTempParticipants.setQuestionId(selectedMembers.getQuestionId());
    }

    if (selectedMembers != null && selectedMembers.getAmountId() != null) {
      userTempParticipants.setAmountId(selectedMembers.getAmountId());
    }

    if (user != null && user.getId() != null) {
      userTempParticipants.setUserId(user.getId());
    }

    if (selectedMembers != null && StringUtils.hasText(selectedMembers.getGameType())) {
      userTempParticipants.setGameType(selectedMembers.getGameType());
    }

    if (selectedMembers != null && selectedMembers.getSelectedMembers() != null
        && !selectedMembers.getSelectedMembers().isEmpty()) {
      String participantsCommaSeparated = selectedMembers.getSelectedMembers().stream()
          .map(p -> p.toString()).collect(Collectors.joining(","));
      userTempParticipants.setParticipants(participantsCommaSeparated);
    }

    if (selectedMembers != null && selectedMembers.getCapitanId() != null) {
      userTempParticipants.setCapitanId(selectedMembers.getCapitanId());
    }

    if (selectedMembers != null && selectedMembers.getViceCapitanId() != null) {
      userTempParticipants.setViceCapitanId(selectedMembers.getViceCapitanId());
    }

    if (selectedMembers != null && selectedMembers.getSuppoterId() != null) {
      userTempParticipants.setSuppoterId(selectedMembers.getSuppoterId());
    }

    if (selectedMembers != null && selectedMembers.getUserTempId() != null) {
      userTempParticipants.setId(selectedMembers.getUserTempId());
    }

    userTempParticipants.setCreatedDate(new Date());
    return userTempParticipantRepository.save(userTempParticipants);
  }

  @Override
  public Map<String, List<GameParticipants>> getAllTempParticipantsByQuestionId(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user) {
    Map<String, List<GameParticipants>> mapPameParticipants = new TreeMap<>();
    UserTempParticipants userTempParticipants = userTempParticipantRepository.findByTempData(userTempId, questionId, amountId, gameType, user.getId());
    if(userTempParticipants!=null && StringUtils.hasText(userTempParticipants.getParticipants())) {
        List<Long> participantIds = Stream.of(userTempParticipants.getParticipants().split(",")).map(Long::valueOf).collect(Collectors.toList());
        if(participantIds!=null && !participantIds.isEmpty()) {
            List<GameParticipants> gameParticipants = gameParticipantRepository.getAllParticipantsByQuestionIdAndParticipantsIds(userTempParticipants.getQuestionId(), participantIds);
            if(gameParticipants!=null && !gameParticipants.isEmpty()) {
                for(GameParticipants gameParticipant: gameParticipants) {
                    if(userTempParticipants.getCapitanId()!=null && gameParticipant.getId().equals(userTempParticipants.getCapitanId())) {
                        gameParticipant.setCapitanId(userTempParticipants.getCapitanId());
                    }
                    if(userTempParticipants.getViceCapitanId()!=null && gameParticipant.getId().equals(userTempParticipants.getViceCapitanId())) {
                        gameParticipant.setViceCapitanId(userTempParticipants.getViceCapitanId());
                    }
                    if(userTempParticipants.getSuppoterId()!=null && gameParticipant.getId().equals(userTempParticipants.getSuppoterId())) {
                        gameParticipant.setSuppoterId(userTempParticipants.getSuppoterId());
                    }
                    
                    if(mapPameParticipants.containsKey(gameParticipant.getParticipantType())) {
                        List<GameParticipants> list = mapPameParticipants.get(gameParticipant.getParticipantType());
                        list.add(gameParticipant);
                        mapPameParticipants.put(gameParticipant.getParticipantType(), list);
                    } else {
                        List<GameParticipants> list = new ArrayList<>();
                        list.add(gameParticipant);
                        mapPameParticipants.put(gameParticipant.getParticipantType(), list);
                    }
                }
            }
        }
    }
    return mapPameParticipants;
  }

  @Override
  public SelectedMembers findById(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user) {
    UserTempParticipants participants = userTempParticipantRepository.findByTempData(userTempId, questionId, amountId, gameType, user.getId());
    SelectedMembers selectedMembers = new SelectedMembers(questionId, amountId, gameType);
    if(participants!=null) {
        if(participants.getParticipants()!=null && participants.getParticipants()!= "") {
          List<Long> participantIds = Stream.of(participants.getParticipants().split(",")).map(Long::valueOf).collect(Collectors.toList());
          selectedMembers.setSelectedMembers(participantIds);
        }
        if(participants.getCapitanId()!=null) {
            selectedMembers.setCapitanId(participants.getCapitanId());
        }
        
        if(participants.getViceCapitanId()!=null) {
            selectedMembers.setViceCapitanId(participants.getViceCapitanId());
        }
        
        if(participants.getSuppoterId()!=null) {
            selectedMembers.setSuppoterId(participants.getSuppoterId());
        }
        
        if(participants.getId()!=null) {
            selectedMembers.setUserTempId(participants.getId()); // setting user temp participant id
        }
    }
    return selectedMembers;
  }

  @Override
  public UserTempParticipants findCurrentParticipantById(Long userTempId, Long questionId, Long amountId, String gameType, UserInfo user) {
    return userTempParticipantRepository.findByTempData(userTempId, questionId, amountId, gameType, user.getId());
  }

  @Override
  public UserTempParticipants update(SelectedMembers selectedMembers, UserTempParticipants userTempParticipants) {
    userTempParticipants.setCapitanId(selectedMembers.getCapitanId());
    userTempParticipants.setViceCapitanId(selectedMembers.getViceCapitanId());
    userTempParticipants.setSuppoterId(selectedMembers.getSuppoterId());
    return userTempParticipantRepository.save(userTempParticipants);
  }

  @Override
  public void deleteTempUserSelectedParticipantsById(Long userTempId) {
    userTempParticipantRepository.deleteById(userTempId);
  }

  @Override
  public UserTempParticipants editsave(SelectedMembers selectedMembers, UserInfo user) {
    UserTempParticipants userTempParticipants = new UserTempParticipants();
    
    if(user!=null && user.getId()!=null) {
        userTempParticipants.setUserId(user.getId());
    }
    
    if(selectedMembers!=null && selectedMembers.getCapitanId()!=null) {
        userTempParticipants.setCapitanId(selectedMembers.getCapitanId());
    }
    
    if(selectedMembers!=null && selectedMembers.getViceCapitanId()!=null) {
        userTempParticipants.setViceCapitanId(selectedMembers.getViceCapitanId());
    }
    
    if(selectedMembers!=null && selectedMembers.getSuppoterId()!=null) {
        userTempParticipants.setSuppoterId(selectedMembers.getSuppoterId());
    }
    
    if(selectedMembers!=null && selectedMembers.getUserTeamId()!=null) {
        userTempParticipants.setUserTeamId(selectedMembers.getUserTeamId());
    }
    
    if(selectedMembers!=null && selectedMembers.getUserTempId()!=null) {
        userTempParticipants.setId(selectedMembers.getUserTempId());
    }
    
    if(selectedMembers!=null && selectedMembers.getQuestionId()!=null) {
        userTempParticipants.setQuestionId(selectedMembers.getQuestionId());
    }
    
    if(selectedMembers!=null && selectedMembers.getAmountId()!=null) {
        userTempParticipants.setAmountId(selectedMembers.getAmountId());
    }
    
    if(selectedMembers!=null && selectedMembers.getSelectedMembers()!=null && !selectedMembers.getSelectedMembers().isEmpty()) {
        String participantsCommaSeparated = selectedMembers.getSelectedMembers().stream().map(p -> p.toString()).collect(Collectors.joining(","));
        userTempParticipants.setParticipants(participantsCommaSeparated);
    }
    
    if(selectedMembers!=null && StringUtils.hasText(selectedMembers.getGameType())) {
        userTempParticipants.setGameType(selectedMembers.getGameType());
    }
    
    userTempParticipants.setCreatedDate(new Date());
    return userTempParticipantRepository.save(userTempParticipants);
  }

  @Override
  public UserTempAmount saveUserAmount(UserAmount userAmount, String orderId) {
    UserTempAmount userTempAmount = new UserTempAmount();
    userTempAmount.setAmount(userAmount.getAddedAmount());
    userTempAmount.setOrderId(orderId);
    userTempAmount.setUserId(userAmount.getUser().getId());
    return userTempAmountRepository.save(userTempAmount);
  }

  @Override
  public UserTempAmount getUserTempAmount(String orderId) {
    return userTempAmountRepository.getUserTempAmount(orderId);
  }

  @Override
  public void deleteTempUserSelectedAmountById(Long id) {
    userTempAmountRepository.deleteById(id);
  }

  @Override
  public void deleteTempUserSelectedAmount(String orderId) {
    userTempAmountRepository.deleteTempUserSelectedAmount(orderId);
  }

}
