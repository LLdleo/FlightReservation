package airplane;

import dao.ServerAccessException;
import dao.ServerInterface;
import utils.Saps;

import java.io.IOException;

/**
 * @author Jackson Powell
 * @since 2020-05-03
 * Responsibilities: Hold a cache of airplanes to reduce repeated calls to the server for static information
 */
public enum AirplaneCache {
    INSTANCE;
    /**
     * airplanes is the cache of the list of airplanes.
     */
    private Airplanes airplanes;
    {
        try {
            airplanes = ServerInterface.INSTANCE.getAirplanes(Saps.TEAMNAME);
        } catch (ServerAccessException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }

    AirplaneCache(
    ) throws ExceptionInInitializerError {
    }

    /**
     * Provide access to an airplane from the cache.
     *
     * @see Airplanes For the function that actually implemenets this.
     * @pre There is exactly one airplane in the server with a given model
     * @param model The model of the airplane to get.
     * @return The airplane from the cache with the given model.
     */
    public Airplane getAirplaneByModel(String model){
        return airplanes.getAirplaneByModel(model);
    }
}
