package com.mosh.drone.dispatcher.model.response;

import com.mosh.drone.dispatcher.exception.ExceptionCode;
import com.mosh.drone.dispatcher.util.ClientUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseJson {
  private String description;
  private String responseMessage;
  private boolean success;
  private Object data;
  private Map<String, Object> metadata;
  private List<ApiError> errors;

  public ApiResponseJson(Object data, String description) {
    this.success = true;
    this.description = StringUtils.isBlank(description) ? "Treated successfully" : description;
    this.responseMessage = StringUtils.isBlank(description) ? "Treated successfully" : description;
    this.data = data;
    this.errors = new ArrayList<>();
  }

  public ApiResponseJson(ExceptionCode exceptionCode) {
    this.setDescription(exceptionCode.getMessage());
    this.setResponseMessage(exceptionCode.getMessage());
    this.setSuccess(false);
    this.setErrors(List.of(new ApiError(exceptionCode.getMessage(), exceptionCode.getCode(), "")));
  }

  public ApiResponseJson(ExceptionCode exceptionCode, String fieldName) {
    this.setDescription(exceptionCode.getMessage());
    this.setResponseMessage(exceptionCode.getMessage());
    this.setSuccess(false);
    this.setErrors(
        List.of(new ApiError(exceptionCode.getMessage(), exceptionCode.getCode(), fieldName)));
  }

  public ApiResponseJson(String code, String description) {
    this.description = description;
    this.responseMessage = description;
    this.setSuccess(false);
    this.setErrors(List.of(new ApiError(description, code, "")));
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
