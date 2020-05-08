package search;

/**
 * Enumerate the possible ways to sort a list of flights.
 *
 * @author Jackson Powell
 * @since 2020-05-07
 */
public enum SortTypeEnum {
    /**
     * DEP_TIME is for sorting by the departure time of each flight. Ascending order goes from earliest to latest date and time.
     */
    DEP_TIME,
    /**
     * ARR_TIME is for sorting by the arrival time of each flight. Ascending order goes from earliest to latest date and time.
     */
    ARR_TIME,
    /**
     * Travel_TIME is for sorting by the total travel time of each flight (timespan from flight departure to flight arrival.
     * Ascending order goes from least to most amount of time.
     */
    TRAVEL_TIME,
    /**
     * COACH_PRICE is for sorting by the total price of reserving one coach seat on each leg of a flight.
     */
    COACH_PRICE,
    /**
     * FIRST_CLASS_PRICE is for sorting by the total price of reserving one first class seat on each leg of a flight.
     */
    FIRST_CLASS_PRICE
}
