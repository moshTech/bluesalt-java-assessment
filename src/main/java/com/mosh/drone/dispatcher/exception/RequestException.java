package com.mosh.drone.dispatcher.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class RequestException extends RuntimeException {

  private String code;
  private String actualMessage;

  public RequestException(ExceptionCode exceptionCode) {
    super(
        exceptionCode.getCode().startsWith("5")
            ? ExceptionOf.System.InternalError.SERVER_ERROR.getMessage()
            : exceptionCode.getMessage());

    this.code = exceptionCode.getCode();
    this.actualMessage = exceptionCode.getMessage();
  }

  public RequestException(ExceptionCode exceptionCode, Throwable cause) {
    super(
        exceptionCode.getCode().startsWith("5")
            ? ExceptionOf.System.InternalError.SERVER_ERROR.getMessage()
            : exceptionCode.getMessage(),
        cause);

    this.code = exceptionCode.getCode();
    this.actualMessage = exceptionCode.getMessage();
  }
}
