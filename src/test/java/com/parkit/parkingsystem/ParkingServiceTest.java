package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;


import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class Test for the parking services.
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

    //private Date inTime;
    //private Date outTime;


    @BeforeEach
    void setUpPerTest(){
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
    public void processExitingVehicleTest() throws Exception {
        Ticket ticket = new Ticket();
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    }

    @Test
    public void getNextParkingNumberIfAvailableTest() throws IOException {

        //GIVEN
        Ticket ticket = new Ticket();
       parkingService.getNextParkingNumberIfAvailable();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingSpot = new ParkingSpot(3, CAR, false);

        //WHEN
        parkingService.processIncomingVehicle();
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingSpotDAO.getNextAvailableSlot(CAR);


        //THEN
        assertEquals(3,parkingService.getNextParkingNumberIfAvailable());
       // assertNull(parkingService.getNextParkingNumberIfAvailable());
       // assertThat(parkingService.getNextParkingNumberIfAvailable().getId()).isEqualTo(parkingSpot.getId());
       // assertThat(parkingService.getNextParkingNumberIfAvailable().isAvailable()).isEqualTo(true);

    }

    @Test
    public void parkingServiceFullTest() throws IOException {

        //WHEN
        lenient().when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        lenient().when(parkingSpotDAO.getNextAvailableSlot(CAR)).thenReturn(0);

        //THEN
        try {
            parkingService.getNextParkingNumberIfAvailable();
        } catch (Exception e) {
            String message = e.getMessage();
            assertTrue(message.contains("Error fetching next available parking slot"));
        }
    }

    @Test
    public void processExitingBikeTest() throws Exception {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (120 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("AAAAA");
        //WHEN
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AAAAA");
        lenient().when(ticketDAO.getTicket(ArgumentMatchers.anyString())).thenReturn(ticket);
        lenient().when(ticketDAO.updateTicket(ArgumentMatchers.any(Ticket.class))).thenReturn(true);
        parkingService.processExitingVehicle();

        //THEN
        // assertEquals(2.0,ticket.getPrice());
        assertThat(ticket.getVehicleRegNumber());
        assertThat(ticket);

    }


}