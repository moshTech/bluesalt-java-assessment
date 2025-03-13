package com.mosh.drone.dispatcher.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.mosh.drone.dispatcher.model.response.HealthStatusResponse;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
class ActuatorControllerIT {
  @Autowired private TestRestTemplate restTemplate;

  @Test
  void getHealthWorks() {

    var response =
        restTemplate.exchange(
            "/api/v1/actuator/health", HttpMethod.GET, null, HealthStatusResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    assertThat(response.getBody()).isNotNull();
    var healthResponse = Objects.requireNonNull(response.getBody());

    assertThat(healthResponse.getStatus()).isEqualTo("OK");
    assertThat(healthResponse.getDatetime()).isNotNull();
    assertThat(healthResponse.getProfile()).isEqualTo("integration-test");
    assertThat(healthResponse.getDb()).isEqualTo("UP");
    //    assertThat(healthResponse.getRedis()).isEqualTo("UP");
  }
}
