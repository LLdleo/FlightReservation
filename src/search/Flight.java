package search;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ServerAccessException;
import leg.SeatTypeEnum;

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
        this.coachPrice = flightToConvert.getPrice(SeatTypeEnum.COACH);
        this.firstClassPrice = flightToConvert.getPrice(SeatTypeEnum.FIRSTCLASS);
        this.travelTime = flightToConvert.calculateTravelTime();
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
}
