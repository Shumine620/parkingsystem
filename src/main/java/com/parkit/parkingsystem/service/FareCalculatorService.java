package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {


    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime());
        duration = duration / (60 * 60 * 1000);// Convert duration in milliseconds

        if (duration <= 0.5) {
            ticket.setPrice(0.0);
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
        TicketDAO ticketRecurent = new TicketDAO();
        if (ticketRecurent.ticketNum(ticket.getVehicleRegNumber())>1) {
            duration = duration * 0.05;
        }
    }
}
/**
 * public void calculateDiscount (Ticket ticket, double discount){
 * calculateFare(ticket);
 * ticket.setPrice(ticket.getPrice() * Fare.PERCENTAGE_DISCOUNT);
 * }
 */
