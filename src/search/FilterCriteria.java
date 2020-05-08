package search;

import leg.SeatTypeEnum;

import java.time.LocalTime;

/**
 * Encapsulate criteria used for filtering flights by time windows and seat availability.
 *
 * @author Jackson Powell
 * @since 2020-05-07
 */
public class FilterCriteria {
    /**
     * seatType is the type of seat to check for availability on each connecting leg.
     */
    SeatTypeEnum seatType;
    /**
     * startDep is the start of the departure time window to filter flights. Only looks at time of day, not time and day.
     */
    LocalTime startDep;
    /**
     * endDep is the end of the departure time window to filter flights. Only looks at time of day, not time and day.
     */
    LocalTime endDep;
    /**
     * startArr is the start of the arrival time window to filter flights. Only looks at time of day, not time and day.
     */
    LocalTime startArr;
    /**
     * endArr is the end of the arrival time window to filter flights. Only looks at time of day, not time and day.
     */
    LocalTime endArr;

    /**
     * Create a FilterCriteria object to encapsulate criteria used to filter flights.
     *
     * @post Instantiate a new FilterCriteria object filled with the given criteria. If invalid criteria is used, then a filter using this object will not match the criteria and so not return any matching flights.
     * @param seatType The seat type to check availability of each leg in each flight.
     * @param startDep The start of the departure time window bound.
     * @param endDep The end of the departure time window bound.
     * @param startArr The start of the arrival time window bound.
     * @param endArr THe end of the arrival time window bound.
     */
    public FilterCriteria (SeatTypeEnum seatType, LocalTime startDep, LocalTime endDep, LocalTime startArr, LocalTime endArr){
        this.seatType = seatType;
        this.endArr =endArr;
        this.endDep = endDep;
        this.startArr = startArr;
        this.startDep = startDep;
    }

    /**
     * Get the seatType used in this filter criteria.
     *
     * @return the seatType used in this filter criteria.
     */
    public SeatTypeEnum getSeatType() {
        return seatType;
    }
}
