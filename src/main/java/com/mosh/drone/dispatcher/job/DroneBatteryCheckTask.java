package com.mosh.drone.dispatcher.job;

import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.entity.DroneBatteryLog;
import com.mosh.drone.dispatcher.repository.DroneBatteryLogRepository;
import com.mosh.drone.dispatcher.repository.DroneRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Component
@RequiredArgsConstructor
public class DroneBatteryCheckTask {

  private final DroneRepository droneRepository;

  private final DroneBatteryLogRepository batteryLogRepository;

  @Scheduled(fixedRate = 60000) // Every minute
  public void checkBatteryLevels() {
    List<Drone> drones = droneRepository.findAll();
    for (Drone drone : drones) {
      DroneBatteryLog log =
          DroneBatteryLog.builder()
              .droneId(drone.getId())
              .batteryLevel(drone.getBatteryCapacity())
              .build();
      batteryLogRepository.save(log);
    }
  }
}
