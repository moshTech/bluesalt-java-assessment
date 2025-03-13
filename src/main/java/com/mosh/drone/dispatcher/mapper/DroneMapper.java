package com.mosh.drone.dispatcher.mapper;

import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Component
public class DroneMapper {

  public DroneResponse toDroneResponse(Drone drone) {
    DroneResponse droneResponse = new DroneResponse();
    BeanUtils.copyProperties(drone, droneResponse);

    return droneResponse;
  }
}
