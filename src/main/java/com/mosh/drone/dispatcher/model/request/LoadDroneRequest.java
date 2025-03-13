package com.mosh.drone.dispatcher.model.request;

import jakarta.validation.constraints.NotBlank;
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
public class LoadDroneRequest {

  @NotBlank private List<String> medicationIds;
}
