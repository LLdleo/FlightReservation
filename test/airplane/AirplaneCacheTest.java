package airplane;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test AirplaneCache class's ability to get airports.
 * Significant associations: AirplaneCache and Airplanes for functionality, WPI server for availability and unique variables.
 */
public class AirplaneCacheTest {
    @Test
    public void testGetAirplaneByModel() {
        Assert.assertNull(AirplaneCache.INSTANCE.getAirplaneByModel("not a model"));
        Airplane result = AirplaneCache.INSTANCE.getAirplaneByModel("747");
        Airplane expected = new Airplane("Boeing","747",124,400);
        Assert.assertEquals(result, expected);
    }
}
