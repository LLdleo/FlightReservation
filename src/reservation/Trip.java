package reservation;

import dao.ServerAccessException;
import dao.ServerInterface;
import leg.Flight;
import leg.SeatTypeEnum;
import reservation.ServerLockException;
import utils.Saps;

/**
 * @author Jackson Powell
 * @since 2020-04-21
 * Responsibilities: Store one or two selected flights as a one-way or round trip.
 * Significant associations: WPI server for the needed information to reserve a seat and how many flights can be reserved at once by a customer. leg.Flight for the objects contained and for checking availability.
 */
public class Trip {
    /**
     * outgoingFlight is the first/only flight for a round or one-way trip.
     */
    private Flight outgoingFlight;
    /**
     * seatType is the type of seat that will be used for each leg on the outgoing flight. Different seatTypes cannot be used on different legs of the same flight.
     */
    private SeatTypeEnum seatType;
    /**
     * returnFlight is the second flight for a round trip. Will be null for one-way trips
     */
    private Flight returnFlight;
    /**
     * returnSeatType is the type of seat that will be used for each leg on the return flight.  Different seatTypes cannot be used on different legs of the same flight.
     */
    private SeatTypeEnum returnSeatType;
    /**
     * Get the flight for this trip.
     *
     * @return the flight for this trip.
     */
    public Flight getOutgoingFlight() {
        return outgoingFlight;
    }

    /**
     * Get the seatType for the outgoing flight.
     *
     * @return the type of seat being used for the outgoing flight.
     */
    public SeatTypeEnum getSeatType() {
        return seatType;
    }

    /**
     * Get the return flight of a round trip.
     *
     * @return the return flight of a round trip.
     */
    public Flight getReturnFlight() {
        return returnFlight;
    }

    /**
     * Get the seatType for the return flight.
     *
     * @return the seatType for the return flight
     */
    public SeatTypeEnum getReturnSeatType() {
        return returnSeatType;
    }

    /**
     * Constructor for a trip with the given outgoing flight
     *
     * @pre outgoingFlight was already validated as a Flight by flight
     * @post A Trip object is instantiated representing a one-way trip that can be reserved for a certain seatType on each leg.
     * @param outgoingFlight The the outgoing flight for a trip.
     */
    public Trip(Flight outgoingFlight, SeatTypeEnum seatType){
        this.outgoingFlight = outgoingFlight;
        this.seatType = seatType;
    }
    /**
     * Constructor for a round trip with the given outgoing and return flights
     *
     * @pre outgoingFlight and returnFlight were already validated as flights
     * @post A Trip object is instantiated representing a round trip that can be reserved for a certain outSeatType on the outgoingFlight and a certain returnSeatType on the returnFlight.
     * @param outgoingFlight The the outgoing flight for a trip.
     * @param returnFlight The return flight for a round trip.
     * @param outSeatType The seat type used for each leg on the outgoing flight.
     * @param returnSeatType The seat type used for each leg on the return flight.
     */
    public Trip(Flight outgoingFlight,Flight returnFlight, SeatTypeEnum outSeatType, SeatTypeEnum returnSeatType){
        this.outgoingFlight = outgoingFlight;
        this.seatType = outSeatType;
        this.returnFlight = returnFlight;
        this.returnSeatType = returnSeatType;
    }

    /**
     * Reserve a seat of the trip's seatType on each connectingLeg of the trip's flight
     *
     * @pre The outgoing flight is at least set and if there is a return flight then its departure occurs after the outgoing flight's arrival.
     * @post If true, then a seat on each set flight will be reserved on the database, incrementing the number of seats reserved.
     * @see Saps For the timeouts for trying to acquire the lock and connect to the server.
     * @throws ServerLockException If the lock for the WPI server cannot be acquired.
     * @throws ServerAccessException If there is an issue connecting to the WPI server when refreshing the connecting leg information.
     * @return True if the reservation was successful, false otherwise
     */
    public boolean reserveSeats() throws ServerLockException, ServerAccessException {
        boolean success;
        if(!this.outgoingFlight.allSeatsStillAvailable(this.seatType)){
            ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
            return false;
        }
        if(this.returnFlight != null && this.returnFlight.allSeatsStillAvailable(this.returnSeatType)){
            ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
            return false;
        }
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        do {
            success = ServerInterface.INSTANCE.lock(Saps.TEAMNAME);
            endLockTimer = System.currentTimeMillis();
        }while(!success && (endLockTimer - startLockTimer)/1000 < Saps.LOCK_TIMEOUT_SECONDS);

        if(!success){throw new ServerLockException(Saps.LOCK_EXCEPTION_MESSAGE);}
        success = ServerInterface.INSTANCE.reserve(Saps.TEAMNAME,this);
        ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
        return success;
    }

}
