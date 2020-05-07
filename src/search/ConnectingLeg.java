package search;

import airplane.Airplane;
import airplane.AirplaneCache;
import airplane.Airplanes;
import airport.Airport;
import airport.AirportCache;
import airport.Airports;
import dao.ServerAccessException;
import dao.ServerInterface;
import leg.Seating;
import time.MyTime;
import utils.Saps;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsibilities: Contain all connecting leg flight information for passing to the frontend for self-contained filtering and sorting.
 * Significant associations: leg.ConnectingLeg which this object is based on, extending the information by finding the objects referenced by the keys for Airport, Airplane, and MyTime.
 *
 * @author Jackson Powell
 * @since 2020-05-01
*/
public class ConnectingLeg {
    /**
     * cache is the in-memory cache for already converted ConnectingLegs.
     */
    private static Map<leg.ConnectingLeg, ConnectingLeg> cache = new HashMap<>();
    /**
     * departureAirport is the airport that this leg is departing from, including all the airport's information.
     */
    private Airport departureAirport;
    /**
     * arrivalAirport is the airport that this leg is arriving at, including all the airport's information.
     */
    private Airport arrivalAirport;
    /**
     * airplane is the information for the airplane model that this leg is departing from.
     */
    private Airplane airplane;
    /**
     * seating is the information for the number of seats and prices.
     */
    private Seating seating;
    /**
     * departureTime is the gmt and local time of the scheduled departure time for this leg.
     */
    private MyTime departureTime;
    /**
     * arrivalTime is the gmt and local time of the scheduled departure time for this leg.
     */
    private MyTime arrivalTime;
    /**
     * flightNumber is the identifying number for this flight leg
     */
    private String flightNumber;
    /**
     * flightTime is the number of minutes that this leg takes to go from the departure airport to the arrival airport
     */
    private String flightTime;

    /**
     * Constructor for a ConnectingLeg with all related information.
     *
     * @throws ServerAccessException If there is an issue connecting to the timezone server when calculating the local time.
     * @pre legToConvert's flight information has already been validate and is part of an available flight for some search. LegToConvert is not already in the cache
     * @post A new ConnectingLeg object is instantiated with the same, but extended, information as legToConvert
     * @param legToConvert The ConnectingLeg that only has key information for airports and airplanes.
     */
    private ConnectingLeg(leg.ConnectingLeg legToConvert) throws ServerAccessException {
        this.departureAirport = AirportCache.INSTANCE.getAirportByCode(legToConvert.departure().getCode());
        this.arrivalAirport = AirportCache.INSTANCE.getAirportByCode(legToConvert.arrival().getCode());
        this.airplane = AirplaneCache.INSTANCE.getAirplaneByModel(legToConvert.airplane());
        this.departureTime = new MyTime(legToConvert.departure().getTime(),departureAirport.latitude(),departureAirport.longitude());
        this.arrivalTime = new MyTime(legToConvert.arrival().getTime(),arrivalAirport.latitude(),arrivalAirport.longitude());
        this.seating = legToConvert.seating();
        this.flightNumber = legToConvert.number();
        this.flightTime = legToConvert.flightTime();
    }

    /**
     * Return the converted version of legToConvert with additional information.
     *
     * @throws ServerAccessException If there is an issue connecting to the timezone server when calculating the local time.
     * @pre legToConvert's flight information has already been validate and is part of an available flight for some search.
     * @post If legToConvert is not already in the cache, then a new object is instantiated and added to the cache.
     * @param legToConvert The ConnectingLeg that only has key information for airports and airplanes.
     * @return The converted ConnectingLeg object
     */
    public static ConnectingLeg convertLeg(leg.ConnectingLeg legToConvert) throws ServerAccessException{
        if(cache.containsKey(legToConvert)){
            return cache.get(legToConvert);
        }
        ConnectingLeg newLeg = new ConnectingLeg(legToConvert);
        cache.put(legToConvert,newLeg);
        return newLeg;
    }

    /**
     * Get the airport that this leg departs from.
     *
     * @return the airport that this leg departs from.
     */
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    /**
     * Get the airport that this leg arrives at.
     * @return The airport that this leg arrives at.
     */
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Get the airplane that this leg flies using.
     *
     * @return The airplane that this leg flies using.
     */
    public Airplane getAirplane() {
        return airplane;
    }

    /**
     * Get the seating info for this leg including the number of seats reserved and their prices.
     *
     * @return the seating info for this leg including the number of seats reserved and their prices.
     */
    public Seating getSeating() {
        return seating;
    }

    /**
     * Get the time (both local and gmt) that this leg departs.
     * @return the time (both local and gmt) that this leg departs.
     */
    public MyTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Get the time (both local and gmt) that this leg arrives.
     * @return the time (both local and gmt) that this leg arrives.
     */
    public MyTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Get the identifying flight number for this leg.
     *
     * @return The identifying flight number for this leg.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Get the time of the flight (in minutes).
     * @return the time of the flight (in minutes).
     */
    public String getFlightTime() {
        return flightTime;
    }
    public String toString(){
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Departs at ").append(this.departureAirport.name()).append(" at ").append(timeString(getDepartureTime().getLocalTime())).append("\n").
                append("Arrives at ").append(this.arrivalAirport.name()).append(" at ").append(timeString(getArrivalTime().getLocalTime())).append("\n").
                append("Airplane: ").append(airplane.toString()).append("\n\n");
        return toReturn.toString();
    }
    public String timeString(LocalDateTime time){
        return "" + time.getYear() + " " + time.getMonth().toString() + " " + time.getDayOfMonth() + " " +
                String.format("%02d",time.getHour()) + ":" + String.format("%02d",time.getMinute());
    }
}
