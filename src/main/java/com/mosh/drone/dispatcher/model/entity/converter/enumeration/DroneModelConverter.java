package com.mosh.drone.dispatcher.model.entity.converter.enumeration;

import com.mosh.drone.dispatcher.model.entity.converter.StringEnumConverter;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import jakarta.persistence.Converter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Converter
public class DroneModelConverter extends StringEnumConverter<DroneModel> {
  public DroneModelConverter() {
    super(DroneModel.class);
  }
}
