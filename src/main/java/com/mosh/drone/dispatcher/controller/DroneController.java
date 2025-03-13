package com.mosh.drone.dispatcher.controller;

import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.service.DroneService;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  private final DroneService droneService;

  @GetMapping("/available")
  public Page<DroneResponse> getAvailableDrones(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") @Max(100) int size) {
    return droneService.getAvailableDrones(page, size);
  }

  @GetMapping("/{id}/battery")
  public GenericMessageResponse getDroneBatteryCapacity(@PathVariable String id) {
    return droneService.getDroneBatteryCapacity(id);
  }
}
