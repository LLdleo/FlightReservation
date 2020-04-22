package time;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jackson Powell
 * Testing class for junit tests on TimezoneInterface
 */
public class TimezoneInterfaceTest {

    @Test
    public void testTimezoneOffsetAPI() {
        // Test example given on ipgeolocation for brisbane, Australia having timezone offset of 10 hours
        Double actualOffset = TimezoneInterface.INSTANCE.getTimezoneOffset(-27.4748, 153.017);
        double expectedOffset = 10;
        Assert.assertNotNull(actualOffset);
        Assert.assertEquals(expectedOffset, actualOffset,0);
    }

    @Test
    public void testCachingPersistence() {
        // Test that cache does not have the timezone for Port Columbus International (CMH), and then does
        double sampleLatitude = 40.000158;
        double sampleLongitude = -82.887198;
        double expectedOffset = -5;
        Assert.assertFalse(Timezones.INSTANCE.isLocationCached(sampleLatitude, sampleLongitude));
        Assert.assertEquals(expectedOffset, TimezoneInterface.INSTANCE.getTimezoneOffset(sampleLatitude,sampleLongitude),0);
        Assert.assertTrue(Timezones.INSTANCE.isLocationCached(sampleLatitude,sampleLongitude));
        Assert.assertEquals(expectedOffset, TimezoneInterface.INSTANCE.getTimezoneOffset(sampleLatitude,sampleLongitude),0);
    }
}
