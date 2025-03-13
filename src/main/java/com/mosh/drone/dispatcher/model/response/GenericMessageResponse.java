package com.mosh.drone.dispatcher.model.response;

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
public class GenericMessageResponse {

  private String message;
}
