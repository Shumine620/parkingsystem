package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * ParkingSpot class indicate the information of each parking spot to allowed process and ticket issuance.
 */
public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * @param number      the number of the parking spot
     * @param parkingType the type of the vehicle
     * @param isAvailable if the spot is available or not
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * @return the parking spot's number
     */
    public int getId() {
        return number;
    }

    /**
     * @param number the parking spot's number
     */
    public void setId(int number) {
        this.number = number;
    }

    /**
     * @return type of parking
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * @return true is the spot is available
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}