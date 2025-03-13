package com.mosh.drone.dispatcher.model.request;

import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
public class RegisterDroneRequest {

  @NotBlank private String serialNumber;

  @NotNull private DroneModel model;

  @NotNull @Max(value = 500, message = "cannot exceed 500g.")
  private double weightLimit;

  @NotNull @Max(value = 100, message = "should not be greater than 100 %")
  @Min(value = 25, message = "should not be less than 25 %")
  private int batteryCapacity;
}
