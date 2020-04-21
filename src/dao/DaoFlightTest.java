package dao;

import flight.Flights;

import static org.junit.Assert.*;

public class DaoFlightTest {
    public static void main(String[] args) {
        Flights flights = DaoFlight.buildFlight("BOS", "CLE", "2020_05_10", "2020_05_10");
        System.out.println(flights.mFlights);
    }
}