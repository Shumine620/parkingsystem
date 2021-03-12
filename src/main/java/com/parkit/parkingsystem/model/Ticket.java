package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * Complete ticket information.
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean reccurentUser;


    /**
     * @return getter the id of the ticket.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id of the ticket.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Getter the parking spot.
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * @param parkingSpot the parking spot
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * @return Getter of the vehicle registration number.
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * @param vehicleRegNumber number of the vehicle registration number.
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * @return Getter price of the ticket.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price of the ticket.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return Getter time of vehicle getting in.
     */
    public Date getInTime() {
        return inTime;
    }

    /**
     * @param inTime time of vehicle getting in.
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * @return Getter time of vehicle getting out.
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * @param outTime time of vehicle getting out.
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    /**
     * @return Getter true is the user is reccurent to the parking system.
     */
    public boolean getReccurentUser() {
        return reccurentUser;
    }

    /**
     * @param reccurentUser true is the user is reccurent to the parking system.
     */
    public void setReccurentUser(boolean reccurentUser) {
        this.reccurentUser = reccurentUser;
    }

}
