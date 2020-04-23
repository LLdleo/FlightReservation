/**
 * 
 */
package airport;

import java.util.ArrayList;

/**
 * This class aggregates a number of Airport. The aggregate is implemented as an ArrayList.
 * Airports can be added to the aggregate using the ArrayList interface. Objects can 
 * be removed from the collection using the ArrayList interface.
 * 
 * @author blake, Jackson Powell
 * @version 1.0
 * @since 2016-02-24
 *
 */
public class Airports extends ArrayList<Airport> {
	private static final long serialVersionUID = 1L;

	/**
	 * Get an airport from a list of airports using the code.
	 *
	 * @param airportCode The 3-letter code of the airport to get.
	 * @return The airport if found, null otherwise.
	 */
	public Airport getAirportByCode(String airportCode){
		for(Airport air: this){
			if (air.code().equalsIgnoreCase(airportCode)){
				return air;
			}
		}
		return null;
	}
}
