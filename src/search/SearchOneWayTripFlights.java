package search;

import flight.Flights;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jackson Powell
 * @since 2020-05-01
 * Responsibilities: Retrieve one-way flights matching search criteria
 */
public class SearchOneWayTripFlights {
    /**
     * input is the search criteria that the available flights are based on.
     */
    private SearchCriteria input;
    /**
     * availableFlights is the list of flights matching the input search criteria.
     */
    private List<Flight> availableFlights;

    /**
     * Constructor for SearchOneWayTripFlights control object given the criteria to search on.
     *
     * @pre input is already validated.
     * @post A SearchOneWayTripFlights object is instantiated that can now be called any number of times to refresh matching search results.
     * @param input The search criteria that the search should be based on.
     */
    public SearchOneWayTripFlights(SearchCriteria input){
        this.input = input;
        this.availableFlights = new ArrayList<>();
    }

    /**
     * Search for flights that meet the input search criteria.
     *
     * @post Refreshes the list of available flights with the latest search results.
     * @return a list of flights that meet the input search criteria.
     */
    public List<search.Flight> search(){
        this.availableFlights = new ArrayList<>();
        CreatePossibleFlights flightCreater = new CreatePossibleFlights(this.input);
        Flights flights = flightCreater.createPossibleConnectingLegCombinations();
        for(leg.Flight flight : flights){
            this.availableFlights.add(new search.Flight(flight));
        }
        return this.availableFlights;
    }

}
