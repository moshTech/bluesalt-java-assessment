package com.mosh.drone.dispatcher.controller;

import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medication")
public class MedicationController {

  private final MedicationService medicationService;

  @GetMapping("/all")
  @Operation(summary = "Get medications", description = "To fetch all medications")
  public List<Medication> getMedications() {
    return medicationService.getMedications();
  }
}
