package airport;

import airplane.Airplane;
import airplane.Airplanes;
import dao.ServerAccessException;
import dao.ServerInterface;
import utils.Saps;

/**
 * @author Jackson Powell
 * @since 2020-05-03
 * Responsibilities: Hold a cache of airports to reduce repeated calls to the server for static information
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
            throw new ExceptionInInitializerError();
        }
    }

    AirportCache(
    ) throws ExceptionInInitializerError {
    }

    /**
     * Provide access to an airport from the cache.
     *
     * @see Airports For the function that actually implements this.
     * @pre There is exactly one airport in the server with a given code
     * @param code The 3-letter code of the airport  to get.
     * @return The airport from the cache with the given code.
     */
    public Airport getAirportByCode(String code){
        return airports.getAirportByCode(code);
    }
}
