/**
 * 
 */
package utils;

/**
 * @author blake, Jackson Powell
 * @version 1.2
 * @since 2016-02-24
 *
 */
public class QueryFactory {
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of airports
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getAirports(String teamName) {
		return "?team=" + teamName + "&action=list&list_type=airports";
	}

	/**
	 * Return a query string that can be passed to HTTP URL to request list of airports
	 *
	 * @param teamName is the name of the team to specify the data copy on server
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getAirplanes(String teamName) {
		return "?team=" + teamName + "&action=list&list_type=airplanes";
	}

	/**
	 * Return a query string that can be passed to HTTP URL to request list of legs
	 *
	 * @param teamName is the name of the team to specify the data copy on server
	 * @param listType is either departing or arriving
	 * @param airportCode is the code of departing or arriving airport
	 * @param day is day of departing or arriving
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getLegs(String teamName, String listType, String airportCode, String day) {
		return "?team=" + teamName + "&action=list&list_type=" + listType + "&airport=" + airportCode + "&day=" + day;
	}

	/**
	 * Return a query string that can be passed to HTTP URL to request list of airports
	 *
	 * @param teamName is the name of the team to specify the data copy on server
	 * @param xmlFlights is the xml string of the flights that is going to reserve
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String reserve(String teamName, String xmlFlights) {
		return "?team=" + teamName + "&action=buyTickets&flightData=" + xmlFlights;
	}

	/**
	 * Lock the server database so updates can be written
	 * 
	 * @param teamName is the name of the team to acquire the lock
	 * @return the String written to HTTP POST to lock server database 
	 */
	public static String lock (String teamName) {
		return "team=" + teamName + "&action=lockDB";
	}
	
	/**
	 * Unlock the server database after updates are written
	 * 
	 * @param teamName is the name of the team holding the lock
	 * @return the String written to the HTTP POST to unlock server database
	 */
	public static String unlock (String teamName) {
		return "team=" + teamName + "&action=unlockDB";
	}

	/**
	 * Get the timezone offset to convert to/from local time
	 *
	 * @param latitude The latitude of the airport to get the timezone offset for
	 * @param longitude The longitude of the airport to get the timezone offset for
	 * @return The query string which can be appended to URL to form HTTP GET request
	 */
	public static String getTimezoneOffset(double latitude, double longitude){
		return "&lat=" + latitude + "&long=" + longitude;
	}

	/**
	 * Reset the server database after updates are written
	 *
	 * @param teamName is the name of the team handling the database
	 * @return the String written to the HTTP POST to reset server database
	 */
	public static String resetDB (String teamName) {
		return "?team=" + teamName + "&action=resetDB";
	}

}
