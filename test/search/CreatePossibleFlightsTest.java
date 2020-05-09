package search;

import airplane.Airplane;
import airplane.AirplaneCache;
import airport.Airport;
import airport.AirportCache;
import dao.ServerInterface;
import leg.ConnectingLeg;
import leg.Flights;
import leg.SeatTypeEnum;
import org.junit.*;
import reservation.Trip;
import time.MyTime;
import utils.Saps;

import java.time.LocalDate;
import java.util.Iterator;

/**
 * Test CreatePossibleFlights for creating valid flights matching constraints (will not check that every combination is produced since getting the expected values and checking permutations is too time intensive.
 * Significant associations: CreatePossibleFlights for the functionality to test. Flight for the constraints to check.
 *
 * @author Jackson Powell
 * @since 2020-05-06
  */
public class CreatePossibleFlightsTest {
    /**
     * criteria is the criteria used to create available flights.
     */
    private static SearchCriteria criteria;
    /**
     * criteriaBack is the criteria used to create returnAvailableFlights.
     */
    private static SearchCriteria criteriaBack;
    /**
     * availableFlights is the list of flights that matched the criteria and flight constraints. Calculated once to save time.
     */
    private static Flights availableFlights;
    /**
     * returnAvailableFlights is the list of flights that matched the criteriaBack and flight constraints. Calculated once to save time
     */
    private static Flights returnAvailableFlights;

    /**
     * fullLeg is a connecting leg that would be part of availableFlights but has fully reserved and so shouldn't appear anywhere.
     */
    private static ConnectingLeg fullLeg;

    /**
     * Conduct two searches for all other tests in this class.
     * @throws Exception If there is an exception when trying to search for flights.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
        fullLeg = (new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 6, "17.62", 108));
        int oldFirst = 6;
        int maxFirst = 12;
        int oldCoach = 108;
        int maxCoach = 124;
        leg.Flight fullFlight = new leg.Flight(fullLeg);
        Trip coach = new Trip(fullFlight, SeatTypeEnum.COACH);
        Trip first = new Trip(fullFlight, SeatTypeEnum.FIRSTCLASS);
        for(int i = 0; i < maxCoach - oldCoach; i++){
            coach.reserveSeats();
        }
        for(int i = 0; i < maxFirst - oldFirst; i++){
            first.reserveSeats();
        }
        criteria = new SearchCriteria("CLE","BOS", LocalDate.of(2020,5,10),true);
        criteriaBack = new SearchCriteria("BOS","CLE", LocalDate.of(2020,5,24), false);
        CreatePossibleFlights flightCreator = new CreatePossibleFlights(criteria);
        CreatePossibleFlights flightCreator2 = new CreatePossibleFlights(criteriaBack);
        availableFlights = flightCreator.createPossibleConnectingLegCombinations();
        returnAvailableFlights = flightCreator2.createPossibleConnectingLegCombinations();
    }

    /**
     * Reset the database.
     */
    @AfterClass
    public static void tearDown(){
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
    }

    /**
     * Test that the constraints of departure date and airports for searching based on departure.
     */
    @Test
    public void testConstraintsMetDeparture() {
        try{
        for(leg.Flight flight: availableFlights){
            Assert.assertEquals(criteria.getDepartureAirportCode(),flight.getDepartureAirportCode());
            Assert.assertEquals(criteria.getArrivalAirportCode(),flight.getArrivalAirportCode());
            Airport depAirport = AirportCache.INSTANCE.getAirportByCode(flight.getDepartureAirportCode());
            LocalDate localDate = (new MyTime(flight.getDepartureTime().getGmtTime(), depAirport.latitude(),depAirport.longitude())).getLocalTime().toLocalDate();
            Assert.assertTrue(criteria.getFlightDate().isEqual(localDate));
            Iterator<leg.ConnectingLeg> legs = flight.getLegs();
            Assert.assertTrue(legs.hasNext());
            ConnectingLeg currentLeg;
            while(legs.hasNext()){
                currentLeg = legs.next();
                Airplane air = AirplaneCache.INSTANCE.getAirplaneByModel(currentLeg.airplane());
                Assert.assertNotEquals(currentLeg.number(),fullLeg.number());
                Assert.assertTrue(currentLeg.seating().getFirstClassReserved() < air.firstClassSeats() || currentLeg.seating().getCoachReserved() < air.coachSeats());
            }
        }
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
    /**
     * Test that the constraints of arrival date and airports for searching based on arrival.
     */
    @Test
    public void testConstraintsMetArrival() {
        try{
        for(leg.Flight flight: returnAvailableFlights){
            Assert.assertEquals(criteriaBack.getDepartureAirportCode(),flight.getDepartureAirportCode());
            Assert.assertEquals(criteriaBack.getArrivalAirportCode(),flight.getArrivalAirportCode());
            Airport arrAirport = AirportCache.INSTANCE.getAirportByCode(flight.getArrivalAirportCode());

            LocalDate localDate = (new MyTime(flight.getArrivalTime().getGmtTime(), arrAirport.latitude(),arrAirport.longitude())).getLocalTime().toLocalDate();
            Assert.assertTrue(criteriaBack.getFlightDate().isEqual(localDate));
            Iterator<leg.ConnectingLeg> legs = flight.getLegs();
            Assert.assertTrue(legs.hasNext());
            ConnectingLeg currentLeg;
            while(legs.hasNext()){
                currentLeg = legs.next();
                Airplane air = AirplaneCache.INSTANCE.getAirplaneByModel(currentLeg.airplane());
                Assert.assertNotEquals(currentLeg.number(),fullLeg.number());
                Assert.assertTrue(currentLeg.seating().getFirstClassReserved() < air.firstClassSeats() || currentLeg.seating().getCoachReserved() < air.coachSeats());
            }
        }
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
