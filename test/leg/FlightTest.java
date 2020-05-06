package leg;

import dao.ServerInterface;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reservation.Trip;
import time.MyTime;
import utils.Saps;

import java.util.Iterator;

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test functionality in leg.Flight.
 * Significant associations: leg.Flight for the functionality being tested.
 */
public class FlightTest {
    ConnectingLeg leg1;
    ConnectingLeg leg2;
    ConnectingLeg leg3;
    Flight sample;

    @Test
    public void testShallowCopy() {
        Flight copy = sample.shallowCopy();
        Assert.assertNotEquals(copy.hashCode(),sample.hashCode());
        Iterator<ConnectingLeg> legs = sample.getLegs();
        Iterator<ConnectingLeg> legs2 = copy.getLegs();
        Assert.assertEquals(legs.next(),legs2.next());
        Assert.assertEquals(legs.next(),legs2.next());
        Assert.assertEquals(legs.next(),legs2.next());
        Assert.assertFalse(legs.hasNext());
        Assert.assertFalse(legs2.hasNext());
    }

    @Test
    public void testInvalidFlightConstruction() {
        ConnectingLegs badLegs = new leg.ConnectingLegs();
        badLegs.add(leg1);
        badLegs.add(leg3);
        badLegs.add(leg2);
        Assert.assertFalse(leg.Flight.isValid(badLegs));

        ConnectingLeg leg4 = (new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 00:46", "JFK", "2020 May 11 04:11", "182.1", 6, "17.62", 108));
        Flight badLegs2 = new Flight(leg1);
        Assert.assertFalse(badLegs2.addLeg(leg4,true));
        Flight bad3 = new Flight(leg4);
        Assert.assertFalse(bad3.addLeg(leg1,false));

        ConnectingLegs badLegs3 = new leg.ConnectingLegs();
        ConnectingLeg leg5 = (new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 00:46", "JFK", "2020 May 11 20:00", "182.1", 6, "17.62", 108));
        ConnectingLeg leg6 = (new ConnectingLeg("767", "23", "27569", "JFK", "2020 May 12 00:01", "BOS", "2020 May 11 06:53", "16.79", 31, "8.73", 148));
        badLegs3.add(leg5);
        badLegs3.add(leg6);
        Assert.assertFalse(leg.Flight.isValid(badLegs3));
        try {
            new leg.Flight(badLegs3);
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertFalse(Flight.isValid(badLegs3));
        }
        Flight flight = new leg.Flight(leg5);
        Assert.assertFalse(flight.addLeg(leg6));

    }

    @Test
    public void testFlightStatistics() {
        Flight sample = new Flight(leg1);
        Assert.assertTrue(sample.addLeg(leg2, true));
        Assert.assertTrue(sample.addLeg(leg3,true));

        double expectedCoachPrice = 14.78 + 17.62 + 8.73;
        double expectedFirstClassPrice = 182.1 + 28.42 + 16.79;
        Assert.assertEquals(expectedCoachPrice,sample.getPrice(SeatTypeEnum.COACH),.001);
        Assert.assertEquals(expectedFirstClassPrice,sample.getPrice(SeatTypeEnum.FIRSTCLASS),.001);

        //7 hours 15 minutes
        double expectedTravelTime = 7.25;
        Assert.assertEquals(expectedTravelTime,sample.calculateTravelTime(),.001);

        String expectedDepCode = "CLE";
        String expectedArrCode = "BOS";
        Assert.assertEquals(expectedDepCode,sample.getDepartureAirportCode());
        Assert.assertEquals(expectedArrCode,sample.getArrivalAirportCode());
        Assert.assertEquals(expectedDepCode,sample.getAirportCode(true));
        Assert.assertEquals(expectedArrCode,sample.getAirportCode(false));

        MyTime expectedDepTime = new MyTime("2020 May 10 23:38");
        MyTime expectedArrTime = new MyTime("2020 May 11 06:53");
        Assert.assertEquals(0,expectedDepTime.timespan(sample.getDepartureTime()),0);
        Assert.assertEquals(0,expectedArrTime.timespan(sample.getArrivalTime()),0);
        Assert.assertEquals(0,expectedDepTime.timespan(sample.getFlightTime(true)),0);
        Assert.assertEquals(0,expectedArrTime.timespan(sample.getFlightTime(false)),0);

    }

    @Test
    public void testValidConstructors() {
        ConnectingLegs legConst = new ConnectingLegs();
        legConst.add(leg1);
        legConst.add(leg2);
        legConst.add(leg3);
        try{
            new Flight(legConst);
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

        Flight addLegs = new Flight(leg2);
        Assert.assertTrue(addLegs.addLeg(leg1,false));
        Assert.assertTrue(addLegs.addLeg(leg3));
        Assert.assertFalse(addLegs.addLeg(leg2));
        Iterator<ConnectingLeg> legs = addLegs.getLegs();
        Assert.assertEquals(legs.next(),leg1);
        Assert.assertEquals(legs.next(),leg2);
        Assert.assertEquals(legs.next(),leg3);
        Assert.assertFalse(legs.hasNext());

    }

    @Before
    public void setUp() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
        leg1 = (new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129));
        leg2 = (new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 7, "17.62", 108));
        leg3 = (new ConnectingLeg("767", "23", "27569", "JFK", "2020 May 11 06:30", "BOS", "2020 May 11 06:53", "16.79", 31, "8.73", 148));
        sample = new Flight(leg1);
        sample.addLeg(leg2);
        sample.addLeg(leg3);
    }

    @After
    public void tearDown() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
    }

    @Test
    public void testAvailability() {
        try {
            // Original should be six
            int oldFirstSeatsOnSecond = 7;
            Iterator<ConnectingLeg> legs = sample.getLegs();
            legs.next();
            Assert.assertEquals(oldFirstSeatsOnSecond,legs.next().seating().getFirstClassReserved());
            sample.refresh();
            Iterator<ConnectingLeg> newLegs = sample.getLegs();
            newLegs.next();
            int newFirstSeatsOnSecond = newLegs.next().seating().firstClassReserved;
            Assert.assertTrue(newFirstSeatsOnSecond < oldFirstSeatsOnSecond);

            Assert.assertTrue(sample.allSeatsStillAvailable(SeatTypeEnum.FIRSTCLASS));
            Trip forReserve = new Trip(sample,SeatTypeEnum.FIRSTCLASS);
            int maxFirstClassSeats = 12;
            for(int i = 0; i < maxFirstClassSeats - newFirstSeatsOnSecond; i++){
                Assert.assertTrue(forReserve.reserveSeats());
            }
            Assert.assertFalse(sample.allSeatsStillAvailable(SeatTypeEnum.FIRSTCLASS));
            Assert.assertTrue(sample.allSeatsStillAvailable(SeatTypeEnum.COACH));
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
