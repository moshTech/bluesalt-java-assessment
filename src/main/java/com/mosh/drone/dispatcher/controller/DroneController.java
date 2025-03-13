package com.mosh.drone.dispatcher.controller;

import com.mosh.drone.dispatcher.facade.DroneFacade;
import com.mosh.drone.dispatcher.model.request.LoadDroneRequest;
import com.mosh.drone.dispatcher.model.request.RegisterDroneRequest;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drone")
public class DroneController {

  private final DroneFacade droneFacade;
  private final DroneService droneService;

  @GetMapping("/available")
  @Operation(summary = "Get available drones", description = "To fetch available drones")
  public Page<DroneResponse> getAvailableDrones(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") @Max(100) int size) {
    return droneService.getAvailableDrones(page, size);
  }

  @GetMapping("/{id}/battery")
  @Operation(summary = "Get battery level", description = "To battery level a specified drone")
  public GenericMessageResponse getDroneBatteryCapacity(@PathVariable String id) {
    return droneService.getDroneBatteryCapacity(id);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Register a drone", description = "To register a drone")
  public DroneResponse register(@RequestBody @Valid RegisterDroneRequest request) {
    return droneService.registerDrone(request);
  }

  @PostMapping("/{id}/load")
  @Operation(summary = "Load drone with medication", description = "To load drone with medication")
  public GenericMessageResponse loadDrone(@PathVariable String id, @RequestBody LoadDroneRequest request) {
    return droneFacade.loadDrone(id, request);
  }

  @GetMapping("/loaded/{id}")
  @Operation(summary = "Get loaded drone by id", description = "To fetch loaded drone by id with its medications")
  public DroneResponse getLoadedDroneById(
          @PathVariable String id) {
    return droneService.getLoadedDroneWithMedications(id);
  }
}
