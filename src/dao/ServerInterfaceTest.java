package dao;

//import org.json.JSONException;
//import org.json.JSONObject;
import airplane.Airplane;
import airplane.Airplanes;
import flight.Flights;
import leg.Flight;
import leg.SeatTypeEnum;
import org.junit.Test;
import search.CreatePossibleFlights;
import search.SearchCriteria;

import javax.swing.*;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ServerInterfaceTest {
    public static void main(String[] args){
//        JSONObject jsonObject = ServerInterface.INSTANCE.getTimezone("PoLYmer", "6605a2073bfb4fdb9efdf98ab5c29e9a", "42.272099", "-71.812028");
//        System.out.println(jsonObject);
        final long start = System.currentTimeMillis();
        LocalDate depDate = LocalDate.of(2020,5,10);
        Flights flights = new CreatePossibleFlights(new SearchCriteria("BOS","CLE",depDate, true, SeatTypeEnum.COACH)).createPossibleConnectingLegCombinations();
        for(Flight flight:flights){
            System.out.println(flight);
        }
        final long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);



    }

}