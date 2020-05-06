
package leg;

import airport.Airport;

import java.util.ArrayList;

/**
 * This class aggregates a number of Airport. The aggregate is implemented as an ArrayList.
 * Airports can be added to the aggregate using the ArrayList interface. Objects can 
 * be removed from the collection using the ArrayList interface.
 * Significant associations: ConnectingLeg for the objects contained and retrieved and arraylist for extended functionality.
 *
 * @author blake, Jackson Powell
 * @version 1.0
 * @since 2016-02-24
 */
public class ConnectingLegs extends ArrayList<ConnectingLeg> {
	private static final long serialVersionUID = 1L;
	/**
	 * Get a connecting leg from a list of connecting leg using the id.
	 *
	 * @pre There is exactly one connecting leg in the server with a given id
	 * @inv The list remains unchanged throughout this function.
	 * @param connectingLegID The id of the connecting leg to get.
	 * @return The connecting leg if found, null otherwise.
	 */
	public ConnectingLeg getLegByID(String connectingLegID){
		for(ConnectingLeg leg: this){
			if (leg.number().equalsIgnoreCase(connectingLegID)){
				return leg;
			}
		}
		return null;
	}
	
}
