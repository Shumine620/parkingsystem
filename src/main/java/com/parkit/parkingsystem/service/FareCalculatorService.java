package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {


    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double duration =  (outHour - inHour)/ (60 * 60 * 1000);// Convert duration in milliseconds then in hour


        //Free parking under 0.5hour
        if (duration < 0.5) {
           duration = 0;
       }else{ duration = (duration - 0.5);
            }


        //Normal fare above 0.5hour (check to remove 30minutes)
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration  * Fare.CAR_RATE_PER_HOUR);
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
       TicketDAO ticketRecurrent = new TicketDAO();

        if (ticket.getReccurentUser()> 1) {
          Math.round(ticket.setPrice(ticket.getPrice() - (ticket.getPrice()*Fare.PERCENTAGE_DISCOUNT/100)));
        }

    }


}

