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
        Assert.assertNotNull(actualOffset);
        Assert.assertEquals(10, actualOffset,0);
    }

}
