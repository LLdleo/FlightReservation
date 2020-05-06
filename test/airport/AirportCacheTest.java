package airport;

import airplane.Airplane;
import airplane.AirplaneCache;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test AirportCache class's ability to get airports.
 * Significant associations: AirportCache and Airports for functionality, WPI server for availability and unique variables.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class AirportCacheTest {
    @Test
    public void testGetAirportByCode() {
        Assert.assertNull(AirportCache.INSTANCE.getAirportByCode("not a code"));
        Airport result = AirportCache.INSTANCE.getAirportByCode("MSP");
        Airport expected = new Airport("Minneapolis/St. Paul International","MSP",44.885002,-93.222296);
        Assert.assertEquals(result, expected);
    }
}
