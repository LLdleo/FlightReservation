package search;

import leg.SeatTypeEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggregate a list of search.Flight and provide sorting and filtering functionality for that list.
 * Significant associations: search.Flight for the objects being aggregated and statistics used, FilterCriteria for the criteria
 * used to filter flights, and SortTypeEnum for the different ways to sort Flights in ascending or descending.
 *
 * @author Jackson Powell
 * @since 2020-05-07
 */
public class Flights extends ArrayList<search.Flight> {
    private static final long serialVersionUID = 1L;

    /**
     * Filter this list of flights based on given filter criteria.
     *
     * @pre There are flights in this list to filter.
     * @inv The list of flights in this object are not changed.
     * @post A new Flights object is instantiated that contains the flights from this object that meets the FilterCriteria.
     * @param filterCriteria The criteria to filter this list of flights by.
     * @return The filtered list of flights.
     */
    public Flights filter(FilterCriteria filterCriteria){
        List<Flight> filtered = this.stream().filter(flight -> flight.allSeatsAvailable(filterCriteria.seatType) &&
                flight.inRange(filterCriteria.startDep,filterCriteria.endDep,true) &&
                flight.inRange(filterCriteria.startArr,filterCriteria.endArr,false)).collect(Collectors.toList());
        Flights newFlights = new Flights();
        newFlights.addAll(filtered);
        return newFlights;
    }

    /**
     * Sort this list of flights in the given order using the given flight statistic.
     *
     * @pre There are flights in this list to sort.
     * @inv The number of flights in this list stays constant.
     * @post This list is sorted in either ascending or descending order based on the given sortType.
     * @param sortType The statistic to sort the list by.
     * @param isAscending True if sorting the list in ascending order of the sortType, false if descending.
     * @return THe sorted list of flights.
     */
    public Flights sort(SortTypeEnum sortType, boolean isAscending){
        Comparator<search.Flight> compare = (o1, o2) -> {
            int toReturn = 0;
            boolean less;
            boolean more;
            switch (sortType){
                case ARR_TIME:
                    boolean early = o1.getArrivalTime().getLocalTime().isBefore(o2.getArrivalTime().getLocalTime());
                    boolean after = o2.getArrivalTime().getLocalTime().isBefore(o1.getArrivalTime().getLocalTime());
                    if(!early && !after){
                        return 0;
                    }
                    else {
                        toReturn = early ? -1 : 1;
                    }
                    break;
                case COACH_PRICE:
                    less = o1.getCoachPrice() < o2.getCoachPrice();
                    more = o2.getCoachPrice() < o1.getCoachPrice();
                    if(!less && !more){
                        return 0;
                    }
                    else {
                        toReturn = less ? -1 : 1;
                    }
                    break;
                case FIRST_CLASS_PRICE:
                    less = o1.getFirstClassPrice() < o2.getFirstClassPrice();
                    more = o2.getFirstClassPrice() < o1.getFirstClassPrice();
                    if(!less && !more){
                        return 0;
                    }
                    else {
                        toReturn = less ? -1 : 1;
                    }
                    break;
                case TRAVEL_TIME:
                    less = o1.getTravelTime() < o2.getTravelTime();
                    more = o2.getTravelTime() < o1.getTravelTime();
                    if(!less && !more){
                        return 0;
                    }
                    else {
                        toReturn = less ? -1 : 1;
                    }
                    break;
                case DEP_TIME:
                    boolean early2 = o1.getDepartureTime().getLocalTime().isBefore(o2.getDepartureTime().getLocalTime());
                    boolean after2 = o2.getDepartureTime().getLocalTime().isBefore(o1.getDepartureTime().getLocalTime());
                    if(!early2 && !after2){
                        toReturn = 0;
                    }
                    else{
                        toReturn = early2 ? -1 : 1;
                    }
                    break;
            }
            return toReturn;
        };
        if(!isAscending){
            this.sort(Collections.reverseOrder(compare));
        }
        else{
            this.sort(compare);
        }
        return this;
    }

    /**
     * Get the string representation of the list of flights for the user to view.
     *
     * @return the string representation of the list of flights for the user to view.
     */
    public String toString(){
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Flights:\n");
        int num = 0;
        for(Flight flight : this){
            toReturn.append("\nFlight ").append(num).append("\n").
            append(flight.toString());
            num++;
        }
        return toReturn.toString();
    }
}
