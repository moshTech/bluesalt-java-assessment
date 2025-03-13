package com.mosh.drone.dispatcher.model.response;

import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
public class DroneResponse extends BaseResponse {

  private String serialNumber;

  private DroneModel model;

  private double weightLimit;

  private int batteryCapacity;

  private DroneState state;
}
