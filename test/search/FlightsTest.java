package search;

import leg.SeatTypeEnum;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Test filtering and sorting functionality on a list of flights.
 *
 * @author Jackson Powell
 * @since 2020-05-08
 */
public class FlightsTest {
    /**
     * sampleFlights is the list of flights searched that is being sorted and filtered
     */
    private static Flights sampleFlights;
    /**
     * oldSize is the initial size of sampleFlights for checking the invariant that the size of this flight list does not change.
     */
    private static int oldSize;

    /**
     * Conduct a search to get the sample list of flights to test sorting and filtering.
     * @throws Exception If there is an exception thrown when searching.
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
        SearchCriteria criteria = new SearchCriteria("CLE", "BOS", LocalDate.of(2020, 5, 10), true);
        SearchOneWayTripFlights flightSearcher = new SearchOneWayTripFlights(criteria);
        sampleFlights = flightSearcher.search();
        oldSize = sampleFlights.size();
    }

    /**
     * Test sorting in ascending and descending order.
     */
    @Test
    public void testSortingDepartureTimes() {
        sampleFlights.sort(SortTypeEnum.DEP_TIME,true);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getDepartureTime().getLocalTime().isBefore(sampleFlights.get(i+1).getDepartureTime().getLocalTime())
            || sampleFlights.get(i).getDepartureTime().getLocalTime().isEqual(sampleFlights.get(i+1).getDepartureTime().getLocalTime()));
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        sampleFlights.sort(SortTypeEnum.DEP_TIME,false);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getDepartureTime().getLocalTime().isAfter(sampleFlights.get(i+1).getDepartureTime().getLocalTime())
                    || sampleFlights.get(i).getDepartureTime().getLocalTime().isEqual(sampleFlights.get(i+1).getDepartureTime().getLocalTime()));
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
    }

    /**
     * Test sorting by arrival time in ascending and descending order.
     */
    @Test
    public void testSortingArrivalTime() {
        sampleFlights.sort(SortTypeEnum.ARR_TIME,true);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getArrivalTime().getLocalTime().isBefore(sampleFlights.get(i+1).getArrivalTime().getLocalTime())
                    || sampleFlights.get(i).getArrivalTime().getLocalTime().isEqual(sampleFlights.get(i+1).getArrivalTime().getLocalTime()));
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        sampleFlights.sort(SortTypeEnum.ARR_TIME,false);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getArrivalTime().getLocalTime().isAfter(sampleFlights.get(i+1).getArrivalTime().getLocalTime())
                    || sampleFlights.get(i).getArrivalTime().getLocalTime().isEqual(sampleFlights.get(i+1).getArrivalTime().getLocalTime()));
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
    }

    /**
     * Test sorting by travel time in ascending and descending order.
     */
    @Test
    public void testSortingTravelTime() {
        sampleFlights.sort(SortTypeEnum.TRAVEL_TIME,true);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getTravelTime() <= sampleFlights.get(i+1).getTravelTime());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        sampleFlights.sort(SortTypeEnum.TRAVEL_TIME,false);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getTravelTime() >= sampleFlights.get(i+1).getTravelTime());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
    }

    /**
     * Test sorting by coach price in ascending and descending order.
     */
    @Test
    public void testSortingCoachPrice() {
        sampleFlights.sort(SortTypeEnum.COACH_PRICE,true);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getCoachPrice() <= sampleFlights.get(i+1).getCoachPrice());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        sampleFlights.sort(SortTypeEnum.COACH_PRICE,false);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getCoachPrice() >= sampleFlights.get(i+1).getCoachPrice());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
    }

    /**
     * Test sorting by first class price in ascending and descending order.
     */
    @Test
    public void testSortingFirstClassPrice() {
        sampleFlights.sort(SortTypeEnum.FIRST_CLASS_PRICE,true);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getFirstClassPrice() <= sampleFlights.get(i+1).getFirstClassPrice());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        sampleFlights.sort(SortTypeEnum.FIRST_CLASS_PRICE,false);
        for(int i = 0; i < sampleFlights.size()-1; i++){
            Assert.assertTrue(sampleFlights.get(i).getFirstClassPrice() >= sampleFlights.get(i+1).getFirstClassPrice());
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
    }

    /**
     * Test filtering flights by seat type and time windows.
     */
    @Test
    public void testFiltering() {
        LocalTime startDeparture = LocalTime.of(4,12);
        LocalTime startArrival = LocalTime.of(8,10);
        LocalTime endDeparture = LocalTime.of(8,10);
        LocalTime endArrival = LocalTime.of(9,20);
        FilterCriteria criteria = new FilterCriteria(SeatTypeEnum.FIRSTCLASS,startDeparture,endDeparture,startArrival,endArrival);
        Flights filteredFLights = sampleFlights.filter(criteria);
        for(Flight flight: filteredFLights){
            Assert.assertTrue(flight.allSeatsAvailable(criteria.seatType));
            Assert.assertTrue(flight.inRange(startArrival,endArrival,false));
            Assert.assertTrue(flight.inRange(startDeparture,endDeparture,true));
        }
        Assert.assertEquals(oldSize,sampleFlights.size());
        criteria = new FilterCriteria(SeatTypeEnum.COACH,endArrival,startArrival,startDeparture,endDeparture);
        filteredFLights = sampleFlights.filter(criteria);
        Assert.assertEquals(0,filteredFLights.size());
        Assert.assertEquals(oldSize,sampleFlights.size());
    }
}
