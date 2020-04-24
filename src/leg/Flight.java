package leg;

import airplane.Airplanes;
import dao.ServerInterface;
import time.MyTime;
import utils.Saps;

import java.util.stream.Collectors;

import static leg.SeatTypeEnum.FIRSTCLASS;

/**
 * @author Jackson Powell
 * @since 2020-04-21
 * Responsibilities: Store sets of connecting legs as a flight
 */
public class Flight {
    /**
     * connectingLegs is the list of connecting legs that make up the flight. Length of list must be in range [1,3].
     */
    private ConnectingLegs connectingLegs;

    /**
     * Constructor for Flight for building it up from one flight leg.
     *
     * @param firstLeg The first leg of the flight.
     */
    public Flight(ConnectingLeg firstLeg){
        connectingLegs = new ConnectingLegs();
        connectingLegs.add(firstLeg);
    }

    /**
     * Constructor for Flight if list of legs already created
     *
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
     * @post If this method returns true, then this flight has added newleg to the end or start of its connecting legs based on addAtEnd. If false, this flight's connecting legs remain unchanged.
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
        return this.connectingLegs.stream().map(leg -> getLegPrice(leg,seatType)).collect(Collectors.summingDouble(Double::valueOf));
    }

    /**
     * Get price of a type of seat on a connecting leg. Defaults to coach if illegal seatType given.
     *
     * @param leg The connecting leg to get the price of.
     * @param seatType The type of seat to check the price for.
     * @return The price of a seat of the seat type on the given leg.
     */
    private double getLegPrice(ConnectingLeg leg, SeatTypeEnum seatType){
        if (seatType == FIRSTCLASS){return leg.seating().getFirstClassPrice();}
        return leg.seating().getCoachPrice();
    }
    /**
     * Checks if there are still seats for this flight available.
     *
     * @pre The database is locked to guarantee consistent execution.
     * @inv The database state is constant throughout execution.
     * @post The database is still in th same state as when called.
     * @return True if there are seats available on the trip's seat type for every leg of the trip's outgoing flight
     */
    public boolean allSeatsStillAvailable(SeatTypeEnum seatType){
        int totalLegsCount = this.connectingLegs.size();
        // TODO: Get airplanes to check seat availability by: Airplanes airplanes = ServerInterface.INSTANCE.getAirplanes(Saps.TEAMNAME);
        int availableLegsCount = totalLegsCount;
        return totalLegsCount == availableLegsCount;
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
     * Get the time for the arrival of the last connecting leg of this flight.
     *
     * @return the time for the arrival of the last connecting leg of this flight.
     */
    public MyTime getArrivalTime(){
        return new MyTime(this.connectingLegs.get(this.connectingLegs.size()-1).arrival().time);
    }
    /**
     * Get the time for the departure of the first connecting leg of this flight.
     *
     * @return the time for the departure of the first connecting leg of this flight.
     */
    public MyTime getDepartureTime(){
        return new MyTime(this.connectingLegs.get(0).departure().time);
    }
    /**
     * Get the time of the arrival or departure of the last/first connecting leg of this flight.
     *
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
     * Create a string representation of this flight's information.
     *
     * @return a string representation of this flight's information.
     */
    public String toString(){
        String toReturn = "Flight: ";
        for(ConnectingLeg leg: this.connectingLegs){
            toReturn += "\t" + leg.departure().code + " " + leg.arrival().code + " " + leg.departure().time + " " + leg.arrival().time + "\n";
        }
        return toReturn;
    }
}