package com.mosh.drone.dispatcher.repository;

import com.mosh.drone.dispatcher.model.entity.DroneBatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Repository
public interface DroneBatteryLogRepository extends JpaRepository<DroneBatteryLog, String> {}
