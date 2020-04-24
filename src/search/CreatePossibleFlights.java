package search;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;
import leg.Flight;
import flight.Flights;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import time.MyTime;
import time.TimezoneInterface;
import utils.Saps;

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
        Airport airport = airports.getAirportByCode(criteria.getSearchAirportCode());
        double latitude = airport.latitude();
        double longitude = airport.longitude();
        double airportOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(latitude,longitude);
        int dateOffset = -(int) (airportOffset / Math.abs(airportOffset));
        LocalDate secondDate = criteria.getFlightDate().plusDays(dateOffset);
        ConnectingLegs firstLegs = queryServer(criteria.getSearchAirportCode(),criteria.getFlightDate(),criteria.isSelectedDateForDeparture(), false);
        firstLegs.addAll(queryServer(criteria.getSearchAirportCode(),secondDate,criteria.isSelectedDateForDeparture(), false));
        List<ConnectingLeg> localFirstLegs = firstLegs.stream().filter(leg ->
                (new MyTime(criteria.isSelectedDateForDeparture() ? leg.departure().getTime() : leg.arrival().getTime()
                        ,latitude,longitude)).getLocalTime().toLocalDate().isEqual(criteria.getFlightDate())).collect(Collectors.toList());

        Flights firstLegFlights = new Flights();
        for(ConnectingLeg leg : localFirstLegs){
            if(legMatchesCriteria(leg,criteria.isSelectedDateForDeparture())){
                availableFlights.add(new Flight(leg));
            }
            else {
                firstLegFlights.add(new Flight(leg));
            }
        }
        Flights twoLegFlights = getNextLegCombinations(availableFlights,firstLegFlights,criteria.isSelectedDateForDeparture(),false);
        getNextLegCombinations(availableFlights,twoLegFlights,criteria.isSelectedDateForDeparture(),true);
        return availableFlights;
    }

    /**
     * Retrieve the set of Flights that contains one more connecting leg each, but is still not complete.
     *
     * @post availableFlights will be populated by any Flights that match the search criteria and don't need to search further.
     * @param availableFlights The list of complete flights which complete flights can be added to.
     * @param previousCombinations The list of incomplete flights to get one more connecting leg each to append, creating combinations to try and get complete flights.
     * @param isDeparture True if searching based on departure date, false if based on arrival date.
     * @param isLastLeg True if getting the third set of connecting legs, which is the limit for the number of legs per flight, false otherwise. Indicates whether search criteria can be used to constrain connecting leg combinations.
     * @return The set of Flights of combinations of the previousCombinations with one more connecting leg that don't meet the search criteria.
     */
    private Flights getNextLegCombinations(Flights availableFlights, Flights previousCombinations, boolean isDeparture, boolean isLastLeg){
        Flights nextLegFlights = new Flights();
        for(Flight last : previousCombinations){
            ConnectingLegs nextLegs = getRelevantNextLegs(last,isDeparture,isLastLeg);
            for(ConnectingLeg leg : nextLegs){
                Flight moreLegs = last.shallowCopy();
                if(moreLegs.addLeg(leg, isDeparture)){
                    if(legMatchesCriteria(leg,isDeparture)){
                        availableFlights.add(moreLegs);
                    }
                    else{
                        nextLegFlights.add(moreLegs);
                    }
                }
            }
        }
        return nextLegFlights;
    }

    /**
     * Determine if the leg matches the search criteria such that the flight could end/start with this leg.
     *
     * @param legToCheck The leg to check whether it matches the search criteria.
     * @param shouldMatchArrivalCriteria True if checking this leg's airport against the arrival airport criterion, false if checking the departure airports.
     * @return True if the leg can end/start a flight by ending/starting in the required arrival/departure airport.
     */
    private boolean legMatchesCriteria(ConnectingLeg legToCheck, boolean shouldMatchArrivalCriteria){
        if(shouldMatchArrivalCriteria){
            return legToCheck.arrival().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode());
        }
        return legToCheck.departure().getCode().equalsIgnoreCase(criteria.getDepartureAirportCode());
    }

    /**
     * Get the next set of connecting legs to combine with the FLight start.
     *
     * @param start The flight to get connecting legs to add to this flight.
     * @param isDeparture True if searching for flights based on the departure date. False if based on the arrival date.
     * @param isLastLeg True if getting the third set of legs, and thus searching should restrict results to those that meet the search criteria. False otherwise.
     * @return The next set of connecting legs to combine with the flight start that will be constrained if this isLastLeg and will be searched in a direction based on isDeparture.
     */
    private ConnectingLegs getRelevantNextLegs(Flight start, boolean isDeparture, boolean isLastLeg){
        ConnectingLegs nextLegs = new ConnectingLegs();
        // Note: These could probably be combined using general functions like getFlightTime(), but I left that out for
        // now because I thought it might be too unreadable.
        if(isDeparture){
            MyTime arrivalTime = start.getArrivalTime();
            // Avoid unnecessary queries
            double hoursToNextDay = arrivalTime.getTimeToNextDay();
            String arrivalCode = start.getArrivalAirportCode();
            LocalDate arrivalDate = start.getArrivalTime().getGmtTime().toLocalDate();
            if (hoursToNextDay >= Saps.MIN_LAYOVER_TIME_HOURS){
                nextLegs.addAll(queryServer(arrivalCode,arrivalDate,true,isLastLeg));
            }
            if(hoursToNextDay <= Saps.MAX_LAYOVER_TIME_HOURS){
                nextLegs.addAll(queryServer(arrivalCode,arrivalDate.plusDays(1),true,isLastLeg));
            }
        }
        else{
            MyTime departureTime = start.getDepartureTime();
            // Avoid unnecessary queries
            double hoursToLastDay = departureTime.getTimeToLastDay();
            String departureCode = start.getDepartureAirportCode();
            LocalDate departureDate = start.getDepartureTime().getGmtTime().toLocalDate();
            if (hoursToLastDay >= Saps.MIN_LAYOVER_TIME_HOURS){
                nextLegs.addAll(queryServer(departureCode,departureDate,false,isLastLeg));
            }
            if(hoursToLastDay <= Saps.MAX_LAYOVER_TIME_HOURS){
                nextLegs.addAll(queryServer(departureCode,departureDate.plusDays(-1),false,isLastLeg));
            }
        }
        return nextLegs;
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
     * TODO: Maybe move this somewhere else
     * @param date The LocalDate to be converted.
     * @return The string representation of the given date in the string format of the date to query the WPI server with.
     */
    public static String getStringDate(LocalDate date){
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
