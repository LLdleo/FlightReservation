package dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.Flight;
import leg.SeatTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import reservation.Trip;
import utils.Saps;

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test the server interface methods by calling them and checking that something is returned and that the method does as is documented
 * Significant associations: ServerInterface for the functionality being tested and wpi server for providing consistent information and responses for locking.
 */
public class ServerInterfaceTest {

    @Test
    public void testResetAndLocking() {
        Assert.assertTrue(ServerInterface.INSTANCE.reset(Saps.TEAMNAME));
        boolean success = ServerInterface.INSTANCE.lock(Saps.TEAMNAME);
        Assert.assertTrue(success);
        Assert.assertTrue(ServerInterface.INSTANCE.reset(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME)); // Can't test lock conflicts on same system
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
    }

    @Test
    public void testNotNullGetMethods() {
        try{
            Assert.assertNotNull(ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME));
            Assert.assertNotNull(ServerInterface.INSTANCE.getAirplanes(Saps.TEAMNAME));
            // Test getting legs for airports besides BOS and CLE for boundary cases
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing","PHX","2020_05_10"));
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing","HNL","2020_05_30"));
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"arriving","LGA","2020_05_10"));
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"arriving","TPA","2020_05_31"));
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing","PHL","2020_05_23"));
            Assert.assertNotNull(ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"arriving","MSY","2020_05_23"));

            Assert.assertEquals(0,ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing","DCA", "2020_05_09").size());
            Assert.assertEquals(0,ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"departing","IAD", "2020_06_01").size());
            Assert.assertEquals(0,ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"arriving","EWR", "2020_06_01").size());
            Assert.assertEquals(0,ServerInterface.INSTANCE.getLegs(Saps.TEAMNAME,"arriving","LAX", "2020_05_09").size());
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testServerReserve() {
        try {
            Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME));
            Flight outGoingFlight = new Flight(new ConnectingLeg("737", "68", "3783", "BOS", "2020 May 10 06:09", "CLE", "2020 May 10 07:17", "183.22", 7, "51.3", 62));
            ConnectingLegs legs3 = new ConnectingLegs();
            legs3.add(new ConnectingLeg("747", "82", "8549", "CLE", "2020 May 11 03:50", "BOS", "2020 May 11 05:12", "49.91", 43, "15.47", 155));
            Flight returnFlight = new Flight(legs3);
            Trip trip = new Trip(outGoingFlight,returnFlight, SeatTypeEnum.COACH, SeatTypeEnum.FIRSTCLASS);
            Assert.assertTrue(ServerInterface.INSTANCE.reserve(Saps.TEAMNAME,trip));
            Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
            Assert.assertFalse(ServerInterface.INSTANCE.reserve(Saps.TEAMNAME,trip));
            Assert.assertTrue(ServerInterface.INSTANCE.reset(Saps.TEAMNAME));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
