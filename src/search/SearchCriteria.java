package search;

import leg.SeatTypeEnum;

import java.security.InvalidParameterException;
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
     * @throws InvalidParameterException If any parameter is null or if the two airport codes are the same.
     * @post A SearchCriteria object is instantiated which can be passed to CreatePossibleFlights as the criteria for the search.
     * @param departureAirportCode The 3-letter code for the departure airport criterion.
     * @param arrivalAirportCode The 3-letter code for the arrival airport criterion.
     * @param flightDate The date criterion for a flight either departing/arriving.
     * @param isSelectedDateForDeparture True if the date is for departing, false if arriving.
     */
    public SearchCriteria(String departureAirportCode, String arrivalAirportCode, LocalDate flightDate, boolean isSelectedDateForDeparture) throws InvalidParameterException {
        if(departureAirportCode.equalsIgnoreCase(arrivalAirportCode)){
            throw new InvalidParameterException("Departure airport cannot be the same as arrival airport");
        }
        if(departureAirportCode.length() != 3 || arrivalAirportCode.length() != 3){
            throw new InvalidParameterException("Airport codes must be 3 characters long");
        }
        if(flightDate == null){
            throw new InvalidParameterException("All criteria must be provided for search");
        }
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.flightDate = flightDate;
        this.isSelectedDateForDeparture = isSelectedDateForDeparture;
    }
}
