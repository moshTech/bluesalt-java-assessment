package com.mosh.drone.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.mosh.drone.dispatcher"})
@EnableJpaRepositories(basePackages = {"com.mosh.drone.dispatcher"})
@EntityScan(basePackages = {"com.mosh.drone.dispatcher"})
@EnableCaching
@EnableAsync
public class DroneDispatcherApplication {

  public static void main(String[] args) {
    SpringApplication.run(DroneDispatcherApplication.class, args);
  }
}
