package search;

import leg.SeatTypeEnum;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Jackson Powell
 * @since 2020-04-23
 * Responsibilities: Encapsulate criteria for search for one flight and validate search criteria.
 */
public class SearchCriteria {
    /**
     * departureAirportCode is the 3-letter code for the airport that the flights should depart from for this search.
     */
    private String departureAirportCode;
    /**
     * arrivalAirportCode is the 3-letter code for the airport that the flights should arrive at for this search.
     */
    private String arrivalAirportCode;
    /**
     * flightDate is the date that the flights should depart/arrive on for this search.
     */
    private LocalDate flightDate;
    /**
     * isSelectedDateForDeparture is true if the flightDate is when flights depart, false if flightDate is when flights arrive.
     */
    private boolean isSelectedDateForDeparture;
    /**
     * seatType is the type of seat that the flights should be filtered by for availability initially
     */
    private SeatTypeEnum seatType;

    /**
     * Return the departure airport's code.
     * @return the departure airport's code.
     */
    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    /**
     * Get the arrival airport's code.
     * @return the arrival airport's code.
     */
    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    /**
     * Get the date criterion for the flight departing/arriving on.
     * @return The date criterion for the flight departing/arriving on.
     */
    public LocalDate getFlightDate() {
        return flightDate;
    }

    /**
     * Get whether the criterion is based on the flights arrival or departure.
     * @return True if the date is for the flights departure, false if arrival.
     */
    public boolean isSelectedDateForDeparture() {
        return isSelectedDateForDeparture;
    }

    /**
     * Get the type of seat that the flights should be filtered by for availability.
     * @return The type of seat that the flights should be filtered by for availability.
     */
    public SeatTypeEnum getSeatType() {
        return seatType;
    }

    /**
     * Return the airport code of the airport that the search will be started on.
     *
     * @return the airport code of the airport that the search will be started on.
     */
    public String getSearchAirportCode(){
        return isSelectedDateForDeparture ? departureAirportCode : arrivalAirportCode;
    }
    /**
     * Constructor for search criteria to encapsulate different restrictions on returned flights.
     *
     * @post A SearchCriteria object is instantiated which can be passed to CreatePossibleFlights as the criteria for the search.
     * @param departureAirportCode The 3-letter code for the departure airport criterion.
     * @param arrivalAirportCode The 3-letter code for the arrival airport criterion.
     * @param flightDate The date criterion for a flight either departing/arriving.
     * @param isSelectedDateForDeparture True if the date is for departing, false if arriving.
     * @param seatType The type of seat to filter the flights for availability.
     */
    public SearchCriteria(String departureAirportCode, String arrivalAirportCode, LocalDate flightDate, boolean isSelectedDateForDeparture, SeatTypeEnum seatType){
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.flightDate = flightDate;
        this.isSelectedDateForDeparture = isSelectedDateForDeparture;
        this.seatType = seatType;
    }
}
