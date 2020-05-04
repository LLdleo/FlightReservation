package airplane;

import dao.ServerAccessException;
import dao.ServerInterface;
import utils.Saps;

import java.io.IOException;

/**
 * @author Jackson Powell
 * @since 2020-05-03
 * Responsibilities: Hold a cache of airplanes to reduce repeated calls to the server for static information
 * Significant associations: Airplanes and Airplane since this is just a cache for those objects and needs to know how to lookup an airplane.
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

    /**
     * Singleton constructor that throws an initialization error if there was a ServerAccessException when getting the list of airplanes.
     *
     * @throws ExceptionInInitializerError When there is a ServerAccessException when trying to populate the cache.
     */
    AirplaneCache(
    ) throws ExceptionInInitializerError {
    }

    /**
     * Provide access to an airplane from the cache.
     *
     * @see Airplanes Airplanes::getAirplaneByModel for the function that this delegates to.
     * @pre There is exactly one airplane in the server with a given model
     * @inv The cache remains unchanged during throughout this method.
     * @param model The model of the airplane to get.
     * @return The airplane from the cache with the given model.
     */
    public Airplane getAirplaneByModel(String model){
        return airplanes.getAirplaneByModel(model);
    }
}
