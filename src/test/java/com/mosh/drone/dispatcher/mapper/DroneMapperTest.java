package com.mosh.drone.dispatcher.mapper;

import com.mosh.drone.dispatcher.model.entity.Drone;
import com.mosh.drone.dispatcher.model.enumeration.DroneModel;
import com.mosh.drone.dispatcher.model.enumeration.DroneState;
import com.mosh.drone.dispatcher.model.response.DroneResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */

@ExtendWith(MockitoExtension.class)
public class DroneMapperTest {

    @InjectMocks
    private DroneMapper droneMapper;

    private Drone drone;

    @BeforeEach
    void setUp() {
        drone = new Drone();
        drone.setModel(DroneModel.LIGHTWEIGHT);
        drone.setState(DroneState.IDLE);
        drone.setSerialNumber("D001");
        drone.setWeightLimit(300.5);
        drone.setBatteryCapacity(50);
    }


    @Test
    void testToDroneMapper(){

        DroneResponse droneResponse = droneMapper.toDroneResponse(drone);

        // Assert
        assertNotNull(droneResponse);
        assertEquals(drone.getId(), droneResponse.getId());
        assertEquals(drone.getSerialNumber(), droneResponse.getSerialNumber());
        assertEquals(drone.getModel(), droneResponse.getModel());
        assertEquals(drone.getWeightLimit(), droneResponse.getWeightLimit());
        assertEquals(drone.getBatteryCapacity(), droneResponse.getBatteryCapacity());

    }
}
