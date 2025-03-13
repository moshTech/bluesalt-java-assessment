package com.mosh.drone.dispatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class DroneDispatcherApplicationTests {

  public static void main(String[] args) {
    SpringApplication.from(DroneDispatcherApplication::main)
        .with(DroneDispatcherApplicationTests.class)
        .run(args);
  }

  @Test
  void testSomething() {
    String test = "test";
    assertEquals("test", test);
  }
}
