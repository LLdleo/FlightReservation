package dao;

//import org.json.JSONException;
//import org.json.JSONObject;
import airplane.Airplane;
import airplane.Airplanes;
import com.fasterxml.jackson.databind.ObjectMapper;
import flight.Flights;
import leg.ConnectingLeg;
import leg.SeatTypeEnum;
import leg.Trip;
import org.junit.Test;
import search.CreatePossibleFlights;
import search.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;

import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ServerInterfaceTest {
    public static void main(String[] args){
//        JSONObject jsonObject = ServerInterface.INSTANCE.getTimezone("PoLYmer", "6605a2073bfb4fdb9efdf98ab5c29e9a", "42.272099", "-71.812028");
//        System.out.println(jsonObject);
        // Search test
        final long start = System.currentTimeMillis();
        LocalDate depDate = LocalDate.of(2020,5,10);
        List<Flight> flights = new SearchOneWayTripFlights(new SearchCriteria("BOS","CLE",depDate, true)).search();
//        for(search.Flight flight:flights){
//            System.out.println(flight);
//        }
        ObjectMapper mapper = new ObjectMapper();
        try{
//            List<String> jsonStrings = new ArrayList<>();
//            for(Flight flight: flights){
//                jsonStrings.add(flight.toJSON());
//            }
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flights));
            //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flights.get(0).getConnectingLegList().get(0)));
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        final long end = System.currentTimeMillis();
        System.out.println((end-start)/1000.0);
        System.out.println(flights.size());

        // Reserve test
        //<Flight Airplane="A320" FlightTime="167" Number="3774"><Departure><Code>BOS</Code><Time>2020 May 10 00:12 GMT</Time>
        // </Departure><Arrival><Code>MSP</Code><Time>2020 May 10 02:59 GMT</Time></Arrival><Seating><FirstClass Price="$1,053.50">8</FirstClass>
        // <Coach Price="$101.95">100</Coach></Seating></Flight>
        /*try {
            ServerInterface.INSTANCE.getAirports("PoLYmer");
            new Trip(new Flight(new ConnectingLeg("A320", "167", "3774", "BOS", "2020 May 10 00:12 GMT", "MSP", "2020 May 10 02:59 GMT",
                    "$1,053.50", "8", "$101.95", "100")), SeatTypeEnum.COACH).reserveSeats();
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }*/


    }

}