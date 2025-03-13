package com.mosh.drone.dispatcher.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mosh.drone.dispatcher.exception.ExceptionCode;
import com.mosh.drone.dispatcher.exception.ExceptionOf;
import com.mosh.drone.dispatcher.exception.RequestException;
import com.mosh.drone.dispatcher.model.response.ApiError;
import com.mosh.drone.dispatcher.model.response.ApiResponseJson;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiAdvice implements ResponseBodyAdvice<Object> {

  private final Environment env;

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponseJson> handleNoResourceFoundException(
      NoResourceFoundException e) {
    log.error("NoResourceFoundException", e);

    ExceptionCode exceptionCode = ExceptionOf.Business.NotFound.NOT_FOUND;

    ApiResponseJson responseJson = new ApiResponseJson(exceptionCode);

    return new ResponseEntity<>(responseJson, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseJson> handleException(Exception e) {

    log.error("Exception", e);

    ExceptionCode exceptionCode = ExceptionOf.System.InternalError.SERVER_ERROR;

    ApiResponseJson responseJson = new ApiResponseJson(exceptionCode);

    return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleInvalidFormatException(InvalidFormatException e) {
    log.error("InvalidFormatException", e);
    ExceptionCode exceptionCode = ExceptionOf.Business.BadRequest.BAD_REQUEST;
    exceptionCode.exception(
        "Invalid Value - "
            + e.getValue()
            + ". Possible Values: "
            + Arrays.toString(e.getTargetType().getEnumConstants()));

    ApiResponseJson responseJson = new ApiResponseJson(exceptionCode, e.getPathReference());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleValidationException(
      MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException", e);
    return new ResponseEntity<>(
        getBindingExceptions(e.getBindingResult().getFieldErrors()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleConstraintViolationException(
      ConstraintViolationException e) {
    log.error("ConstraintViolationException", e);
    List<FieldError> errors =
        e.getConstraintViolations().stream()
            .map(
                constraintViolation -> {
                  return new FieldError(
                      constraintViolation.getRootBeanClass().getName(),
                      constraintViolation.getPropertyPath().toString(),
                      constraintViolation.getMessage());
                })
            .collect(Collectors.toList());
    return new ResponseEntity<>(getBindingExceptions(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponseJson handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("Illegal argument exception", e);

    return new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleBindingException(BindException e) {
    log.error("BindException", e);
    return new ResponseEntity<>(
        getBindingExceptions(e.getBindingResult().getFieldErrors()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    log.error("MethodArgumentTypeMismatchException", e);

    Throwable throwable = (e.getRootCause() != null) ? e.getRootCause() : e;

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, throwable.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleMissingRequestHeaderException(
      MissingRequestHeaderException e) {
    log.error("MissingRequestHeaderException", e);

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ApiResponseJson> handleNullException(NullPointerException e) {
    log.error("NullPointerException", e);

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.System.InternalError.SERVER_ERROR, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ServletException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ApiResponseJson> handleServletException(ServletException e) {
    log.error("ServletException", e);

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.System.InternalError.SERVER_ERROR, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {

    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("HttpMessageNotReadableException", e);

    ApiResponseJson responseJson =
        new ApiResponseJson(
            ExceptionOf.Business.BadRequest.BAD_REQUEST,
            e.getMessage().contains("Required request body is missing:")
                ? "Invalid body request"
                : e.getMessage());
    responseJson.setResponseMessage(e.getMessage());

    if (e.getMessage().contains("Required request body is missing:")) {
      responseJson.setResponseMessage("Invalid body request");
    }
    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponseJson> handleUnsupportedMediaTypeException(
      HttpMediaTypeNotSupportedException e) {
    log.error("HttpMediaTypeNotSupportedException", e);
    ApiResponseJson responseJson =
        new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST, e.getMessage());

    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RequestException.class)
  public ResponseEntity<ApiResponseJson> handleRequestException(RequestException e) {
    log.error("RequestException", e);
    var baseResponse = new ApiResponseJson(e.getCode(), e.getMessage());
    var statusAsInt = Integer.parseInt(e.getCode().substring(0, 3));
    HttpStatus status =
        HttpStatus.valueOf(statusAsInt); // Assuming e.getCode() returns a valid HTTP status code
    return new ResponseEntity<>(baseResponse, status);
  }

  @SneakyThrows
  private ApiResponseJson getBindingExceptions(List<FieldError> errorList) {

    ApiResponseJson responseJson = new ApiResponseJson(ExceptionOf.Business.BadRequest.BAD_REQUEST);
    List<ApiError> errors = new ArrayList<>();

    for (FieldError fieldError : errorList) {
      errors.add(
          new ApiError(
              fieldError.getField(),
              fieldError.isBindingFailure()
                  ? "Invalid data format"
                  : fieldError
                      .getField()
                      .concat(" ")
                      .concat(
                          fieldError.getDefaultMessage() == null
                              ? "is required and must be valid"
                              : fieldError.getDefaultMessage())));
    }

    if (!errors.isEmpty()) {
      log.info("Errors: {}", errors);
      String responseMessage =
          String.format("%s: %s", errors.get(0).getFieldName(), errors.get(0).getMessage());

      responseJson.setResponseMessage(responseMessage);
    }

    responseJson.setErrors(errors);
    return responseJson;
  }

  public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> type) {
    return true;
  }

  @SneakyThrows
  public Object beforeBodyWrite(
      Object body,
      MethodParameter mp,
      MediaType mt,
      Class<? extends HttpMessageConverter<?>> type,
      ServerHttpRequest shr,
      ServerHttpResponse shr1) {

    var activeProfiles = Arrays.asList(env.getActiveProfiles()); // Get active profiles as a list

    // Check for specific profiles and specific paths
    if (isSwaggerEndpoint(shr.getURI().getPath()) || activeProfiles.contains("integration-test")) {

      return body;
    }

    // If the response is already a String, return it as-is
    if (body instanceof String) {
      return body;
    }

    return body instanceof ApiResponseJson || body instanceof Resource
        ? this.cleanXSSObjectFieldsRecursive(body)
        : new ApiResponseJson(body, "");
  }

  protected Object cleanXSSObjectFields(Object body) {
    if (body == null) {
      return null;
    }

    // Skip sanitization for non-application classes
    if (!body.getClass().getPackageName().contains("com.digicore")) {
      return body;
    }

    Field[] fields = body.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(body);
        if (value instanceof String && !((String) value).isEmpty()) {
          // Sanitize String value
          String cleanValue = cleanXSS((String) value);
          field.set(body, cleanValue);
        }
      } catch (IllegalAccessException e) {
        // Log the exception for debugging
        log.error("Failed to access field: " + field.getName(), e);
      }
    }

    return body;
  }

  private String cleanXSS(String value) {
    // Remove dangerous script-related patterns
    value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", ""); // Matches <script> tags
    value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", ""); // Matches javascript:
    value = value.replaceAll("(?i)<.*?on.*?=.*?>.*?</.*?>", ""); // Matches on* events like onClick

    // Remove potentially dangerous eval and expressions
    value = value.replaceAll("eval\\((.*)\\)", "");
    value = value.replaceAll("[\\\"\\'][\\s]*javascript:(.*?)[\\\"\\']", "\"\"");

    // Return cleaned value
    return value;
  }

  private Object cleanXSSObjectFieldsRecursive(Object body) {
    if (body == null) {
      return null;
    }

    if (!body.getClass().getPackageName().contains("com.mosh")) {
      return body;
    }

    Field[] fields = body.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(body);
        if (value instanceof String && !((String) value).isEmpty()) {
          String cleanValue = cleanXSS((String) value);
          field.set(body, cleanValue);
        } else if (value != null && value.getClass().getPackageName().contains("com.mosh")) {
          cleanXSSObjectFieldsRecursive(value); // Recursive sanitization
        }
      } catch (IllegalAccessException e) {
        log.error("Failed to access field: " + field.getName(), e);
      }
    }

    return body;
  }

  private boolean isSwaggerEndpoint(String uriPath) {
    String SWAGGER_UI_PATH = "/swagger-ui";
    String API_DOCS_PATH = "/v3/api-docs";
    return uriPath.contains(SWAGGER_UI_PATH) || uriPath.contains(API_DOCS_PATH);
  }
}
