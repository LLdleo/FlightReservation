package dao;

//import org.json.JSONException;
//import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import leg.ConnectingLeg;
import leg.SeatTypeEnum;
import reservation.Trip;
import leg.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;
import utils.Saps;

import java.time.LocalDate;
import java.util.List;

/**
 * Class used for manual testing entry point.
 *
 * @author Jackson Powell, Lidian Lin
 * @deprecated This class is for manual testing during development.
 */
public class ServerInterfaceTest {
    public static void main(String[] args){
        // Search test
        try{
            final long start = System.currentTimeMillis();
        LocalDate depDate = LocalDate.of(2020,5,10);
        List<search.Flight> flights = new SearchOneWayTripFlights(new SearchCriteria("CLE","BOS",depDate, true)).search();
        ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flights));
            final long end = System.currentTimeMillis();
            System.out.println((end-start)/1000.0);
            System.out.println(flights.size());
        }
        catch(Exception e){
            System.out.println(e.toString());
        }


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


    }

}