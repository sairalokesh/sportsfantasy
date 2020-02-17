package com.sports.fantasy.domain;

import java.io.Serializable;

public class Response implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Integer status;
  private String message;
  private Long userId;

  public Response() {
    super();
  }

  public Response(Integer status, String message) {
    super();
    this.status = status;
    this.message = message;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
