package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

/**
 *
 */
@ExtendWith(MockitoExtension.class)

public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;
    private static FareCalculatorService fareCalculatorService;
    private static ParkingSpot parkingSpot;
    private static ParkingType parkingType;

    @Mock
    public static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        fareCalculatorService = new FareCalculatorService();
    }

    /**
     * @throws Exception
     */
    @BeforeEach
    public void setUpPerTest() throws Exception {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     *
     */
    @AfterAll
    public static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * @throws IOException
     */
    @Test  //Check that a ticket is actually saved in DB and Parking table is updated with availability
    public void testParkingACar() throws IOException {
        //GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, ticket);
        parkingType = ParkingType.CAR;
        parkingSpotDAO.getNextAvailableSlot(parkingType);

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        //THEN
        assertEquals(1, ticket.getId());// Check ticket with RegNumber
        assertNotNull(parkingSpotDAO);
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)); //Check that if slot 1 is busy, slot 2 is allocated
        assertFalse(ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable());
    }

    /**
     * @throws Exception
     */
    @Test  //Check that the fare generated and out time are populated correctly in the database
    public void testParkingLotExitCar() throws Exception {

        //GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, ticket);
        parkingType = ParkingType.CAR;
        parkingService.processIncomingVehicle();
        ticketDAO.saveTicket(ticket);

        //WHEN
        Thread.sleep(4000);
        parkingService.processExitingVehicle();
        ticket = ticketDAO.getTicket("ABCDEF");
        fareCalculatorService.calculateFare(ticket);

        //THEN
        assertNotNull(ticketDAO.getTicket(ticket.getVehicleRegNumber()));
        assertEquals(1, ticket.getParkingSpot().getId());//Check ticket related to the parking spot
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertNotNull(ticket.getOutTime());
        assertEquals(0, ticket.getPrice());
    }
}