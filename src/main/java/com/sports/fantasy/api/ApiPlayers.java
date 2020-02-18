package com.sports.fantasy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiPlayers {
  private String fullname;
  @SerializedName(value = "image_path")
  private String imagePath;
  private ApiPositions position;

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public ApiPositions getPosition() {
    return position;
  }

  public void setPosition(ApiPositions position) {
    this.position = position;
  }
}
