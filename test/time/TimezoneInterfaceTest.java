package time;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.sql.Time;

/**
 * @author Jackson Powell
 * Testing class for junit tests on TimezoneInterface
 */
public class TimezoneInterfaceTest {

    @Test
    public void testTimezoneOffsetAPIEdgeCases() {
        try{
            double actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(0,0); // Null Island
            Assert.assertEquals(0,actualOffset,0);
            actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(51.5,0); // Greenwich
            Assert.assertEquals(0,actualOffset,0);
            actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(0,-72); // Columbia
            Assert.assertEquals(-5,actualOffset,0);
            actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(29,76); // New Delhi, India
            Assert.assertEquals(5.5, actualOffset,0);
            actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(90,-180);
            Assert.assertEquals(12,actualOffset,0);
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void testTimezoneOffsetAPI() {
        // Test example given on ipgeolocation for brisbane, Australia having timezone offset of 10 hours
        try {
            Double actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(-27.4748, 153.017);
            double expectedOffset = 10;
            Assert.assertNotNull(actualOffset);
            Assert.assertEquals(expectedOffset, actualOffset, 0);
        }
        catch(Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testCachingPersistence() {
        // Test that cache does not have the timezone for Port Columbus International (CMH), and then does
        double sampleLatitude = 40.000158;
        double sampleLongitude = -82.887198;
        double expectedOffset = -5;
        try {
            Assert.assertFalse(Timezones.INSTANCE.isLocationCached(sampleLatitude, sampleLongitude));
            Assert.assertEquals(expectedOffset, TimezoneInterface.INSTANCE.getTimezoneOffset(sampleLatitude, sampleLongitude), 0);
            Assert.assertTrue(Timezones.INSTANCE.isLocationCached(sampleLatitude, sampleLongitude));
            Assert.assertEquals(expectedOffset, TimezoneInterface.INSTANCE.getTimezoneOffset(sampleLatitude, sampleLongitude), 0);
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testParameterExceptions() {
        boolean success = false;
        try{
            TimezoneInterface.INSTANCE.getTimezoneOffset(-90.01,2);
            Assert.fail();
        }
        catch(InvalidParameterException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);
        success = false;
        try{
            TimezoneInterface.INSTANCE.getTimezoneOffset(25,181);
            Assert.fail();
        }
        catch (InvalidParameterException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);

    }
}
