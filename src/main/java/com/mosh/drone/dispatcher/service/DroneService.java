package com.mosh.drone.dispatcher.service;

import com.mosh.drone.dispatcher.exception.ExceptionOf;
import com.mosh.drone.dispatcher.mapper.DroneMapper;
import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import com.mosh.drone.dispatcher.model.request.RegisterDroneRequest;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DroneService {

  private final DroneRepository droneRepository;

  private final DroneMapper droneMapper;

  public Page<DroneResponse> getAvailableDrones(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

    return droneRepository
        .findByStateAndBatteryCapacityGreaterThanEqual(DroneState.IDLE, 25, pageable)
        .map(droneMapper::toDroneResponse);
  }

  public DroneResponse getLoadedDroneWithMedications(String id) {

    Drone drone = droneRepository.findByIdAndStateIn(id, List.of(DroneState.LOADING, DroneState.LOADED))
            .orElseThrow(() -> ExceptionOf.Business.NotFound.NOT_FOUND.exception("Drone not found in loading/loaded state"));

    return droneMapper.toDroneResponseWithMedication(drone);
  }

  public GenericMessageResponse getDroneBatteryCapacity(String droneId) {

    int batteryCapacity =
        droneRepository
            .findById(droneId)
            .orElseThrow(() -> ExceptionOf.Business.NotFound.NOT_FOUND.exception("Drone not found"))
            .getBatteryCapacity();

    return GenericMessageResponse.builder()
                    .message(String.valueOf(batteryCapacity).concat(" %"))
    .build();
  }

  public DroneResponse registerDrone(RegisterDroneRequest request) {

    if (droneRepository.existsBySerialNumber(request.getSerialNumber())) {
      throw ExceptionOf.Business.BadRequest.BAD_REQUEST.exception(
          "Drone already exist with the provided serial number");
    }

    return droneMapper.toDroneResponse(droneRepository.save(droneMapper.toDrone(request)));
  }


  public void loadDrone(String droneId, List<Medication> medications) {
    Drone drone = droneRepository.findById(droneId)
            .orElseThrow(() -> ExceptionOf.Business.NotFound.NOT_FOUND.exception("Drone not found"));

    double totalWeight = medications.stream().mapToDouble(Medication::getWeight).sum();

    if (totalWeight > drone.getWeightLimit()) {
      throw  ExceptionOf.Business.BadRequest.BAD_REQUEST.exception("Total weight exceeds drone's limit.");
    }

    if (drone.getBatteryCapacity() < 25) {
      throw new IllegalStateException("Drone battery too low for loading.");
    }

    drone.setState(DroneState.LOADING);
    drone.setMedications(medications);
    droneRepository.save(drone);
  }
}
