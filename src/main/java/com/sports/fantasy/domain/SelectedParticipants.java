package com.sports.fantasy.domain;

import java.util.ArrayList;
import java.util.List;

public class SelectedParticipants {

  private List<Long> selectedParticipants = new ArrayList<>();
  private Long questionId;
  private Long captainId;
  private Long viceCaptainId;
  private Long suppoterId;

  public List<Long> getSelectedParticipants() {
    return selectedParticipants;
  }

  public void setSelectedParticipants(List<Long> selectedParticipants) {
    this.selectedParticipants = selectedParticipants;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public Long getCaptainId() {
    return captainId;
  }

  public void setCaptainId(Long captainId) {
    this.captainId = captainId;
  }

  public Long getViceCaptainId() {
    return viceCaptainId;
  }

  public void setViceCaptainId(Long viceCaptainId) {
    this.viceCaptainId = viceCaptainId;
  }

  public Long getSuppoterId() {
    return suppoterId;
  }

  public void setSuppoterId(Long suppoterId) {
    this.suppoterId = suppoterId;
  }



}
