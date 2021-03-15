package com.parkit.parkingsystem.model;

import java.sql.Time;
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
        if (inTime==null) {
            return null;
        }else{
            return new Date(inTime.getTime());
        }
           }

    /**
     * @param inTime time of vehicle getting in.
     */
    public void setInTime(Date inTime) {
        if (inTime==null) {
           this.inTime = null;
        }else{
           this.inTime = new Date(inTime.getTime());
    }}

    /**
     * @return Getter time of vehicle getting out.
     */
            public Date getOutTime(){
            if (outTime==null) {
                return null;
            }else{
                return new Date(outTime.getTime());
            }
        }
    /**
     * @param outTime time of vehicle getting out.
     */
        public void setOutTime(Date outTime) {
            if (inTime == null) {
                this.outTime = null;
            } else {
                this.outTime = new Date(outTime.getTime());
            }}

            /**
             * @return Getter true is the user is reccurent to the parking system.
             */
            public boolean getReccurentUser () {
                return reccurentUser;
            }

            /**
             * @param reccurentUser true is the user is reccurent to the parking system.
             */
            public void setReccurentUser ( boolean reccurentUser){
                this.reccurentUser = reccurentUser;
            }
        }

