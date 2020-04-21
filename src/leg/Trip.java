package leg;

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

    }
    protected boolean isValid(){

    }
}
