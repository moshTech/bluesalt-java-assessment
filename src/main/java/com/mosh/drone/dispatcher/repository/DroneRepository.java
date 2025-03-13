package com.mosh.drone.dispatcher.repository;

import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {

  Page<Drone> findByStateAndBatteryCapacityGreaterThanEqual(
      DroneState state, int battery, Pageable pageable);
}
