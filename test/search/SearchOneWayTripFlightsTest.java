package search;

import org.junit.Assert;
import org.junit.Test;
import utils.Saps;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Jackson Powell
 * @since 2020-05-06
 * Responsibilities: Test SearchOneWayTripFlights by testing more details of search results using information provided by extended connecting leg and flight
 * Significant associations: SearchOneWayTripFlights for functionality, CreatePossibleFlights for implementing the search, and search.ConnectingLeg and search.Flight for the extended information used in testing.
 */
public class SearchOneWayTripFlightsTest {

    @Test
    public void testSearchConstraintsMet() {
        try {
            SearchCriteria criteria = new SearchCriteria("CLE", "BOS", LocalDate.of(2020, 5, 10), true);
            SearchOneWayTripFlights flightSearcher = new SearchOneWayTripFlights(criteria);
            List<Flight> availableFlights = flightSearcher.search();
            for(Flight flight : availableFlights){
                int numLegs = flight.getNumLegs();
                if(numLegs > 3 || numLegs < 1){
                    Assert.fail();
                }
                if(numLegs > 1){
                    for(int i = 0; i < numLegs-1; i++){
                        double layoverTime = flight.getConnectingLegList().get(i).getArrivalTime().timespan(flight.getConnectingLegList().get(i+1).getDepartureTime());
                        Assert.assertFalse(layoverTime < Saps.MIN_LAYOVER_TIME_HOURS || layoverTime > Saps.MAX_LAYOVER_TIME_HOURS);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testNullSearch() {
        SearchCriteria criteria = new SearchCriteria("BOS","CLE",LocalDate.of(2020,5,8),true);
        SearchOneWayTripFlights search = new SearchOneWayTripFlights(criteria);
        try{
            Assert.assertEquals(0,search.search().size());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
}
