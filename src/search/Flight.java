package search;

import airplane.AirplaneCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ServerAccessException;
import leg.SeatTypeEnum;
import time.MyTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hold search.ConnectingLegs with extended information for searching flights.
 * Significant associations: search.ConnectingLeg for the objects that compose this flight and leg.ConnectingLeg for getting search.ConnectingLeg and calculating flight statistics.
 *
 * @author Jackson Powell
 * @since 2020-05-01
 */
public class Flight {
    /**
     * numLegs is the number of connecting legs in this flight. Must be between 1 and 3
     */
    private int numLegs;
    /**
     * connectingLegList is the list of connecting legs that make up this flight
     */
    private ConnectingLegs connectingLegList;
    /**
     * travelTime is the time from the first legs' scheduled departure to the last leg's scheduled arrival.
     */
    private double travelTime;
    /**
     * coachPrice is the total price of reserving a coach seat on each leg of the flight.
     */
    private double coachPrice;
    /**
     * firstClassPrice is the total price of reserving a first class seat on each leg of the flight.
     */
    private double firstClassPrice;

    /**
     * Constructor for a flight with extended information.
     *
     * @throws ServerAccessException If there is an issue connecting to the timezone server when calculating the local time.
     * @pre flightToConvert has already been validated as a valid flight that meets the search criteria.
     * @post A new Flight object is instantiated that has calculated information necessary for sorting and filtering.
     * @param flightToConvert The validated flight to convert into a flight with connecting legs with more information.
     */
    public Flight(leg.Flight flightToConvert) throws ServerAccessException {
        this.connectingLegList = new ConnectingLegs();
        Iterator<leg.ConnectingLeg> iter = flightToConvert.getLegs();
        while(iter.hasNext()){
            this.connectingLegList.add(search.ConnectingLeg.convertLeg(iter.next()));
        }
        this.numLegs = this.connectingLegList.size();
        this.coachPrice = Math.round(100.0 * flightToConvert.getPrice(SeatTypeEnum.COACH)) / 100.0;
        this.firstClassPrice = Math.round(100.0 * flightToConvert.getPrice(SeatTypeEnum.FIRSTCLASS)) / 100.0;
        this.travelTime = Math.round(100.0 * flightToConvert.calculateTravelTime()) / 100.0;
    }

    /**
     * Get the number of legs that this flight is made up of.
     *
     * @return The number of legs that this flight is made up of.
     */
    public int getNumLegs() {
        return numLegs;
    }

    /**
     * Get the connecting legs that make up this flight.
     *
     * @return The connecting legs that make up this flight.
     */
    public ConnectingLegs getConnectingLegList() {
        return connectingLegList;
    }

    /**
     * Get the total amount of time from the first leg's scheduled departure to the last leg's scheduled arrival in hours.
     *
     * @return the total amount of time from the first leg's scheduled departure to the last leg's scheduled arrival in hours.
     */
    public double getTravelTime() {
        return travelTime;
    }

    /**
     * Get the total price of reserving a coach seat on each leg of the flight.
     *
     * @return the total price of reserving a coach seat on each leg of the flight.
     */
    public double getCoachPrice() {
        return coachPrice;
    }

    /**
     * Get the total price of reserving a first class seat on each leg of the flight.
     *
     * @return the total price of reserving a first class seat on each leg of the flight.
     */
    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    /**
     * Check if there is at least one seat available of the given seat type on each connecting leg of the flight.
     *
     * @param seatType The seat type to check availability of.
     * @return True if there is at least one seat of the given seat type available on each connecting leg of the flight.
     */
    public boolean allSeatsAvailable(SeatTypeEnum seatType){
        int totalLegsCount = this.getConnectingLegList().size();
        int availableLegsCount = (int) this.getConnectingLegList().stream().filter(leg ->
                leg.getSeating().getNumReserved(seatType) < leg.getAirplane().getNumSeats(seatType)).count();
        return totalLegsCount == availableLegsCount;
    }

    /**
     * Get departure time of the first leg of the flight.
     *
     * @return the departure time of the first leg of the flight.
     */
    public MyTime getDepartureTime(){
        return this.connectingLegList.get(0).getDepartureTime();
    }

    /**
     * Get arrival time of the last leg of the flight.
     *
     * @return the arrival time of the last leg of the flight.
     */
    public MyTime getArrivalTime(){
        return this.connectingLegList.get(numLegs-1).getArrivalTime();
    }

    /**
     * Check if a departure or arrival time fits within a given time window.
     *
     * @param start The start of the time window.
     * @param end The end of the time window.
     * @param isDep True if checking the departure time, false if checking the arrival time.
     * @return True if a flight fits in the time window. False otherwise.
     */
    public boolean inRange(LocalTime start, LocalTime end, boolean isDep){
        MyTime dateTime = isDep ? getDepartureTime() : getArrivalTime();
        LocalTime localTime = dateTime.getLocalTime().toLocalTime();
        boolean equal = localTime.getHour() == start.getHour() && localTime.getMinute() == start.getMinute();
        equal = equal || localTime.getHour() == end.getHour() && localTime.getMinute() == end.getMinute();
        return equal || (localTime.isAfter(start) && localTime.isBefore(end));
    }

    /**
     * Get the leg.Flight version of this object.
     *
     * @return the leg.Flight version of this object.
     */
    public leg.Flight convertBack(){
        leg.ConnectingLegs legs = new leg.ConnectingLegs();
        for(search.ConnectingLeg leg : connectingLegList){
            legs.add(leg.convertBack());
        }
        return new leg.Flight(legs);
    }

    /**
     * Get the string representation of the flight including flight statistics and connecting leg information.
     *
     * @return the string representation of the flight including flight statistics and connecting leg information for the user to view.
     */
    public String toString(){
        StringBuilder toReturn = new StringBuilder();
        toReturn.
                append("Coach price: $").append(this.getCoachPrice()).append("\t").
                append("First Class Price: $").append(this.getFirstClassPrice()).append("\n").
                append("Travel time: ").append(this.getTravelTime()).append(" hours\t").
                append("Number of legs: ").append(this.getNumLegs()).append("\nLegs: \n");
        for(ConnectingLeg leg: this.getConnectingLegList()){
            toReturn.append(leg.toString());
        }
        return toReturn.toString();
    }
}
