package search;

import airport.Airports;
import dao.ServerInterface;
import leg.Flight;
import flight.Flights;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import time.MyTime;
import utils.Saps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jackson Powell
 * @since 2020-04-23
 * Responsibilities: Calculate possible connecting leg combinations based on search criteria
 */
public class CreatePossibleFlights {
    /**
     * criteria is the search criteria to retrieve flights based on whether they match this criteria
     */
    private SearchCriteria criteria;
    /**
     * Hashmap for server queries about connecting legs to prevent duplicate calls to WPI server.
     * Assumes number of seats won't change drastically while searching.
     */
    private Map<String, ConnectingLegs> memo;

    /**
     * Constructor for searching on some criteria.
     *
     * Constructor to create an object that can search for flights based on some criteria.
     * Note: this object should not be re-used for the same searches over a long period of time as the cache won't be refreshed.
     * TODO: maybe implement better caching technology that will eliminate stale entries.
     * @post CreatePossibleFlights object will be instantiated and ready to search for matching flights.
     * @param criteria Search criteria to search flights based on.
     */
    public CreatePossibleFlights(SearchCriteria criteria){
        this.criteria = criteria;
        this.memo = new HashMap<>();
    }

    /**
     * Calculate all possible flight combinations matching search criteria
     *
     * Calculate all possible flight combinations matching search criteria where layover times are between a half and 4 hours,
     * the initial departure airport is not returned to, and flights have at most 3 connecting legs.
     *
     * @return all possible flight combinations matching search criteria.
     */
    public Flights createPossibleConnectingLegCombinations(){
        Flights availableFlights = new Flights();
        Airports airports = ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME);

        // Assume departure TODO: add arrival and account for local date
        ConnectingLegs firstLegs = queryServer(criteria.getDepartureAirportCode(),criteria.getFlightDate(),criteria.isSelectedDateForDeparture(), false);
        Flights firstLegFlights = new Flights();
        for(ConnectingLeg leg : firstLegs){
            if(leg.arrival().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())){
                availableFlights.add(new Flight(leg));
            }
            else {
                firstLegFlights.add(new Flight(leg));
            }
        } // TODO: Extract these two into helper to not duplicate
        // Get second legs
        Flights twoLegFlights = new Flights();
        for(Flight first : firstLegFlights){
            MyTime arrivalTime = first.getArrivalTime();
            // Avoid unnecessary queries
            double hoursToNextDay = arrivalTime.getTimeToNextDay();
            ConnectingLegs secondLegs = new ConnectingLegs();
            String arrivalCode = first.getArrivalAirportCode();
            LocalDate arrivalDate = first.getArrivalTime().getGmtTime().toLocalDate();
            if (hoursToNextDay >= Saps.MIN_LAYOVER_TIME_HOURS){
                secondLegs.addAll(queryServer(arrivalCode,arrivalDate,true,false));
            }
            if(hoursToNextDay <= Saps.MAX_LAYOVER_TIME_HOURS){
                secondLegs.addAll(queryServer(arrivalCode,arrivalDate.plusDays(1),true,false));
            }
            for(ConnectingLeg leg : secondLegs){
                Flight twoLegs = first.shallowCopy();
                if(twoLegs.addLeg(leg)){
                    if(leg.arrival().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())){
                        availableFlights.add(twoLegs);
                    }
                    else{
                        twoLegFlights.add(twoLegs);
                    }
                }
            }
        }
        firstLegFlights.clear();
        for(Flight second : twoLegFlights){
            MyTime arrivalTime = second.getArrivalTime();
            // Avoid unnecessary queries
            double hoursToNextDay = arrivalTime.getTimeToNextDay();
            ConnectingLegs secondLegs = new ConnectingLegs();
            String arrivalCode = second.getArrivalAirportCode();
            LocalDate arrivalDate = second.getArrivalTime().getGmtTime().toLocalDate();
            if (hoursToNextDay >= Saps.MIN_LAYOVER_TIME_HOURS){
                secondLegs.addAll(queryServer(arrivalCode,arrivalDate,true,true));
            }
            if(hoursToNextDay <= Saps.MAX_LAYOVER_TIME_HOURS){
                secondLegs.addAll(queryServer(arrivalCode,arrivalDate.plusDays(1),true,true));
            }
            for(ConnectingLeg leg : secondLegs){
                Flight twoLegs = second.shallowCopy();
                if(twoLegs.addLeg(leg)){
                    availableFlights.add(twoLegs);
                }
            }
        }

        return availableFlights;
    }

    /**
     * Create the hash string of the server query for caching or retrieving cached results.
     *
     * @param airportCode The 3-letter string of the code for the airport being queried for departing or arriving connecting legs.
     * @param flightDate The GMT date of the flights either departing or arriving.
     * @param isDeparture True if the query is for departing connecting legs, false if the query is for arriving connecting legs.
     * @param isLastLeg True if the query results have been filtered to only include flights ending at an arrival airport or starting at a departure airport depending on criteria.
     * @return The hash string of the server query for caching or retrieving cached results.
     */
    private String hash(String airportCode, LocalDate flightDate, boolean isDeparture, boolean isLastLeg){
        return airportCode + getStringDate(flightDate) + (isDeparture ? "t" : "f") + (isLastLeg ? "t" : "f");
    }

    /**
     * Convert a date to the string format needed for querying the server.
     *
     * @param date The LocalDate to be converted.
     * @return The string representation of the given date in the string format of the date to query the WPI server with.
     */
    private String getStringDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT));
    }

    /**
     * Return the results for a query to the server, may be from cache
     *
     * @param airportCode The 3-letter string of the airport to query about connecting legs departing from or arriving at.
     * @param flightDate The GMT date to query about legs departing or arriving on.
     * @param isDeparture True if the query seeks the legs departing from an airport on a certain date, false if query is seeking legs arriving at an airport on a certain date.
     * @param isLastLeg True if the query wants the results filtered to only include legs arriving at the arrival airport if the departure date was selected or
     *                  only include legs departing from the departure airport if the arrival date was selected in the criteria.
     * @return The results for the connecting legs either departing or arriving (based on isDeparture) from/at an airport with a given airport code on a given flightDate.
     */
    private ConnectingLegs queryServer(String airportCode, LocalDate flightDate, boolean isDeparture, boolean isLastLeg){
        String key = hash(airportCode, flightDate, isDeparture, isLastLeg);
        if(this.memo.containsKey(key)){return this.memo.get(key);}
        ConnectingLegs result = ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,(isDeparture ? "departing" : "arriving"), airportCode,getStringDate(flightDate));
        List<ConnectingLeg> intermediateList;
        if(isDeparture){
            intermediateList = result.stream().filter(leg -> !leg.arrival().getCode().equalsIgnoreCase(criteria.getDepartureAirportCode())).collect(Collectors.toList());
        }
        else{
            intermediateList = result.stream().filter(leg -> !leg.departure().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())).collect(Collectors.toList());
        }
        result.clear();
        result.addAll(intermediateList);
        if(isLastLeg){
            if(isDeparture){
                intermediateList = result.stream().filter(leg -> leg.arrival().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())).collect(Collectors.toList());
            }
            else{
                intermediateList = result.stream().filter(leg -> leg.departure().getCode().equalsIgnoreCase(criteria.getDepartureAirportCode())).collect(Collectors.toList());
            }
            result.clear();
            result.addAll(intermediateList);
        }
        this.memo.put(key,result);
        return result;
    }
}
