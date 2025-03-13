package com.mosh.drone.dispatcher.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mosh.drone.dispatcher.exception.ExceptionOf;
import com.mosh.drone.dispatcher.mapper.DroneMapper;
import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import com.mosh.drone.dispatcher.model.request.RegisterDroneRequest;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import com.mosh.drone.dispatcher.model.response.GenericMessageResponse;
import com.mosh.drone.dispatcher.repository.DroneRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {

  @Mock private DroneRepository droneRepository;

  @Mock private DroneMapper droneMapper;

  @InjectMocks private DroneService droneService;

  private Drone drone1, drone2;
  private DroneResponse droneResponse1, droneResponse2;

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

    droneResponse1 = new DroneResponse();

    droneResponse1.setModel(DroneModel.LIGHTWEIGHT);
    droneResponse1.setState(DroneState.IDLE);
    droneResponse1.setSerialNumber("D001");
    droneResponse1.setWeightLimit(300.5);
    droneResponse1.setBatteryCapacity(50);

    droneResponse2 = new DroneResponse();

    droneResponse2.setModel(DroneModel.MIDDLEWEIGHT);
    droneResponse2.setState(DroneState.IDLE);
    droneResponse2.setSerialNumber("D002");
    droneResponse2.setWeightLimit(499.98);
    droneResponse2.setBatteryCapacity(70);
  }

  @Test
  void getAvailableDrones_ShouldReturnPagedDrones() {
    // Arrange
    int page = 0, size = 2;
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    List<Drone> droneList = List.of(drone1, drone2);
    Page<Drone> dronePage = new PageImpl<>(droneList, pageable, droneList.size());

    when(droneRepository.findByStateAndBatteryCapacityGreaterThanEqual(
            DroneState.IDLE, 25, pageable))
        .thenReturn(dronePage);
    when(droneMapper.toDroneResponse(drone1)).thenReturn(droneResponse1);
    when(droneMapper.toDroneResponse(drone2)).thenReturn(droneResponse2);

    // Act
    Page<DroneResponse> result = droneService.getAvailableDrones(page, size);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals("D001", result.getContent().get(0).getSerialNumber());
    assertEquals("D002", result.getContent().get(1).getSerialNumber());

    assertEquals(300.5, result.getContent().get(0).getWeightLimit());
    assertEquals(499.98, result.getContent().get(1).getWeightLimit());

    assertEquals(50, result.getContent().get(0).getBatteryCapacity());
    assertEquals(70, result.getContent().get(1).getBatteryCapacity());

    verify(droneRepository, times(1))
        .findByStateAndBatteryCapacityGreaterThanEqual(DroneState.IDLE, 25, pageable);
    verify(droneMapper, times(2)).toDroneResponse(any(Drone.class));
  }

  @Test
  void getDroneBatteryCapacity_ShouldReturnCorrectValue_WhenDroneExists() {
    // Arrange

    when(droneRepository.findById(drone1.getId())).thenReturn(Optional.of(drone1));

    // Act
    GenericMessageResponse response = droneService.getDroneBatteryCapacity(drone1.getId());

    // Assert
    assertNotNull(response);
    assertEquals("50 %", response.getMessage());

    verify(droneRepository, times(1)).findById(drone1.getId());
  }

  @Test
  void getDroneBatteryCapacity_ShouldThrowNotFoundException_WhenDroneDoesNotExist() {
    // Arrange
    String droneId = "InvalidId";

    when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

    assertThrows(
        ExceptionOf.Business.NotFound.NOT_FOUND.exception().getClass(),
        () -> droneService.getDroneBatteryCapacity(droneId));

    verify(droneRepository, times(1)).findById(droneId);
  }

  @Test
  void registerDrone_success() {
    // Arrange
    RegisterDroneRequest request = new RegisterDroneRequest();
    request.setBatteryCapacity(60);
    request.setModel(DroneModel.LIGHTWEIGHT);
    request.setSerialNumber("0001234");
    request.setWeightLimit(400.6);

    Drone drone = new Drone();
    drone.setState(DroneState.IDLE);
    drone.setModel(DroneModel.LIGHTWEIGHT);
    drone.setSerialNumber("0001234");
    drone.setBatteryCapacity(60);
    drone.setWeightLimit(400.6);

    DroneResponse expectedResponse = new DroneResponse();
    expectedResponse.setState(DroneState.IDLE);
    expectedResponse.setModel(DroneModel.LIGHTWEIGHT);
    expectedResponse.setSerialNumber("0001234");
    expectedResponse.setBatteryCapacity(60);
    expectedResponse.setWeightLimit(400.6);
    expectedResponse.setId(drone.getId());

    when(droneRepository.existsBySerialNumber(request.getSerialNumber())).thenReturn(false);
    when(droneMapper.toDrone(request)).thenReturn(drone);
    when(droneRepository.save(drone)).thenReturn(drone);
    when(droneMapper.toDroneResponse(drone)).thenReturn(expectedResponse);

    // Act
    DroneResponse actualResponse = droneService.registerDrone(request);

    // Assert
    assertNotNull(actualResponse);
    assertEquals(expectedResponse.getSerialNumber(), actualResponse.getSerialNumber());
    assertEquals(expectedResponse.getBatteryCapacity(), actualResponse.getBatteryCapacity());
    assertEquals(expectedResponse.getWeightLimit(), actualResponse.getWeightLimit());
    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getState(), actualResponse.getState());

    verify(droneRepository, times(1)).existsBySerialNumber(request.getSerialNumber());
    verify(droneRepository, times(1)).save(drone);
    verify(droneMapper, times(1)).toDrone(request);
    verify(droneMapper, times(1)).toDroneResponse(drone);
  }

  @Test
  void registerDrone_ShouldThrowException_WhenSerialNumberAlreadyExists() {
    // Arrange
    RegisterDroneRequest request = new RegisterDroneRequest();
    request.setBatteryCapacity(60);
    request.setModel(DroneModel.LIGHTWEIGHT);
    request.setSerialNumber("0001234");
    request.setWeightLimit(400.6);

    when(droneRepository.existsBySerialNumber(request.getSerialNumber())).thenReturn(true);

    // Act & Assert
    assertThrows(
        ExceptionOf.Business.BadRequest.BAD_REQUEST.exception().getClass(),
        () -> droneService.registerDrone(request));

    verify(droneRepository, times(1)).existsBySerialNumber(request.getSerialNumber());
    verify(droneRepository, never()).save(any());
  }
}
