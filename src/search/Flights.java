package search;

import leg.SeatTypeEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Flights extends ArrayList<search.Flight> {
    private static final long serialVersionUID = 1L;
    public Flights filter(FilterCriteria filterCriteria){
        List<Flight> filtered = this.stream().filter(flight -> flight.allSeatsAvailable(filterCriteria.seatType) &&
                flight.inRange(filterCriteria.startDep,filterCriteria.endDep,true) &&
                flight.inRange(filterCriteria.startArr,filterCriteria.endArr,false)).collect(Collectors.toList());
        Flights newFlights = new Flights();
        newFlights.addAll(filtered);
        return newFlights;
    }
    public Flights sort(SortTypeEnum sortType, boolean isAscending){
        Comparator<search.Flight> compare = new Comparator<Flight>() {
            @Override
            public int compare(Flight o1, Flight o2) {
                int toReturn = 0;
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
                        toReturn = (int) (o1.getCoachPrice() - o2.getCoachPrice());
                        break;
                    case FIRST_CLASS_PRICE:
                        toReturn = (int) (o1.getFirstClassPrice() - o2.getFirstClassPrice());
                        break;
                    case TRAVEL_TIME:
                        toReturn = (int) (o1.getTravelTime() - o2.getTravelTime());
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
            }
        };
        if(!isAscending){
            this.sort(Collections.reverseOrder(compare));
        }
        else{
            this.sort(compare);
        }
        return this;
    }
    public String toString(){
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Flights:\n");
        int num = 0;
        for(Flight flight : this){
            toReturn.append("\nFlight ").append(num).append("\n").
                    append("Coach price: ").append(flight.getCoachPrice()).append("\t").
                    append("First Class Price: ").append(flight.getFirstClassPrice()).append("\n").
                    append("Travel time: ").append(flight.getTravelTime()).append("\t").
                    append("Number of legs: ").append(flight.getNumLegs()).append("\nLegs: \n");
            for(ConnectingLeg leg: flight.getConnectingLegList()){
                toReturn.append(leg.toString());
            }
            num++;
        }
        return toReturn.toString();
    }
}
