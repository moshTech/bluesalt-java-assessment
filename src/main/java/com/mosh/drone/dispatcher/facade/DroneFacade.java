package com.mosh.drone.dispatcher.facade;

import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.model.request.LoadDroneRequest;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.service.DroneService;
import com.mosh.drone.dispatcher.service.MedicationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DroneFacade {

  private final DroneService droneService;

  private final MedicationService medicationService;

  public GenericMessageResponse loadDrone(String droneId, LoadDroneRequest request) {
    List<Medication> validMedications = medicationService.getByIds(request);

    droneService.loadDrone(droneId, validMedications);

    return GenericMessageResponse.builder().message("Drone loaded successfully").build();
  }
}
