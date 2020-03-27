package com.PoLYmer.flightreservation.flight;

public class Seating {
    /* First class price as attribute */
    String firstClassPrice;
    /* Number of first class seats already reserved */
    int firstClassReserved;
    /* Coach price as attribute */
    String coachPrice;
    /* Number of coach seats already reserved */
    int coachReserved;

    public String getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(String firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public int getFirstClassReserved() {
        return firstClassReserved;
    }

    public void setFirstClassReserved(int firstClassReserved) {
        this.firstClassReserved = firstClassReserved;
    }

    public String getCoachPrice() {
        return coachPrice;
    }

    public void setCoachPrice(String coachPrice) {
        this.coachPrice = coachPrice;
    }

    public int getCoachReserved() {
        return coachReserved;
    }

    public void setCoachReserved(int coachReserved) {
        this.coachReserved = coachReserved;
    }

    /** Seating Info */
    public void seating(String firstClassPrice, Integer firstClassReserved, String coachPrice, Integer coachReserved) {
        this.firstClassPrice = firstClassPrice;
        this.firstClassReserved = firstClassReserved;
        this.coachPrice = coachPrice;
        this.coachReserved = coachReserved;
    }
    public Seating(){

    }
}
