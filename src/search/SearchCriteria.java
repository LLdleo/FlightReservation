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
    private String departureAirportCode;
    private String arrivalAirportCode;
    private LocalDate flightDate;

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public boolean isSelectedDateForDeparture() {
        return isSelectedDateForDeparture;
    }

    public SeatTypeEnum getSeatType() {
        return seatType;
    }

    private boolean isSelectedDateForDeparture;
    private SeatTypeEnum seatType;

    public SearchCriteria(String departureAirportCode, String arrivalAirportCode, LocalDate flightDate, boolean isSelectedDateForDeparture, SeatTypeEnum seatType){
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.flightDate = flightDate;
        this.isSelectedDateForDeparture = isSelectedDateForDeparture;
        this.seatType = seatType;
    }
}
