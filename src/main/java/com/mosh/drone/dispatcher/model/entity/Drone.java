package com.mosh.drone.dispatcher.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mosh.drone.dispatcher.model.entity.converter.enumeration.DroneModelConverter;
import com.mosh.drone.dispatcher.model.entity.converter.enumeration.DroneStateConverter;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Table(name = "drone")
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted = false")
@SQLDelete(sql = "update drone set deleted = true where id=?")
public class Drone extends BaseEntity {

  @Column(unique = true, length = 100, nullable = false)
  private String serialNumber;

  @NotNull @Convert(converter = DroneModelConverter.class)
  private DroneModel model;

  private double weightLimit;

  private int batteryCapacity;

  @NotNull @Convert(converter = DroneStateConverter.class)
  private DroneState state;

  @OneToMany private List<Medication> medications;
}
