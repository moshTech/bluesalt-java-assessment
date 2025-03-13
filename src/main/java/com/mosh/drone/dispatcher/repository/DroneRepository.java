package com.mosh.drone.dispatcher.repository;

import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {

  Page<Drone> findByStateAndBatteryCapacityGreaterThanEqual(
      DroneState state, int battery, Pageable pageable);

  Optional<Drone> findByIdAndStateIn(String id, List<DroneState> droneStates);

  boolean existsBySerialNumber(String serialNumber);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM drone WHERE serial_number = ?1", nativeQuery = true)
  void deleteBySerialNumber(String serialNumber); // Specifically for clean up for Integration test
}
