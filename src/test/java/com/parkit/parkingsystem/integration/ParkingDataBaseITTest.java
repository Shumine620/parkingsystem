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

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

/**
 * Class Test for the parking database functions.
 */
@ExtendWith(MockitoExtension.class)

public class ParkingDataBaseITTest {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    public static InputReaderUtil inputReaderUtil;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;
    private static ParkingType parkingType;

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
     *
     */
    @AfterAll
    public static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * @throws Exception when reading cannot be done
     */
    @BeforeEach
    public void setUpPerTest() throws Exception {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("PLMKP");
        dataBasePrepareService.clearDataBaseEntries();
    }

        @Test  //Check that a ticket is actually saved in DB and Parking table is updated with availability
    public void testParkingACar() {

        //GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingType = ParkingType.CAR;
        parkingSpotDAO.getNextAvailableSlot(parkingType);
        parkingSpotDAO = new ParkingSpotDAO();

        //WHEN
        parkingService.processIncomingVehicle();
        Ticket ticket1 = ticketDAO.getTicket("PLMKP");

        //THEN
        assertEquals(1, ticket1.getId());
        assertNotNull(parkingSpotDAO);
        assertThat(parkingSpotDAO.getNextAvailableSlot(parkingType)).isNotEqualTo(1);
    }

    /**
     * @throws Exception Exception if the parking cannot be done
     */
    @Test  //Check that the fare generated and out time are populated correctly in the database
    public void testParkingLotExitCar() throws Exception {

        //GIVEN
        Ticket ticket = new Ticket();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingType = ParkingType.CAR;
        parkingService.processIncomingVehicle();
        ticketDAO.saveTicket(ticket);

        //WHEN
        Thread.sleep(4000);
        parkingService.processExitingVehicle();
        ticket = ticketDAO.getTicket("PLMKP");
        fareCalculatorService.calculateFare(ticket);

        //THEN
        assertNotNull(ticketDAO.getTicket(ticket.getVehicleRegNumber()));
        assertEquals(1, ticket.getParkingSpot().getId());//Check ticket related to the parking spot
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertNotNull(ticket.getOutTime());
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void getNextAvailableSlot() {

        //GIVEN
        new ParkingSpot(1, CAR, false);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //WHEN
        parkingService.processIncomingVehicle();

        //THEN
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(CAR));
    }
}