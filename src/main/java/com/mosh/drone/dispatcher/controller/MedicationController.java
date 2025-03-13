package com.mosh.drone.dispatcher.controller;

import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
