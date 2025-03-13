package com.mosh.drone.dispatcher.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mosh.drone.dispatcher.mapper.DroneMapper;
import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import com.mosh.drone.dispatcher.model.request.RegisterDroneRequest;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.repository.DroneRepository;
import com.mosh.drone.dispatcher.util.ClientUtil;
import com.mosh.drone.dispatcher.util.RestPage;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
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
    drone1.setSerialNumber("D003");
    drone1.setWeightLimit(300.5);
    drone1.setBatteryCapacity(50);

    drone2 = new Drone();

    drone2.setModel(DroneModel.MIDDLEWEIGHT);
    drone2.setState(DroneState.IDLE);
    drone2.setSerialNumber("D004");
    drone2.setWeightLimit(499.98);
    drone2.setBatteryCapacity(70);

    droneRepository.saveAll(List.of(drone1, drone2));
  }

  @AfterEach
  void cleanUp() {
    droneRepository.deleteBySerialNumber("D003");
    droneRepository.deleteBySerialNumber("D004");
    droneRepository.deleteBySerialNumber("D005");
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

    assertThat(droneResponse.stream()).anyMatch(drone -> drone.getSerialNumber().equals("D003"));
  }

  @Test
  void testGetBatteryCapacityByDroneId_success() {

    var response =
        restTemplate.exchange(
            "/api/v1/drone/" + drone1.getId() + "/battery",
            HttpMethod.GET,
            null,
            GenericMessageResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    assertThat(response.getBody()).isNotNull();
    var genericMessageResponse = Objects.requireNonNull(response.getBody());

    assertThat(genericMessageResponse.getMessage()).isEqualTo("50 %");
  }

  @Test
  void testGetBatteryCapacityByDroneId_Throw404() {

    var response =
        restTemplate.exchange(
            "/api/v1/drone/" + "InvalidId" + "/battery",
            HttpMethod.GET,
            null,
            GenericMessageResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testRegisterDrone_success() {

    RegisterDroneRequest request = new RegisterDroneRequest();
    request.setBatteryCapacity(60);
    request.setModel(DroneModel.LIGHTWEIGHT);
    request.setSerialNumber("D005");
    request.setWeightLimit(400.6);

    var response =
        restTemplate.exchange(
            "/api/v1/drone/register",
            HttpMethod.POST,
            new HttpEntity<>(request),
            DroneResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    assertThat(response.getBody()).isNotNull();
    var droneResponse = response.getBody();

    assertThat(droneResponse.getState()).isEqualTo(DroneState.IDLE);
    assertThat(droneResponse.getModel()).isEqualTo(request.getModel());
    assertThat(droneResponse.getWeightLimit()).isEqualTo(request.getWeightLimit());
    assertThat(droneResponse.getBatteryCapacity()).isEqualTo(request.getBatteryCapacity());
  }

  @Test
  void testRegisterDrone_InvalidWeightLimit() {

    RegisterDroneRequest request = new RegisterDroneRequest();
    request.setBatteryCapacity(60);
    request.setModel(DroneModel.LIGHTWEIGHT);
    request.setSerialNumber("D005");
    request.setWeightLimit(500.6);

    var response =
        restTemplate.exchange(
            "/api/v1/drone/register", HttpMethod.POST, new HttpEntity<>(request), String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("weightLimit cannot exceed 500g.");
  }

  @Test
  void testRegisterDrone_InvalidBatteryCapacity() {

    RegisterDroneRequest request = new RegisterDroneRequest();
    request.setBatteryCapacity(200);
    request.setModel(DroneModel.LIGHTWEIGHT);
    request.setSerialNumber("D005");
    request.setWeightLimit(300);

    var response =
        restTemplate.exchange(
            "/api/v1/drone/register", HttpMethod.POST, new HttpEntity<>(request), String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("batteryCapacity should not be greater than 100 %");
  }
}
