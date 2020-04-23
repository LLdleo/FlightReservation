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
    SearchCriteria criteria;
    Map<String, ConnectingLegs> memo;
    public CreatePossibleFlights(SearchCriteria criteria){
        this.criteria = criteria;
        this.memo = new HashMap<>();
    }
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
    private String hash(String airportCode, LocalDate flightDate, boolean isDeparture, boolean isLastLeg){
        return airportCode + getStringDate(flightDate) + (isDeparture ? "t" : "f") + (isLastLeg ? "t" : "f");
    }
    private String getStringDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT));
    }
    private ConnectingLegs queryServer(String airportCode, LocalDate flightDate, boolean isDeparture, boolean isLastLeg){
        String key = hash(airportCode, flightDate, isDeparture, isLastLeg);
        if(this.memo.containsKey(key)){return this.memo.get(key);}
        ConnectingLegs result = ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,(isDeparture ? "departing" : "arriving"), airportCode,getStringDate(flightDate));
        List<ConnectingLeg> intermediateList;
        if(isDeparture){
            intermediateList = result.stream().filter(leg -> !leg.arrival().getCode().equalsIgnoreCase(criteria.getDepartureAirportCode())).collect(Collectors.toList());
            result.clear();
            result.addAll(intermediateList);
        }
        else{
            intermediateList = result.stream().filter(leg -> !leg.departure().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())).collect(Collectors.toList());
            result.clear();
            result.addAll(intermediateList);
        }
        if(isLastLeg){
            if(isDeparture){
                intermediateList = result.stream().filter(leg -> leg.arrival().getCode().equalsIgnoreCase(criteria.getArrivalAirportCode())).collect(Collectors.toList());
                result.clear();
                result.addAll(intermediateList);
            }
            else{
                intermediateList = result.stream().filter(leg -> leg.departure().getCode().equalsIgnoreCase(criteria.getDepartureAirportCode())).collect(Collectors.toList());
                result.clear();
                result.addAll(intermediateList);
            }
        }
        this.memo.put(key,result);
        return result;
    }
}
