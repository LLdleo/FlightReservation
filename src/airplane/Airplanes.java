package airplane;

import airport.Airport;

import java.util.ArrayList;

/**
 * This class aggregates a number of Airplanes. The aggregate is implemented as an ArrayList.
 * Airplanes can be added to the aggregate using the ArrayList interface. Objects can
 * be removed from the collection using the ArrayList interface.
 *
 * @author Lidian Lin, Jackson Powell
 * @version 1.0
 * @since 2020-04-24
 * Significant associations: Airplane and ArrayList for the contained objects and functionality.
 */

public class Airplanes extends ArrayList<Airplane> {
    private static final long serialVersionUID = 1L;
    /**
     * Get an airplane from a list of airplanes using the model.
     *
     * @pre There is exactly one airplane with the given model given
     * @inv The list of airplanes remains unchanged during throughout this method.
     * @param airplaneModel The model of the airplane to get.
     * @return The airplane if found, null otherwise.
     */
    public Airplane getAirplaneByModel(String airplaneModel){
        for(Airplane air: this){
            if (air.model().equalsIgnoreCase(airplaneModel)){
                return air;
            }
        }
        return null;
    }
}
