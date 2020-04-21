package leg;

import utils.Saps;

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
        if()
        this.connectingLegs = connectingLegs;
    }

    /**
     * Determine if a list of connecting legs would constitute a valid flight (<=3 legs. reasonable layover times, and compatible airports)
     *
     * @param connectingLegsToCheckIfValid The list of connnecting legs to check if they would make a valid flight.
     * @return True if the set of legs would constitute a valid flight, false otherwise.
     */
    public boolean isValid(ConnectingLegs connectingLegsToCheckIfValid){
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
            if()
        }
    }

}
