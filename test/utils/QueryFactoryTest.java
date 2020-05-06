package utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test that the queries are constructed correctly given strings
 * Significant associations: QueryFactory for the functions tested and WPI server for what the format should be.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class QueryFactoryTest {

    @Test
    public void testAllQueryBuilders() {
        String team = Saps.TEAMNAME;
        String xmlFlights = "<Flights><Flight number=\"1392\" seating=\"Coach\"/></Flights>";
        String listType = "departing";
        String airportCode = "BOS";
        String day = "2020_05_10";
        double lat = 20;
        double longitude = 10;
        String expectedAirport = "?team=PoLYmer&action=list&list_type=airports";
        String expectedAirplane = "?team=PoLYmer&action=list&list_type=airplanes";
        String expectedUnlock = "team=PoLYmer&action=unlockDB";
        String expectedLock = "team=PoLYmer&action=lockDB";
        String expectedReset = "?team=PoLYmer&action=resetDB";
        String expectedLegs = "?team=PoLYmer&action=list&list_type=departing&airport=BOS&day=2020_05_10";
        String expectedReserve = "team=PoLYmer&action=buyTickets&flightData=" + xmlFlights;
        String expectedTimezone = "&lat=" + lat + "&long=" + longitude;

        Assert.assertEquals(QueryFactory.getAirplanes(team), expectedAirplane);
        Assert.assertEquals(QueryFactory.getAirports(team), expectedAirport);
        Assert.assertEquals(QueryFactory.getLegs(team, listType, airportCode, day), expectedLegs);
        Assert.assertEquals(QueryFactory.getTimezoneOffset(lat, longitude), expectedTimezone);
        Assert.assertEquals(QueryFactory.lock(team), expectedLock);
        Assert.assertEquals(QueryFactory.reserve(team, xmlFlights), expectedReserve);
        Assert.assertEquals(QueryFactory.resetDB(team), expectedReset);
        Assert.assertEquals(QueryFactory.unlock(team), expectedUnlock);


    }
}
