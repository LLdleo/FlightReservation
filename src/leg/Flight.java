package leg;

import time.MyTime;
import utils.Saps;

import java.time.Duration;

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
     * Calculate the travel time of this flight in hours
     * Calculate the travel time of this flight in hours as the timespan from the first leg's scheduled departure time to the last leg's scheduled arrival time.
     *
     * @return The travel time of the flight in hours from the first departure to the last arrival
     */
    public double calculateTravelTime(){
        MyTime firstDeparture = new MyTime(this.connectingLegs.get(0).departure().time);
        MyTime lastArrival = new MyTime(this.connectingLegs.get(this.connectingLegs.size()-1).arrival().time);
        return firstDeparture.timespan(lastArrival);
    }

}
