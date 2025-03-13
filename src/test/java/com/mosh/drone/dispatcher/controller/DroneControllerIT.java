package com.mosh.drone.dispatcher.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mosh.drone.dispatcher.mapper.DroneMapper;
import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.repository.DroneRepository;
import com.mosh.drone.dispatcher.util.ClientUtil;
import com.mosh.drone.dispatcher.util.RestPage;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class DroneControllerIT {

  @Autowired private DroneRepository droneRepository;

  @Autowired private DroneMapper droneMapper;

  private Drone drone1, drone2;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    drone1 = new Drone();
    drone1.setModel(DroneModel.LIGHTWEIGHT);
    drone1.setState(DroneState.IDLE);
    drone1.setSerialNumber("D001");
    drone1.setWeightLimit(300.5);
    drone1.setBatteryCapacity(50);

    drone2 = new Drone();

    drone2.setModel(DroneModel.MIDDLEWEIGHT);
    drone2.setState(DroneState.IDLE);
    drone2.setSerialNumber("D002");
    drone2.setWeightLimit(499.98);
    drone2.setBatteryCapacity(70);

    droneRepository.saveAll(List.of(drone1, drone2));
  }

  @SneakyThrows
  @Test
  void getAvailableDrones() {

    var res =
        restTemplate.exchange(
            "/api/v1/drone/available?page=0&size=100",
            HttpMethod.GET,
            new HttpEntity<>(HttpEntity.EMPTY),
            String.class);

    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    var droneResponse =
        ClientUtil.OBJECT_MAPPER.readValue(
            res.getBody(), new TypeReference<RestPage<DroneResponse>>() {});

    assertThat(droneResponse.getSize()).isPositive();

    assertThat(droneResponse.stream()).anyMatch(drone -> drone.getSerialNumber().equals("D001"));
  }
}
