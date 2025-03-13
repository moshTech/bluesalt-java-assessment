package com.mosh.drone.dispatcher.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mosh.drone.dispatcher.model.entity.Medication;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DroneResponse extends BaseResponse {

  private String serialNumber;

  private DroneModel model;

  private double weightLimit;

  private int batteryCapacity;

  private DroneState state;

  private List<Medication> medications;
}
