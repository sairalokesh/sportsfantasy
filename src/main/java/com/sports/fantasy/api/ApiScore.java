package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiScore {

  private ApiData data;

  public ApiData getData() {
    return data;
  }

  public void setData(ApiData data) {
    this.data = data;
  }
}
