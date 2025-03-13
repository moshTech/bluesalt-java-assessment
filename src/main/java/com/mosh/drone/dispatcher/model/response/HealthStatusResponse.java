package com.mosh.drone.dispatcher.model.response;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
@Builder
public class HealthStatusResponse {

  private Date datetime;
  private Date startDate;
  private String upDuration;
  private String timezone;
  private String status;
  private String db;
  private String profile;
  private String redis;
}
