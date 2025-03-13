package com.mosh.drone.dispatcher.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
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
@Table(name = "battery_log")
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted = false")
@SQLDelete(sql = "update battery_log set deleted = true where id=?")
@Builder
public class DroneBatteryLog extends BaseEntity {

  private String droneId;

  private int batteryLevel;
}
