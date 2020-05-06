package leg;

import airplane.AirplaneCache;
import airplane.Airplanes;
import dao.ServerAccessException;
import dao.ServerInterface;
import search.CreatePossibleFlights;
import time.MyTime;
import utils.Saps;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.stream.Collectors;

import static leg.SeatTypeEnum.FIRSTCLASS;

/**
 * @author Jackson Powell
 * @since 2020-04-21
 * Responsibilities: Store sets of connecting legs as a flight
 * Significant associations: leg.ConnectingLeg for the objects contained and the validity of their variables to determine if they form a valid flight. Also associate with seatType for calculating various aggregation functions for the properties of the flight, such as price and availability.
 */
public class Flight {
    /**
     * connectingLegs is the list of connecting legs that make up the flight. Length of list must be in range [1,3].
     */
    private ConnectingLegs connectingLegs;

    /**
     * Constructor for Flight for building it up from one flight leg.
     *
     * @pre firstLeg is valid ConnectingLeg.
     * @post A Flight object is instantiated and populated with one ConnectingLeg since one-leg flights are always valid if the leg itself is valid.
     * @param firstLeg The first leg of the flight.
     */
    public Flight(ConnectingLeg firstLeg){
        connectingLegs = new ConnectingLegs();
        connectingLegs.add(firstLeg);
    }

    /**
     * Constructor for Flight if list of legs already created
     *
     * @pre connectingLegs should be ordered to create a valid flight, otherwise an IllegalArgumentException will be thrown.
     * @post If valid, then a flight object is instantiated and populated with connectingLegs. If invalid, a flight object will not be instantiated and an IllegalArgumentException will be thrown.
     * @param connectingLegs The list of legs to make into a flight.
     * @throws IllegalArgumentException If given connectingLegs are not ordered to constitute a valid Flight
     */
    public Flight(ConnectingLegs connectingLegs) throws IllegalArgumentException{
        if(!isValid(connectingLegs)){
            throw new IllegalArgumentException("Legs don't meet criteria to constitute a flight");
        }
        this.connectingLegs = connectingLegs;
    }

    /**
     * Determine if a list of connecting legs would constitute a valid flight (<=3 legs. reasonable layover times, and compatible airports)
     *
     * @param connectingLegsToCheckIfValid The list of connnecting legs to check if they would make a valid flight.
     * @return True if the set of legs would constitute a valid flight, false otherwise.
     */
    public static boolean isValid(ConnectingLegs connectingLegsToCheckIfValid){
        if (connectingLegsToCheckIfValid.size() > Saps.MAX_LEGS || connectingLegsToCheckIfValid.size() == 0){
            return false;
        }
        if (connectingLegsToCheckIfValid.size() == 1){
            return true;
        }
        for (int i = 0; i < connectingLegsToCheckIfValid.size()-1; i++){
            ConnectingLeg legI = connectingLegsToCheckIfValid.get(i);
            ConnectingLeg legII = connectingLegsToCheckIfValid.get(i+1);
            if(!legI.arrival().code.equalsIgnoreCase(legII.departure().code)){
                return false;
            }
            // Just need to compare gmttimes, so will just provide arbitrary lat, long to avoid accessing server
            MyTime arrivingTime = new MyTime(legI.arrival().time);
            MyTime departingTime = new MyTime(legII.departure().time);
            double layoverTime = arrivingTime.timespan(departingTime);
            if(layoverTime < Saps.MIN_LAYOVER_TIME_HOURS || layoverTime > Saps.MAX_LAYOVER_TIME_HOURS){
                return false;
            }
        }
        return true;
    }

    /**
     * Try to add new leg to the end of the current set of legs.
     *
     * @pre newLeg is a valid ConnectingLeg.
     * @post If this method returns true, then this flight has added newleg to the end. If false, this flight's connecting legs remain unchanged.
     * @param newLeg The new connecting leg to append to the end of the flight
     * @return True if the leg was added to the flight, false if adding would not make a valid flight and so was not added.
     */
    public boolean addLeg(ConnectingLeg newLeg){
        this.connectingLegs.add(newLeg);
        if(isValid(this.connectingLegs)){
            return true;
        }
        this.connectingLegs.remove(this.connectingLegs.size()-1);
        return false;
    }

    /**
     * Try to add new leg to the end or start of the current set of legs.
     *
     * @pre newLeg is a valid ConnectingLeg.
     * @post If this method returns true, then this flight has added newleg to the end or start of its connecting legs based on addAtEnd (true if adding it to the end). If false, this flight's connecting legs remain unchanged.
     * @param newLeg The new connecting leg to append to the end or start of the flight.
     * @param addAtEnd True if newLeg should be appended at the end of the current list, false if newLeg should be appended at the start.
     * @return True if the leg was added to the flight, false if adding would not make a valid flight and so was not added.
     */
    public boolean addLeg(ConnectingLeg newLeg, boolean addAtEnd){
        if (addAtEnd)
            return addLeg(newLeg);
        this.connectingLegs.add(0,newLeg);
        if(isValid(this.connectingLegs)){
            return true;
        }
        this.connectingLegs.remove(0);
        return false;
    }
    /**
     * Calculate the travel time of this flight in hours
     * Calculate the travel time of this flight in hours as the timespan from the first leg's scheduled departure time to the last leg's scheduled arrival time.
     *
     * @pre The flight is fully constructed from the desired start airport to the desired end airport. Otherwise partial calculation is returned.
     * @return The travel time of the flight in hours from the first departure to the last arrival
     */
    public double calculateTravelTime(){
        MyTime firstDeparture = new MyTime(this.connectingLegs.get(0).departure().time);
        MyTime lastArrival = new MyTime(this.connectingLegs.get(this.connectingLegs.size()-1).arrival().time);
        return firstDeparture.timespan(lastArrival);
    }

    /**
     * Get the total cost of the connecting legs based on the given seat type
     *
     * @pre The flight is fully constructed from the desired start airport to the desired end airport. Otherwise partial calculation is returned.
     * @param seatType The seat type to calculate the cost of the seats for.
     * @return The total cost of a seat of the given seat type on each leg in the flight.
     */
    public double getPrice(SeatTypeEnum seatType){
        return this.connectingLegs.stream().map(leg -> getLegPrice(leg, seatType)).mapToDouble(Double::valueOf).sum();
    }

    /**
     * Get price of a type of seat on a connecting leg. Defaults to coach if illegal seatType given.
     *
     * @pre leg is a valid ConnectingLeg with valid prices for each seat type.
     * @param leg The connecting leg to get the price of.
     * @param seatType The type of seat to check the price for.
     * @return The price of a seat of the seat type on the given leg.
     */
    private double getLegPrice(ConnectingLeg leg, SeatTypeEnum seatType){
        //DecimalFormat format = new DecimalFormat("0.00");
        if (seatType == FIRSTCLASS){return Math.round(100 * leg.seating().getFirstClassPrice())/100.0;}
        return Math.round(100 * leg.seating().getCoachPrice())/100.0;
    }
    /**
     * Checks if there are still seats for this flight available.
     *
     * @throws ServerAccessException If there was an issue connecting to the WPI server
     * @inv All fields for this flight's connecting legs remain constant other than the number of reserved seats.
     * @post This flight's connecting legs' number of reserved seats are refreshed with the latest information if the system can connect with the WPI server.
     * @return True if there are seats available on the trip's seat type for every leg of the trip's outgoing flight
     */
    public boolean allSeatsStillAvailable(SeatTypeEnum seatType) throws ServerAccessException{
        int totalLegsCount = this.connectingLegs.size();
        refresh();
        int availableLegsCount = (int) this.connectingLegs.stream().filter(leg ->
                leg.seating().getNumReserved(seatType) < AirplaneCache.INSTANCE.getAirplaneByModel(leg.airplane()).getNumSeats(seatType)).count();
        return totalLegsCount == availableLegsCount;
    }

    /**
     * Update this flights list of connecting legs with the latest number of reserved seats.
     *
     * @throws ServerAccessException If there is an issue connecting with the WPI server
     * @pre The connecting legs of this flight have been validated and still exist in the WPI server.
     * @post The connecting legs of this flight will have the latest reservation data.
     */
    public void refresh() throws ServerAccessException{
        ConnectingLegs newList = new ConnectingLegs();
        for(ConnectingLeg leg: this.connectingLegs){
            ConnectingLegs results =
                    ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing",leg.departure().code,
                            CreatePossibleFlights.getStringDate(new MyTime(leg.departure().time).getGmtTime().toLocalDate()));
            newList.add(results.getLegByID(leg.number()));
        }
        this.connectingLegs = newList;
    }
    /**
     * Get the airport code for the arrival of the last connecting leg of this flight.
     *
     * @return the airport code for the arrival of the last connecting leg of this flight.
     */
    public String getArrivalAirportCode(){
        return this.connectingLegs.get(this.connectingLegs.size()-1).arrival().code;
    }

    /**
     * Get the airport code for the departure of the first connecting leg of this flight.
     *
     * @return the airport code for the departure of the first connecting leg of this flight.
     */
    public String getDepartureAirportCode(){
        return this.connectingLegs.get(0).departure().code;
    }
    /**
     * Get the airport code for the arrival or departure of the last/first connecting leg of this flight.
     *
     * @param isDeparture True if the departure code should be returned, false if the arrival code should be returned.
     * @return the airport code for the arrival or departure of the last/first connecting leg of this flight based on isDeparture.
     */
    public String getAirportCode(boolean isDeparture){
        return isDeparture ? this.getDepartureAirportCode() : this.getArrivalAirportCode();
    }
    /**
     * Get the GMT time for the arrival of the last connecting leg of this flight.
     *
     * @pre The arrival time for each leg is in the format specified in utils.Saps in TIME_FORMAT.
     * @return the time for the arrival of the last connecting leg of this flight.
     */
    public MyTime getArrivalTime(){
        return new MyTime(this.connectingLegs.get(this.connectingLegs.size()-1).arrival().time);
    }
    /**
     * Get the time for the departure of the first connecting leg of this flight.
     *
     * @pre The departure time is in the format specified in utils.Saps in TIME_FORMAT.
     * @return the time for the departure of the first connecting leg of this flight.
     */
    public MyTime getDepartureTime(){
        return new MyTime(this.connectingLegs.get(0).departure().time);
    }
    /**
     * Get the gmt time of the arrival or departure of the last/first connecting leg of this flight.
     *
     * @pre The arrival and departure times are in the format specified in utils.Saps in TIME_FORMAT.
     * @param isDeparture True if the departure time should be returned, false if the arrival time should be returned.
     * @return the time of the arrival or departure of the last/first connecting leg of this flight based on isDeparture.
     */
    public MyTime getFlightTime(boolean isDeparture){
        return isDeparture ? this.getDepartureTime() : this.getArrivalTime();
    }

    /**
     * Make a copy of the flight that references the same connecting leg objects in a new list.
     *
     * @post A new Flight object is instantiated with a new list containing references to the same ConnectingLeg objects as the previous object.
     * @return The new Flight referencing the same connecting leg objects.
     */
    public Flight shallowCopy(){
        ConnectingLegs copy = new ConnectingLegs();
        copy.addAll(this.connectingLegs);
        return new Flight(copy);
    }

    /**
     * Create a string representation of this flight's information (Flight: \t departureCode arrivalCode depTime arrTime\n ...).
     *
     * @return a string representation of this flight's information.
     */
    public String toString(){
        StringBuilder toReturn = new StringBuilder("Flight: ");
        for(ConnectingLeg leg: this.connectingLegs){
            toReturn.append("\t").append(leg.departure().code).append(" ").append(leg.arrival().code).append(" ").append(leg.departure().time).append(" ").append(leg.arrival().time).append("\n");
        }
        return toReturn.toString();
    }

    /**
     * Get an iterator for the legs that make up this flight.
     *
     * @return an iterator for the legs that make up this flight.
     */
    public Iterator<ConnectingLeg> getLegs(){
        return this.connectingLegs.iterator();
    }
}
