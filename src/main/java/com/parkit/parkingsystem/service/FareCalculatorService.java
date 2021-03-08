package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.SQLException;


public class FareCalculatorService {


    public void calculateFare(Ticket ticket)  {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        float duration = (float) ((outHour - inHour) / (60 * 60 * 1000.00));// Convert duration in milliseconds then in hour

        if (duration > 0.5) {
            duration = (float) (duration - 0.5);

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
            if (ticket.getReccurentUser() > 1) {
                ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * Fare.PERCENTAGE_DISCOUNT / 100));
            }

            //Free parking under 0.5hour
        } else {
            ticket.setPrice(0.0);
        }

    }
}

