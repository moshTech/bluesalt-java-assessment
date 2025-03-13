package com.mosh.drone.dispatcher.controller;

import com.mosh.drone.dispatcher.model.response.HealthStatusResponse;
import com.mosh.drone.dispatcher.service.ActuatorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actuator")
public class ActuatorController {

  private final ActuatorService actuatorService;

  @GetMapping("/health")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get health status",
      description =
          "Retrieve the health details about the service and utilities such as db, redis etc")
  public HealthStatusResponse getHealth() {
    return actuatorService.getHealth();
  }
}
