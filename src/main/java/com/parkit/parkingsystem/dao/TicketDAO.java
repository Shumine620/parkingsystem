package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * TicketDAO linking the application with the database mySQL to get the tickets.
 * @throws Exception if the ticket cannot be retrieve or register
 */

public class TicketDAO {

    /**
     * instantiate the logger arguments to get the connection to the database and records tickets.
     */
    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * @param ticket implements the data of the tickets  with come in and out times, price
     * @return ticket
     */
    public Ticket saveTicket(Ticket ticket) {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
            ps.execute();
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }

    /**
     * @param vehicleRegNumber vehicle registration number
     * @return ticket information
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = new Ticket();
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    /**
     * @param ticket
     * @return true if the ticket has been updated correctly
     */
    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            ps.close();
            return true;
        } catch (Exception ex) {
            logger.error("Error saving ticket info", ex);

        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * @param vehicleRegNumber registration number of the vehicle
     * @return true is the user is a reccurent user
     */
    public boolean isReccurentUser(String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isReccurentUser = false;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.IS_RECCURENT_USER);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                isReccurentUser = rs.getBoolean(1);
                isReccurentUser = rs.getInt(1) > 1;
            }
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
            return false;
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            return isReccurentUser;
        }
    }
}
