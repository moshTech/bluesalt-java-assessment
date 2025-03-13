package com.mosh.drone.dispatcher.model.response;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
public class BaseResponse {

  private String id;

  private OffsetDateTime createdAt;

  private OffsetDateTime updatedAt;

  private Boolean deleted;
}
