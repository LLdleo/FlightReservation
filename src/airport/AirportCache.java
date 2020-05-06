package airport;

import airplane.Airplane;
import airplane.Airplanes;
import dao.ServerAccessException;
import dao.ServerInterface;
import utils.Saps;

/**
 * Hold a cache of airports to reduce repeated calls to the server for static information
 * Significant associations: Airport for the object being cached and retrieved and Airports for the cache's data structure.
 *
 * @author Jackson Powell
 * @since 2020-05-03
 */
public enum AirportCache {
    INSTANCE;
    /**
     * airports is the cache of the list of airports.
     */
    private Airports airports;
    {
        try {
            airports = ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME);
        } catch (ServerAccessException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(Saps.CONNECTION_EXCEPTION_MESSAGE);
        }
    }
    /**
     * Singleton constructor that throws an initialization error if there was a ServerAccessException when getting the list of airports.
     *
     * @throws ExceptionInInitializerError When there is a ServerAccessException when trying to populate the cache.
     */
    AirportCache(
    ) throws ExceptionInInitializerError {
    }

    /**
     * Provide access to an airport from the cache.
     *
     * @see Airports Airports::getAirportByCode for the function that this function delegates to.
     * @pre There is exactly one airport in the server with a given code
     * @inv The cache remains unchanged throughout this function.
     * @param code The 3-letter code of the airport  to get.
     * @return The airport from the cache with the given code.
     */
    public Airport getAirportByCode(String code){
        return airports.getAirportByCode(code);
    }
}
