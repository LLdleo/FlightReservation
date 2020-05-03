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
 * Responsibilities: Store one selected flight as a one-way trip
 */
public class Trip {
    /**
     * outgoingFlight is the first/only flight for a round or one-way trip.
     */
    private Flight outgoingFlight;
    /**
     * seatType is the type of seat that will be used for each leg on the flight. Different seatTypes cannot be used on different legs of the same flight.
     */
    private SeatTypeEnum seatType;

    /**
     * Get the flight for this trip.
     * @return the flight for this trip.
     */
    public Flight getOutgoingFlight() {
        return outgoingFlight;
    }

    /**
     * Get the seatType for the outgoing flight.
     * @return the type of seat being used for the outgoing flight.
     */
    public SeatTypeEnum getSeatType() {
        return seatType;
    }
    /**
     * Constructor for a trip with the given outgoing flight
     *
     * @pre outgoingFlight was already validated as a Flight by flight
     * @param outgoingFlight The the outgoing flight for a trip.
     */
    public Trip(Flight outgoingFlight, SeatTypeEnum seatType){
        this.outgoingFlight = outgoingFlight;
        this.seatType = seatType;
    }

    /**
     * Reserve a seat of the trip's seatType on each connectingLeg of the trip's flight
     *
     * @see Saps For the timeouts for trying to acquire the lock and connect to the server.
     * @throws ServerLockException If the lock for the WPI server cannot be acquired.
     * @throws ServerAccessException If there is an issue connecting to the WPI server when refreshing the connecting leg information.
     * @return True if the reservation was successful, false otherwise
     */
    public boolean reserveSeats() throws ServerLockException, ServerAccessException {
        boolean success = false;
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        do {
            success = ServerInterface.INSTANCE.lock(Saps.TEAMNAME);
            endLockTimer = System.currentTimeMillis();
        }while(!success && (endLockTimer - startLockTimer)/1000 < Saps.LOCK_TIMEOUT_SECONDS);
        if(!success){throw new ServerLockException("System timed out after " + (endLockTimer - startLockTimer)/1000 + " seconds");}

        if(!this.outgoingFlight.allSeatsStillAvailable(this.seatType)){
            ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
            return false;
        }
        success = ServerInterface.INSTANCE.reserve(Saps.TEAMNAME,this);
        ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
        return success;
    }

}