package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.parkit.parkingsystem.ParkingServiceTest.parkingService;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the link with the database and the parking spots data enters.
 */
class ParkingSpotDAOTest {
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot;

    @Mock
    public static InputReaderUtil inputReaderUtil;

    @BeforeEach
    public void setUpPerTest() {
        dataBasePrepareService = new DataBasePrepareService();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, new TicketDAO());

    }

    @AfterEach
    void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * Test the capacity of getting the right next available spot.
     */
    @Test
    public void getNextAvailableSlot(){

        //GIVEN
        parkingSpot = new ParkingSpot(1, CAR, true);

        //WHEN
        inputReaderUtil= new InputReaderUtil();
        parkingService.processIncomingVehicle();
        parkingSpotDAO.updateParking(parkingSpot);

        //THEN
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(CAR));
    }

    /**
     * Test the capacity of updating the parking spot.
     */
    @Test
    void updateParking(){

        //GIVEN
        parkingSpot = new ParkingSpot(2, CAR, false);

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);

        //THEN
        assertFalse(parkingSpot.isAvailable());
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    /**
     * Test the parkingSPotDAO when the ParkingType is null.
     */
    @Test
    public void updateParkingWithNullParkingTypeTest(){

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
}