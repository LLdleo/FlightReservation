package reservation;

import dao.ServerInterface;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.Flight;
import leg.SeatTypeEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.QueryFactory;
import utils.Saps;

import java.util.Iterator;

/**
 * Test making a trip and trying to reserve it for both one-way and round trips.
 * Significant associations: Trip for the functionality being tested and WPI server for the protocols needed for locking to check possible exceptions.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class TripTest {
    /**
     * oneWay is a sample one-way trip to test that it has coach seats reserved.
     */
    Trip oneWay;
    /**
     * round is a sample round trip containing the flight in the one-way trip and another flight that will be fully reserved.
     */
    Trip round;
    /**
     * full is a sample trip that includes at least one flight that doesn't have any seats.
     */
    Trip full;
    @Test
    public void testReserveOneWay() {
        int numCoachReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getCoachReserved();
        int numFirstClassReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getFirstClassReserved();
        try{
            Assert.assertTrue(oneWay.reserveSeats());
            oneWay.getOutgoingFlight().refresh();
            int newCoachReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getCoachReserved();
            int newFirstReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getFirstClassReserved();
            Assert.assertEquals(numCoachReserved + 1,newCoachReserved);
            Assert.assertEquals(numFirstClassReserved,newFirstReserved);
        }
        catch (Exception e){
            Assert.fail();
        }

    }

    @Before
    public void setUp() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
        // Max 737 is 28 first class and 100 coach
        // 767 104 first class and 200 coach
        //A320 is 12 first class and 124 coach
        // A310 is 24 first class and 200 coach
        // 747 is 124 first class and 400 coach
        Flight outGoingFlight = new Flight(new ConnectingLeg("737", "68", "3783", "BOS", "2020 May 10 06:09", "CLE", "2020 May 10 07:17", "183.22", 7, "51.3", 62));
        ConnectingLegs legs = new leg.ConnectingLegs();
        legs.add(new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129));
        legs.add(new ConnectingLeg("A320", "29", "48382", "DCA", "2020 May 11 03:36", "JFK", "2020 May 11 04:11", "182.1", 6, "17.62", 108));
        legs.add(new ConnectingLeg("767", "23", "27569", "JFK", "2020 May 11 06:30", "BOS", "2020 May 11 06:53", "16.79", 31, "8.73", 148));
        Flight returnFlight = new Flight(legs);

        ConnectingLegs legs2 = new leg.ConnectingLegs();
        legs2.add(new ConnectingLeg("767", "15", "8522", "CLE", "2020 May 10 10:48", "CMH", "2020 May 10 11:03", "11.12", 31, "5.78", 170));
        legs2.add(new ConnectingLeg("A310", "83", "9488", "CMH", "2020 May 10 14:14", "BOS", "2020 May 10 15:37", "262.64", 15, "31.52", 72));

        ConnectingLegs legs3 = new ConnectingLegs();
        legs3.add(new ConnectingLeg("747", "82", "8549", "CLE", "2020 May 11 03:50", "BOS", "2020 May 11 05:12", "49.91", 43, "15.47", 155));
        Flight fullFlight2 = new Flight(legs3);
        Flight fullFlight = new Flight(legs2);
        oneWay = new Trip(outGoingFlight, SeatTypeEnum.COACH);
        round = new Trip(outGoingFlight, returnFlight, SeatTypeEnum.FIRSTCLASS, SeatTypeEnum.COACH);
        full = new Trip(fullFlight, fullFlight2, SeatTypeEnum.FIRSTCLASS, SeatTypeEnum.FIRSTCLASS);
    }

    @After
    public void tearDown() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
    }

    @Test
    public void testReserveRound() {
        int outNumCoachReserved = round.getOutgoingFlight().getLegs().next().seating().getCoachReserved();
        int outNumFirstClassReserved = round.getOutgoingFlight().getLegs().next().seating().getFirstClassReserved();
        Iterator<ConnectingLeg> roundLegs = round.getReturnFlight().getLegs();
        ConnectingLeg leg1 = roundLegs.next();
        ConnectingLeg leg2 = roundLegs.next();
        ConnectingLeg leg3 = roundLegs.next();
        int retNumCoachReserved = leg1.seating().getCoachReserved();
        int retNumFirstClassReserved = leg1.seating().getFirstClassReserved();
        int retNumCoachReserved2 = leg2.seating().getCoachReserved();
        int retNumFirstClassReserved2 = leg2.seating().getFirstClassReserved();
        int retNumCoachReserved3 = leg3.seating().getCoachReserved();
        int retNumFirstClassReserved3 = leg3.seating().getFirstClassReserved();
        try{
            Assert.assertTrue(round.reserveSeats());
            round.getOutgoingFlight().refresh();
            round.getReturnFlight().refresh();
            int newCoachReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getCoachReserved();
            int newFirstReserved = oneWay.getOutgoingFlight().getLegs().next().seating().getFirstClassReserved();
            Iterator<ConnectingLeg> newRoundLegs = round.getReturnFlight().getLegs();
            ConnectingLeg newleg1 = newRoundLegs.next();
            ConnectingLeg newleg2 = newRoundLegs.next();
            ConnectingLeg newleg3 = newRoundLegs.next();
            int retNewCoachReserved = newleg1.seating().getCoachReserved();
            int retNewFirstClassReserved = newleg1.seating().getFirstClassReserved();
            int retNewCoachReserved2 = newleg2.seating().getCoachReserved();
            int retNewFirstClassReserved2 = newleg2.seating().getFirstClassReserved();
            int retNewCoachReserved3 = newleg3.seating().getCoachReserved();
            int retNewFirstClassReserved3 = newleg3.seating().getFirstClassReserved();
            Assert.assertEquals(outNumCoachReserved,newCoachReserved);
            Assert.assertEquals(outNumFirstClassReserved + 1,newFirstReserved);

            Assert.assertEquals(retNumCoachReserved + 1,retNewCoachReserved);
            Assert.assertEquals(retNumFirstClassReserved,retNewFirstClassReserved);
            Assert.assertEquals(retNumCoachReserved2 + 1,retNewCoachReserved2);
            Assert.assertEquals(retNumFirstClassReserved2,retNewFirstClassReserved2);
            Assert.assertEquals(retNumCoachReserved3 + 1,retNewCoachReserved3);
            Assert.assertEquals(retNumFirstClassReserved3,retNewFirstClassReserved3);
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testNoSeatsToReserve() {
        Iterator<ConnectingLeg> legs = full.getOutgoingFlight().getLegs();
        legs.next();
        int firstReserved = legs.next().seating().getFirstClassReserved();
        int maxFirstClassSeats = 24;
        try {
            for (int i = 0; i < maxFirstClassSeats - firstReserved; i++) {
                Assert.assertTrue(full.reserveSeats());
            }
            Assert.assertFalse(full.reserveSeats());
            Trip otherBrokenTrip = new Trip(full.getReturnFlight(), full.getOutgoingFlight(), SeatTypeEnum.COACH, SeatTypeEnum.FIRSTCLASS);
            Assert.assertFalse(otherBrokenTrip.reserveSeats());
            Trip otherTrip = new Trip(full.getOutgoingFlight(),full.getReturnFlight(),SeatTypeEnum.COACH,SeatTypeEnum.FIRSTCLASS);
            Assert.assertTrue(otherTrip.reserveSeats());
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
