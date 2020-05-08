package dao;

//import org.json.JSONException;
//import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.SeatTypeEnum;
import reservation.Trip;
import leg.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;
import utils.Saps;

import java.time.LocalDate;
import java.util.List;

/**
 * Class used for manual testing entry point. Sets up server data for one leg to only have one seat remaining.
 *
 * @author Jackson Powell, Lidian Lin
 * @deprecated This class is for manual testing during development.
 */
public class SeatConflictPrep {
    public static void main(String[] args){
        // Search test
//        try{
//            final long start = System.currentTimeMillis();
//        LocalDate depDate = LocalDate.of(2020,5,10);
//        List<search.Flight> flights = new SearchOneWayTripFlights(new SearchCriteria("CLE","BOS",depDate, true)).search();
//        ObjectMapper mapper = new ObjectMapper();
//            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flights));
//            final long end = System.currentTimeMillis();
//            System.out.println((end-start)/1000.0);
//            System.out.println(flights.size());
//        }
//        catch(Exception e){
//            System.out.println(e.toString());
//        }


        // Reserve test
        //<Flight Airplane="A320" FlightTime="167" Number="3774"><Departure><Code>BOS</Code><Time>2020 May 10 00:12 GMT</Time>
        // </Departure><Arrival><Code>MSP</Code><Time>2020 May 10 02:59 GMT</Time></Arrival><Seating><FirstClass Price="$1,053.50">8</FirstClass>
        // <Coach Price="$101.95">100</Coach></Seating></Flight>
//        try {
//            ServerInterface.INSTANCE.getAirports("PoLYmer");
//            ConnectingLeg leg = new ConnectingLeg("A320", "167", "3774", "BOS", "2020 May 10 00:12 GMT", "MSP", "2020 May 10 02:59 GMT",
//                    "$1,053.50", "8", "$101.95", "100");
//            System.out.println(new Trip(new Flight(leg), new Flight(leg), SeatTypeEnum.COACH, SeatTypeEnum.FIRSTCLASS).reserveSeats());
//        }
//        catch(Exception e){
//            System.out.println(e.toString());
//            System.out.println(e.getLocalizedMessage());
//            System.out.println(e.getMessage());
//        }
//        try{
//            ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        try{
            ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
            ConnectingLegs legs = new leg.ConnectingLegs();
            legs.add(new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129));
            legs.add(new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 6, "17.62", 108));
            legs.add(new ConnectingLeg("767", "23", "27569", "JFK", "2020 May 11 06:30", "BOS", "2020 May 11 06:53", "16.79", 31, "8.73", 148));
            Flight returnFlight = new Flight(legs);
            Trip toReserve = new Trip(returnFlight,SeatTypeEnum.FIRSTCLASS);
            int reserved = 6;
            int max = 12;
            for(int i = 0; i < max-reserved-1; i++){
                toReserve.reserveSeats();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

}