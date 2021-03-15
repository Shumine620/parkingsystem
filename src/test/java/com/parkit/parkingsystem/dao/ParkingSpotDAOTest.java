package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the link with the database and the parking spots data enters.
 */
class ParkingSpotDAOTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot;

    @BeforeEach
    public void setUpPerTest() {
        dataBasePrepareService = new DataBasePrepareService();
        parkingSpotDAO = new ParkingSpotDAO();
        Ticket ticket = new Ticket();
    }

    @AfterEach
    void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * Test the capacity of getting the right next available spot.
     * @throws Exception in case an error arise when checking the database
     */
    @Test
    public void getNextAvailableSlot() throws Exception {
        //GIVEN
        Ticket ticket = new Ticket();
        parkingSpot = new ParkingSpot(1, CAR, false);

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        parkingSpotDAO.getNextAvailableSlot(CAR);

        //THEN
        //assertFalse(parkingSpot.isAvailable());
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(CAR));
    }

    /**
     * Test the capacity of updating the parking spot.
     * @throws Exception in case an error arise when checking the database
     */
    @Test
    void updateParking() throws Exception {
        //GIVEN
        parkingSpot = new ParkingSpot(2, CAR, false);

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        assertFalse(parkingSpot.isAvailable());

        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    /**
     * Test the parkingSPotDAO when the ParkingType is null.
     * @throws Exception when the program cannot be run
     */
    @Test
    public void updateParkingWithNullParkingTypeTest() throws Exception {
        //WHEN
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
}