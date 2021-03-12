package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 *
 */
@ExtendWith(MockitoExtension.class)

public class ParkingServiceTest {

    public static ParkingService parkingService;
    public ParkingSpot parkingSpot;
    public static Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;


    @BeforeEach
    void setUpPerTest() throws Exception {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
                        lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @AfterEach
    void cleanUp() {
    }

    @Test
    public void ProcessIncomingVehicleTest() throws Exception {
        // GIVEN
        parkingSpot = new ParkingSpot(1, CAR, true);
        inputReaderUtil.readSelection();
        inputReaderUtil.readVehicleRegistrationNumber();
        ticketDAO.saveTicket(ticket);
        parkingSpotDAO.updateParking(parkingSpot);

        // WHEN
        parkingService.processIncomingVehicle();
        parkingSpotDAO.getNextAvailableSlot(CAR);
        // THEN
        assertThat(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void processExitingVehicleTest() {
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    void getNextParkingNumberIfAvailableTest() throws IOException {

        //GIVEN
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingSpot = new ParkingSpot(1, CAR, true);

        //WHEN
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingSpotDAO.getNextAvailableSlot(CAR);
        parkingService.processIncomingVehicle();
        //THEN
        assertEquals(null, parkingService.getNextParkingNumberIfAvailable());
    }
}