package leg;

import dao.ServerInterface;
import utils.Saps;

/**
 * @author Jackson Powell
 * @since 2020-04-21
 * Responsibilities: Store one selected flight as a one-way trip
 */
public class Trip {
    private Flight outgoingFlight;
    private SeatTypeEnum seatType;

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
     * @return True if the reservation was successful, false otherwise
     */
    public boolean reserveSeats(){
        ServerInterface.INSTANCE.lock(Saps.TEAMNAME); // TODO: add handling for when lock can't be acquired.
        if(!this.outgoingFlight.allSeatsStillAvailable(this.seatType)){
            ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
            return false;
        }
        ServerInterface.INSTANCE.reserve(this.outgoingFlight, this.seatType);
        ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
        return true;
    }

}
