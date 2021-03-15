package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Class accessing all the parking services.
 */
public class ParkingService {

    /**
     * @see Logger
     */
    private static final Logger logger = LogManager.getLogger("ParkingService");
    /**
     * @see InputReaderUtil
     */
    private final InputReaderUtil inputReaderUtil;
    /**
     * @see ParkingSpot
     */
    private final ParkingSpotDAO parkingSpotDAO;
    /**
     * @see TicketDAO
     */
    private final TicketDAO ticketDAO;
    /**
     * @see FareCalculatorService
     */
    public static final FareCalculatorService fareCalculatorService = new FareCalculatorService();
    /**
     * @see Ticket
     */
    public Ticket ticket;

    /**
     * @param inputReaderUtil Reading the information from the input user
     * @param parkingSpotDAO  Parking spot register in the database
     * @param ticketDAO       Ticket register in the database
     */
    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Process when a vehicle is getting in the car park.
     * @see Ticket
     * @see ParkingSpotDAO
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false
                Date inTime = new Date();
                Ticket ticket = new Ticket();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0.0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticket.setReccurentUser(false);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);

                if (ticketDAO.isReccurentUser(vehicleRegNumber)) {
                    System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
                }
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
        }
    }

    /**
     * @return the vehicle registration number
     * @throws Exception if the reading cannot be done
     */
    private String getVehicleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * @return the parking spot
     * @see ParkingSpot
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = new ParkingSpot(parkingNumber,ParkingType.CAR,true);
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * @return the vehicle type
     * @see ParkingType
     */
    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Process when a vehicle is exiting the car park.
     * @see Ticket
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            ticket.setReccurentUser(ticketDAO.isReccurentUser(vehicleRegNumber));
            fareCalculatorService.calculateFare(ticket);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
        }
    }
}