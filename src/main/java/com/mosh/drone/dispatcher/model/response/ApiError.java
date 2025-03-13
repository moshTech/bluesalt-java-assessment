package com.mosh.drone.dispatcher.model.response;

import com.mosh.drone.dispatcher.util.ClientUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
  private String message;
  private String code;
  private String fieldName;

  public ApiError(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;
  }

  public ApiError(String message, String code, String fieldName) {
    this.message = message;
    this.code = code;
    this.fieldName = fieldName;
  }

  @Override
  public String toString() {
    try {
      return ClientUtil.OBJECT_MAPPER.writeValueAsString(this);
    } catch (Exception e) {
      ClientUtil.cleanForLog(e.getMessage());
    }

    return "";
  }
}
