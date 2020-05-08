package search;

import airplane.AirplaneCache;
import airport.Airport;
import airport.AirportCache;
import leg.ConnectingLeg;
import org.junit.Assert;
import org.junit.Test;
import time.MyTime;

/**
 * Test search.ConnectingLeg object and conversion from leg.ConnectingLeg
 * Significant associations: search.ConnectingLeg for tested functionality, leg.ConnectingLeg for base data, and airports, airplanes, and MyTime for the objects with extended information.
 *
 * @author Jackson Powell
 * @since 2020-05-06
*/
public class ConnectingLegTest {
    @Test
    public void testLegConversion() {
        //Without exposing the cache, not sure how to test whether it's making use of it.
        leg.ConnectingLeg baseLeg = (new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 6, "17.62", 108));
        try{
            search.ConnectingLeg newLeg = search.ConnectingLeg.convertLeg(baseLeg);

            Assert.assertEquals(baseLeg.seating().getFirstClassReserved(),newLeg.getSeating().getFirstClassReserved());
            Assert.assertEquals(baseLeg.seating().getCoachReserved(),newLeg.getSeating().getCoachReserved());
            Assert.assertEquals(baseLeg.seating().getCoachPrice(),newLeg.getSeating().getCoachPrice(),0.001);
            Assert.assertEquals(baseLeg.seating().getFirstClassPrice(),newLeg.getSeating().getFirstClassPrice(),0.001);
            Assert.assertEquals(AirplaneCache.INSTANCE.getAirplaneByModel(baseLeg.airplane()),newLeg.getAirplane());
            Assert.assertEquals(AirportCache.INSTANCE.getAirportByCode(baseLeg.departure().getCode()),newLeg.getDepartureAirport());
            Assert.assertEquals(AirportCache.INSTANCE.getAirportByCode(baseLeg.arrival().getCode()),newLeg.getArrivalAirport());
            Assert.assertEquals(baseLeg.flightTime(),newLeg.getFlightTime());
            Assert.assertEquals(baseLeg.number(),newLeg.getFlightNumber());
            Airport baseDepAir = AirportCache.INSTANCE.getAirportByCode(baseLeg.departure().getCode());
            Airport baseArrAir = AirportCache.INSTANCE.getAirportByCode(baseLeg.arrival().getCode());
            MyTime depTime = new MyTime(baseLeg.departure().getTime(),baseDepAir.latitude(),baseDepAir.longitude());
            MyTime arrTime = new MyTime(baseLeg.arrival().getTime(),baseArrAir.latitude(),baseArrAir.longitude());
            Assert.assertTrue(newLeg.getDepartureTime().getGmtTime().isEqual(depTime.getGmtTime()));
            Assert.assertTrue(newLeg.getDepartureTime().getLocalTime().isEqual(depTime.getLocalTime()));
            Assert.assertTrue(newLeg.getArrivalTime().getGmtTime().isEqual(arrTime.getGmtTime()));
            Assert.assertTrue(newLeg.getArrivalTime().getLocalTime().isEqual(arrTime.getLocalTime()));

            search.ConnectingLeg newLeg2 = search.ConnectingLeg.convertLeg(baseLeg);
            Assert.assertEquals(newLeg,newLeg2);

            leg.ConnectingLeg oldLeg = newLeg.convertBack();
            Assert.assertEquals(oldLeg,baseLeg);

            String expectedGMTArrival = "2020 May 11 04:11";
            String expectedGMTDeparture = "2020 May 11 03:36";

            Assert.assertEquals(expectedGMTArrival,newLeg.getGMTStringTime(newLeg.getArrivalTime()));
            Assert.assertEquals(expectedGMTDeparture,newLeg.getGMTStringTime(newLeg.getDepartureTime()));

            String expectedGMTArrivalOther = "2020 MAY 11 04:11";
            String expectedGMTDepartureOther = "2020 MAY 11 03:36";
            Assert.assertEquals(expectedGMTArrivalOther,newLeg.timeString(newLeg.getArrivalTime().getGmtTime()));
            Assert.assertEquals(expectedGMTDepartureOther,newLeg.timeString(newLeg.getDepartureTime().getGmtTime()));
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
}
