package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * FareCalculatorService allowed to calculate the fares to be applied to each parking user.
 * Upon time, under 30 minutes it is free. Reccurent users have 5% discount.
 */
public class FareCalculatorService {

    /**
     * @param ticket to be issue to the client
     * @throws IllegalArgumentException if ticket cannot be issue
     * @see Ticket
     */
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            assert ticket.getOutTime() != null;
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString()); //to avoid nullPointer exception
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double duration = (outHour - inHour) / (60 * 60 * 1000.00); // Convert duration in milliseconds then in hour

        if (duration > 0.5) {

            //Normal fare above 0.5hour
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
            //Discount for recurrent user
            if (ticket.getReccurentUser()) {
                ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * Fare.PERCENTAGE_DISCOUNT / 100));
            }
        } else {
            ticket.setPrice(0.0);
        }
    }
}


