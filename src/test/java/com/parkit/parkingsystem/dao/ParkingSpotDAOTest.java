package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertThat;


class ParkingSpotDAOTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static Ticket ticket;
    private static ParkingSpot parkingSpot;
    private static ParkingType parkingType;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");


    @BeforeEach
    public void setUpPerTest() {
        dataBasePrepareService = new DataBasePrepareService();
        parkingSpotDAO = new ParkingSpotDAO();
    }

    @AfterEach
    void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void getNextAvailableSlot() throws IOException {
        //GIVEN
        parkingSpot = new ParkingSpot(1, CAR, false);

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        parkingSpotDAO.getNextAvailableSlot(CAR);

        //THEN
        assertFalse(parkingSpot.isAvailable());
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(CAR));


    }

    @Test
    void updateParking() throws IOException {
        //GIVEN
        parkingSpot = new ParkingSpot(2, CAR, false);

        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        assertFalse(parkingSpot.isAvailable());

        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }
}