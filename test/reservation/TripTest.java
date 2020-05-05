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

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test making a trip and trying to reserve it for both one-way and round trips.
 * Significant associations: Trip for the functionality being tested and WPI server for the protocols needed for locking to check possible exceptions.
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
    public void lockedDatabase() {
        try{
            Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME));
            this.oneWay.reserveSeats();
        }
        catch (ServerLockException e){
            Assert.assertEquals(e.toString(),Saps.LOCK_EXCEPTION_MESSAGE);
            ServerInterface.INSTANCE.unlock(Saps.TEAMNAME);
            try {
                Assert.assertTrue(this.oneWay.reserveSeats());
            }
            catch (Exception e2){
                e2.printStackTrace();
                Assert.fail();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Before
    public void setUp() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
        // Max 737 is 28 first class and 100 coach
        // 767 104 first class and 200 coach
        //A320 is 12 first class and 124 coach
        Flight outGoingFlight = new Flight(new ConnectingLeg("737","68","3783","BOS","2020 May 10 06:09","CLE","2020 May 10 07:17","183.22",7,"51.3",62));
        ConnectingLegs legs = new leg.ConnectingLegs();
        legs.add(new ConnectingLeg("767","39","8543","CLE","2020 May 10 23:38","DCA","2020 May 11 00:17","28.42",2,"14.78",129));
        legs.add(new ConnectingLeg("A320","29","48382","DCA","2020 May 11 03:36","JFK","2020 May 11 04:11","182.1",6,"17.62",108));
        legs.add(new ConnectingLeg("767","23","27569","JFK","2020 May 05 11 06:30","BOS","2020 May 11 06:53","16.79",31,"8.73",148));
        Flight returnFlight = new Flight(legs);
        Flight fullFlight;
        oneWay = new Trip(outGoingFlight, SeatTypeEnum.COACH);
        round = new Trip(outGoingFlight,returnFlight,SeatTypeEnum.FIRSTCLASS,SeatTypeEnum.COACH);
        //full = new Trip();

    }

    @After
    public void tearDown() throws Exception {
        ServerInterface.INSTANCE.reset(Saps.TEAMNAME);
    }
}
