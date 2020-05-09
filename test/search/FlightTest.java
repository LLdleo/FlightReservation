package search;

import dao.ServerInterface;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.Flight;
import leg.SeatTypeEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reservation.Trip;
import time.MyTime;
import utils.Saps;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

/**
 * Test search.Flight conversion and statistics for filtering and sorting.
 *
 * @author Jackson Powell
 * @since 2020-05-08
 */
public class FlightTest {
    /**
     * leg1 is the first leg of the valid flight combination used for testing. Goes from CLE to DCA.
     */
    leg.ConnectingLeg leg1;
    /**
     * leg2 is the second leg in the valid flight combination used for testing. Goes from DCA to JFK.
     */
    leg.ConnectingLeg leg2;
    /**
     * leg3 is the final leg in the valid flight combination used for testing. Goes from JFK to BOS.
     */
    leg.ConnectingLeg leg3;
    /**
     * sample is the valid flight combination used for testing of a valid flight. Goes from CLE to BOS through leg1, leg2, and then leg3.
     */
    leg.Flight sample;
    /**
     * convertSample is the converted flight sample for testing conversion and statistic calculation.
     */
    search.Flight convertSample;


    /**
     * Test aggregation methods for flight statistics such as price and travel time.
     */
    @Test
    public void testFlightStatistics() {

        double expectedCoachPrice = 14.78 + 17.62 + 8.73;
        double expectedFirstClassPrice = 182.1 + 28.42 + 16.79;
        Assert.assertEquals(expectedCoachPrice, convertSample.getCoachPrice(), .001);
        Assert.assertEquals(expectedFirstClassPrice, convertSample.getFirstClassPrice(), .001);

        //7 hours 15 minutes
        double expectedTravelTime = 7.25;
        Assert.assertEquals(expectedTravelTime, convertSample.getTravelTime(), .001);
        LocalDateTime expectedDepGMT = LocalDateTime.of(2020,5,10,23,38);
        LocalDateTime expectedArrGMT = LocalDateTime.of(2020,5,11,6,53);
        //offset is -5 for both
        LocalDateTime expectedDepLocal = LocalDateTime.of(2020,5,10,19,38);
        LocalDateTime expectedArrLocal = LocalDateTime.of(2020,5,11,2,53);
        Assert.assertTrue(expectedArrGMT.isEqual(convertSample.getArrivalTime().getGmtTime()));
        Assert.assertTrue(expectedArrLocal.isEqual(convertSample.getArrivalTime().getLocalTime()));
        Assert.assertTrue(expectedDepGMT.isEqual(convertSample.getDepartureTime().getGmtTime()));
        Assert.assertTrue(expectedDepLocal.isEqual(convertSample.getDepartureTime().getLocalTime()));

    }

    /**
     * Test the function that checks whether an arrival or departure time is in a time window.
     */
    @Test
    public void testInRange() {
        LocalTime departure = LocalTime.of(19,38);
        LocalTime arrival = LocalTime.of(2,53);
        Assert.assertTrue(convertSample.inRange(departure,departure.plusHours(1),true));
        Assert.assertTrue(convertSample.inRange(arrival,arrival.plusHours(1),false));
        Assert.assertTrue(convertSample.inRange(departure.minusHours(1),departure.plusMinutes(1),true));
        Assert.assertFalse(convertSample.inRange(departure.minusHours(1),departure.plusMinutes(1),false));
        Assert.assertTrue(convertSample.inRange(arrival.minusHours(1),arrival.plusMinutes(1),false));
        Assert.assertFalse(convertSample.inRange(arrival.minusHours(1),arrival.plusMinutes(1),true));


    }

    /**
     * Create the sample flight to test flight functions on.
     * @throws Exception If there is an exception when converting the flight.
     */
    @Before
    public void setUp() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
        leg1 = (new leg.ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129));
        leg2 = (new leg.ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 7, "17.62", 108));
        leg3 = (new leg.ConnectingLeg("767", "23", "27569", "JFK", "2020 May 11 06:30", "BOS", "2020 May 11 06:53", "16.79", 31, "8.73", 148));
        sample = new Flight(leg1);
        sample.addLeg(leg2);
        sample.addLeg(leg3);
        convertSample = new search.Flight(sample);
    }

    /**
     * Reset the database.
     */
    @After
    public void tearDown(){
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
    }

    /**
     * Test the availability checker which does not automatically refresh for search.Flight.
     */
    @Test
    public void testAvailability() {
        try {
            // Original should be six
            int oldFirstSeatsOnSecond = 7;
            search.ConnectingLegs legs = convertSample.getConnectingLegList();
            Assert.assertEquals(oldFirstSeatsOnSecond, legs.get(1).getSeating().getFirstClassReserved());
            sample.refresh();
            convertSample = new search.Flight(sample);
            search.ConnectingLegs newLegs = convertSample.getConnectingLegList();
            int newFirstSeatsOnSecond = newLegs.get(1).getSeating().getFirstClassReserved();
            Assert.assertTrue(newFirstSeatsOnSecond < oldFirstSeatsOnSecond);

            Assert.assertTrue(convertSample.allSeatsAvailable(SeatTypeEnum.FIRSTCLASS));
            Trip forReserve = new Trip(convertSample.convertBack(), SeatTypeEnum.FIRSTCLASS);
            int maxFirstClassSeats = 12;
            for (int i = 0; i < maxFirstClassSeats - newFirstSeatsOnSecond; i++) {
                Assert.assertTrue(forReserve.reserveSeats());
            }
            sample.refresh();
            convertSample = new search.Flight(sample);
            Assert.assertFalse(sample.allSeatsStillAvailable(SeatTypeEnum.FIRSTCLASS));
            Assert.assertFalse(convertSample.allSeatsAvailable(SeatTypeEnum.FIRSTCLASS));
            Assert.assertTrue(sample.allSeatsStillAvailable(SeatTypeEnum.COACH));
            Assert.assertTrue(convertSample.allSeatsAvailable(SeatTypeEnum.COACH));




        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testConvertBack() {
        leg.Flight converted = convertSample.convertBack();
        Iterator<leg.ConnectingLeg> legs4 = converted.getLegs();
        Iterator<leg.ConnectingLeg> legs5 = sample.getLegs();
        Assert.assertEquals(legs4.next(),legs5.next());
        Assert.assertEquals(legs4.next(),legs5.next());
        Assert.assertEquals(legs4.next(),legs5.next());
        Assert.assertFalse(legs4.hasNext());
        Assert.assertFalse(legs5.hasNext());
        Assert.assertEquals(converted.getArrivalAirportCode(),sample.getArrivalAirportCode());
        Assert.assertEquals(converted.getDepartureAirportCode(),sample.getDepartureAirportCode());
        Assert.assertEquals(converted.getPrice(SeatTypeEnum.COACH),sample.getPrice(SeatTypeEnum.COACH),0.001);
        Assert.assertEquals(converted.getPrice(SeatTypeEnum.FIRSTCLASS),sample.getPrice(SeatTypeEnum.FIRSTCLASS),.001);
        Assert.assertEquals(converted.calculateTravelTime(),sample.calculateTravelTime(),0.001);
        Assert.assertTrue(converted.getArrivalTime().getGmtTime().isEqual(sample.getArrivalTime().getGmtTime()));
        Assert.assertTrue(converted.getDepartureTime().getGmtTime().isEqual(sample.getDepartureTime().getGmtTime()));
        Assert.assertTrue(converted.getDepartureTime().getLocalTime().isEqual(sample.getDepartureTime().getLocalTime()));
        Assert.assertTrue(converted.getArrivalTime().getLocalTime().isEqual(sample.getArrivalTime().getLocalTime()));
    }
}

