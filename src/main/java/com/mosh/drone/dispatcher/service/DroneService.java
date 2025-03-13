package com.mosh.drone.dispatcher.service;

import com.mosh.drone.dispatcher.exception.ExceptionOf;
import com.mosh.drone.dispatcher.mapper.DroneMapper;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
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
        .findByStateAndBatteryCapacityGreaterThanEqual(DroneState.IDLE, 30, pageable)
        .map(droneMapper::toDroneResponse);
  }

  public GenericMessageResponse getDroneBatteryCapacity(String droneId) {

    var genericMessageResponse = new GenericMessageResponse();

    int batteryCapacity =
        droneRepository
            .findById(droneId)
            .orElseThrow(() -> ExceptionOf.Business.NotFound.NOT_FOUND.exception("Drone not found"))
            .getBatteryCapacity();

    genericMessageResponse.setMessage(String.valueOf(batteryCapacity).concat(" %"));

    return genericMessageResponse;
  }
}
