package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;


/**
 * Testing the link with the application and the database mySQL to get the tickets.
 */
@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    public static DataBasePrepareService dataBasePrepareService;
    public static Ticket ticket;
    public static ParkingSpot parkingSpot;
    private static ParkingType parkingType;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        ticketDAO = new TicketDAO();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    /**
     * @throws Exception if the ticket cannot be retrieve or register
     */
    @BeforeEach
    public void setUpPerTest() throws Exception {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
        ParkingSpot parkingSpot = new ParkingSpot(1,BIKE,true);
    }

    @AfterAll
    public static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();

    }

    @Test
    public void testSaveTicket() throws IOException {
        //GIVEN
        Ticket ticket = new Ticket();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket.setParkingSpot(parkingSpot);
        parkingType = BIKE;
        parkingSpotDAO.getNextAvailableSlot(parkingType);

        //WHEN
        parkingService.processIncomingVehicle();
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
        ticketDAO.saveTicket(ticket);
        //THEN
        assertNotNull(parkingSpotDAO);
      assertTrue(ticketDAO.saveTicket(ticket));


    }

    /**
     * @throws IOException if the ticket cannot be retrieve or register
     */
    @Test
    public void testGetTicket() throws IOException {
        //GIVEN
        Ticket ticket = new Ticket();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket.setParkingSpot(parkingSpot);
        parkingType = BIKE;
        parkingSpotDAO.getNextAvailableSlot(parkingType);

        //WHEN
        parkingService.processIncomingVehicle();
        ticket.setVehicleRegNumber("MPLKN");
        ticket.setPrice(1.0);

        ticketDAO.getTicket("MPLKN");
        //THEN
        assertNotNull(parkingSpotDAO);
        assertNotNull(ticketDAO.getTicket("MPLKN"));
        return;
    }

    /**
     * @throws IOException if the ticket cannot be retrieve or register
     */
    @Test
    public void testUpdateTicket() throws IOException {
        //GIVEN
        Ticket ticket = new Ticket();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket.setParkingSpot(parkingSpot);
        parkingType = BIKE;
        parkingSpotDAO.getNextAvailableSlot(parkingType);

        //WHEN
        parkingService.processIncomingVehicle();
        ticket.setVehicleRegNumber("ABCDEF");

        ticket.setPrice(1.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
        //THEN
        assertNotNull(parkingSpotDAO);
        assertTrue(ticketDAO.updateTicket(ticket));

    }

    /**
     * @throws IOException if the ticket cannot be retrieve or register
     */
    @Test
    public void testIsReccurentUser() throws IOException {
        //GIVEN
        Ticket ticket = new Ticket();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket.setParkingSpot(parkingSpot);
        parkingType = BIKE;
        parkingSpotDAO.getNextAvailableSlot(parkingType);

        //WHEN
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(2.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
        //THEN
        assertNotNull(parkingSpotDAO);
    }
}