/**
 * 
 */
package flight;

import java.util.Comparator;

/**
 * This class holds values pertaining to a single Airport. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and 
 * setter methods.
 * 
 * @author PoLYmer
 * @version 1.0 2020-02-12
 * @since 2020-02-12
 * 
 */
public class Flight implements Comparable<Flight>, Comparator<Flight> {
	
	/**
	 * Airport attributes as defined by the CS509 server interface XML
	 */

	/** Airplane type as an attribute */
	private String mAirplane;
	
	/** Time of the flight in minutes */
	private String mFlightTime;

	/** Flight number either 4 or 5 character */
	private String mNumber;

	/** Departure Info */
	private Departure mDeparture = new Departure();

	/** Arrival Info */
	private Arrival mArrival = new Arrival();

	/** Seating Info */
	private Seating mSeating = new Seating();
	
	/**
	 * Default constructor
	 * 
	 * Constructor without params. Requires object fields to be explicitly
	 * set using setter methods
	 * 
	 * @pre None
	 * @post member attributes are initialized to invalid default values
	 */	
	public Flight() {
		mAirplane = "";
		mFlightTime = "";
		mNumber = "";
	}
	
	/**
	 * Initializing constructor.
	 * 
	 * All attributes are initialized with specified input values following validation for reasonableness.
	 *  
	 * @param airplane The human readable name of the airport
	 * @param flightTime Time of the flight in minutes
	 * @param number Flight number either 4 or 5 characters
	 * @param departureCode The 3 letter code for the departure airport
	 * @param departureTime Date and time of departure in GMT
	 * @param arrivalCode The 3 letter code for the arrival airport
	 * @param arrivalTime Date and time of arrival in GMT
	 * @param firstClassPrice First class price as attribute
	 * @param firstClassReserved Number of first class seats already reserved
	 * @param coachPrice Coach price as attribute
	 * @param coachReserved Number of coach seats already reserved
	 *
	 * @pre code is a 3 character string, name is not empty, latitude and longitude are valid values
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException if any parameter is determined invalid
	 */
	public Flight(String airplane, String flightTime, String number, String departureCode, String departureTime,
				  String arrivalCode, String arrivalTime, String firstClassPrice, Integer firstClassReserved,
				  String coachPrice, Integer coachReserved) {
		if (!isValidAirplane(airplane))
			throw new IllegalArgumentException(airplane);
		if (!isValidFlightTime(flightTime))
			throw new IllegalArgumentException(flightTime);
		if (!isValidNumber(number))
			throw new IllegalArgumentException(number);
		if (!isValidCode(departureCode))
			throw new IllegalArgumentException(departureCode);
		if (isValidTime(departureTime))
			throw new IllegalArgumentException(departureTime);
		if (!isValidCode(arrivalCode))
			throw new IllegalArgumentException(arrivalCode);
		if (isValidTime(arrivalTime))
			throw new IllegalArgumentException(arrivalTime);
		if (isValidPrice(firstClassPrice))
			throw new IllegalArgumentException(firstClassPrice);
		if (!isValidNumberReserved(firstClassReserved))
			throw new IllegalArgumentException(String.valueOf(firstClassReserved));
		if (isValidPrice(coachPrice))
			throw new IllegalArgumentException(coachPrice);
		if (!isValidNumberReserved(coachReserved))
			throw new IllegalArgumentException(String.valueOf(coachReserved));

		mAirplane = airplane;
		mFlightTime = flightTime;
		mNumber = number;
		mDeparture.code = departureCode;
		mDeparture.time = departureTime;
		mArrival.code = arrivalCode;
		mArrival.time = arrivalTime;
		mSeating.firstClassPrice = firstClassPrice;
		mSeating.firstClassReserved = firstClassReserved;
		mSeating.coachPrice = coachPrice;
		mSeating.coachReserved = coachReserved;
	}
	
	/**
	 * Initializing constructor with all params as type String. Converts latitude and longitude
	 * values to required double format before delegating to ctor.
	 *
	 * @param airplane The human readable name of the airport
	 * @param flightTime Time of the flight in minutes
	 * @param number Flight number either 4 or 5 characters
	 * @param departureCode The 3 letter code for the departure airport
	 * @param departureTime Date and time of departure in GMT
	 * @param arrivalCode The 3 letter code for the arrival airport
	 * @param arrivalTime Date and time of arrival in GMT
	 * @param firstClassPrice First class price as attribute
	 * @param firstClassReserved Number of first class seats already reserved
	 * @param coachPrice Coach price as attribute
	 * @param coachReserved Number of coach seats already reserved
	 * 
	 * @pre the latitude and longitude are valid String representations of valid lat/lon values
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException is any parameter is invalid
	 */
	public Flight(String airplane, String flightTime, String number, String departureCode, String departureTime,
				  String arrivalCode, String arrivalTime, String firstClassPrice, String firstClassReserved,
				  String coachPrice, String coachReserved) {
		Integer tmpFirstClassReserved, tmpCoachReserved;
		try {
			tmpFirstClassReserved = Integer.parseInt(firstClassReserved);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("First Class Reserved number is not in the right format", ex);
		}
		
		try {
			tmpCoachReserved = Integer.parseInt(coachReserved);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("Coach Reserved number is not in the right format", ex);
		}

		if (!isValidAirplane(airplane))
			throw new IllegalArgumentException(airplane);
		if (!isValidFlightTime(flightTime))
			throw new IllegalArgumentException(flightTime);
		if (!isValidNumber(number))
			throw new IllegalArgumentException(number);
		if (!isValidCode(departureCode))
			throw new IllegalArgumentException(departureCode);
		if (isValidTime(departureTime))
			throw new IllegalArgumentException(departureTime);
		if (!isValidCode(arrivalCode))
			throw new IllegalArgumentException(arrivalCode);
		if (isValidTime(arrivalTime))
			throw new IllegalArgumentException(arrivalTime);
		if (isValidPrice(firstClassPrice))
			throw new IllegalArgumentException(firstClassPrice);
		if (!isValidNumberReserved(tmpFirstClassReserved))
			throw new IllegalArgumentException(firstClassReserved);
		if (isValidPrice(coachPrice))
			throw new IllegalArgumentException(coachPrice);
		if (!isValidNumberReserved(tmpCoachReserved))
			throw new IllegalArgumentException(coachReserved);

		mAirplane = airplane;
		mFlightTime = flightTime;
		mNumber = number;
		mDeparture.code = departureCode;
		mDeparture.time = departureTime;
		mArrival.code = arrivalCode;
		mArrival.time = arrivalTime;
		mSeating.firstClassPrice = firstClassPrice;
		mSeating.firstClassReserved = tmpFirstClassReserved;
		mSeating.coachPrice = coachPrice;
		mSeating.coachReserved = tmpCoachReserved;
	}

	/**
	 * Convert object to printable string of format "Code, (lat, lon), Name"
	 * 
	 * @return the object formatted as String to display
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(mAirplane).append(", ");
		sb.append(mFlightTime).append(", ");
		sb.append(mNumber).append(", ");
		sb.append(mDeparture.code).append(", ");
		sb.append(mDeparture.time).append(", ");
		sb.append(mArrival.code).append(", ");
		sb.append(mArrival.time).append(", ");
		sb.append(mSeating.firstClassPrice).append(", ");
		sb.append(mSeating.firstClassReserved).append(", ");
		sb.append(mSeating.coachPrice).append(", ");
		sb.append(mSeating.coachReserved).append(", ");

		return sb.toString();
	}

	/**
	 * Convert object to printable string of format "Code, (lat, lon), Name"
	 *
	 * @return the object formatted as String to display
	 */
	public String toJson() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"Number\":\"").append(mNumber).append("\",");
		sb.append("\"Airplane\":\"").append(mAirplane).append("\",");
		sb.append("\"DepartureCode\":\"").append(mDeparture.code).append("\",");
		sb.append("\"DepartureTime\":\"").append(mDeparture.time).append("\",");
		sb.append("\"ArrivalCode\":\"").append(mArrival.code).append("\",");
		sb.append("\"ArrivalTime\":\"").append(mArrival.time).append("\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * get the airport name
	 *
	 * @return Airport name
	 */
	public String airplane() {
		return mAirplane;
	}

	/**
	 * Set the airport name
	 *
	 * @param airplane The human readable name of the airport
	 * @throws IllegalArgumentException if name is invalid
	 */
	public void airplane(String airplane) {
		if (isValidAirplane(airplane))
			mAirplane = airplane;
		else
			throw new IllegalArgumentException (mAirplane);
	}

	public String flightTime() {
		return mFlightTime;
	}

	public void flightTime(String flightTime) {
		if (isValidFlightTime(flightTime))
			mFlightTime = flightTime;
		else
			throw new IllegalArgumentException (flightTime);
	}

	public String number() {
		return mNumber;
	}

	public void number(String number) {
		if (isValidNumber(number))
			mNumber = number;
		else
			throw new IllegalArgumentException (number);
	}

	public Departure departure() {
		return mDeparture;
	}

	public void departure(Departure departure) {
		mDeparture = departure;
	}

	public Arrival arrival() {
		return mArrival;
	}

	public void arrival(Arrival arrival) {
		mArrival = arrival;
	}

	public Seating seating() {
		return mSeating;
	}

	public void seating(Seating seating) {
		mSeating = seating;
	}

	/**
	 * Compare two airports based on 3 character code
	 * 
	 * This implementation delegates to the case insensitive version of string compareTo
	 * @return results of String.compareToIgnoreCase
	 */
	public int compareTo(Flight other) {
		return this.mNumber.compareToIgnoreCase(other.mNumber);
	}
	
	/**
	 * Compare two airports alphabetically for sorting, ordering
	 * 
	 * Delegates to airport1.compareTo for ordering by 3 character code
	 * 
	 * @param flight1 the first airport for comparison
	 * @param flight2 the second / other airport for comparison
	 * @return -1 if airport1  less than airport2, +1 if airport1 greater than airport2, zero ==
	 */
	public int compare(Flight flight1, Flight flight2) {
		return flight1.compareTo(flight2);
	}
	
	
	/**
	 * Determine if two airport objects are the same airport
	 * 
	 * Compare another object to this airport and return true if the other 
	 * object specifies the same airport as this object. String comparisons are
	 * case insensitive BOS is same as bos
	 * 
	 * @param obj is the object to compare against this object
	 * @return true if the param is the same airport as this, else false
	 */
	@Override
	public boolean equals (Object obj) {
		// every object is equal to itself
		if (obj == this)
			return true;
		
		// null not equal to anything
		if (obj == null)
			return false;
		
		// can't be equal if obj is not an instance of Airport
		if (!(obj instanceof Flight))
			return false;
		
		// if all fields are equal, the Airports are the same
		Flight rhs = (Flight) obj;
		if ((rhs.mAirplane.equalsIgnoreCase(mAirplane)) &&
				(rhs.mFlightTime.equalsIgnoreCase(mFlightTime)) &&
				(rhs.mNumber.equalsIgnoreCase(mNumber)) &&
				(rhs.mDeparture.code.equalsIgnoreCase(mDeparture.code)) &&
				(rhs.mDeparture.time.equalsIgnoreCase(mDeparture.time)) &&
				(rhs.mArrival.code.equalsIgnoreCase(mArrival.code)) &&
				(rhs.mArrival.time.equalsIgnoreCase(mArrival.time)))
		{
			return true;
		}
		
		return false;	
	}
	
	/**
	 * Determine if object instance has valid attribute data
	 * 
	 * Verifies the name is not null and not an empty string. 
	 * Verifies code is 3 characters in length.
	 * Verifies latitude is between +90.0 north pole and -90.0 south pole.
	 * Verifies longitude is between +180.0 east prime meridian and -180.0 west prime meridian.
	 * 
	 * @return true if object passes above validation checks
	 * 
	 */
	public boolean isValid() {

		// If the name isn't valid, the object isn't valid
		if ((mAirplane == null) || (mAirplane.equals("")))
			return false;

		if ((mFlightTime == null) || (mFlightTime.equals("")))
			return false;

		// If we don't have a 3 character code, object isn't valid
		return (mNumber != null) && (mNumber.length() >= 4) && (mNumber.length() <= 5);
	}
	
	/**
	 * Check for invalid 3 character airport code
	 * 
	 * @param code is the airport code to validate
	 * @return false if null or not 3 characters in length, else assume valid and return true
	 */
	public boolean isValidCode (String code) {
		// If we don't have a 3 character code it can't be valid valid
		return (code != null) && (code.length() == 3);
	}

	public boolean isValidName (String name) {
		// If the name is null or empty it can't be valid
		return (name != null) && (!name.equals(""));
	}
	
	/**
	 * Check for invalid airport name.
	 * 
	 * @param airplane is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidAirplane(String airplane) {
		return isValidName(airplane);
	}

	/**
	 * Check for invalid airport name.
	 *
	 * @param flightTime is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidFlightTime(String flightTime) {
		return isValidName(flightTime);
	}

	/**
	 * Check for invalid airport name.
	 *
	 * @param time is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidTime(String time) {
		return isValidName(time);
	}

	/**
	 * Check for invalid airport name.
	 *
	 * @param price is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidPrice(String price) {
		return isValidName(price);
	}

	/**
	 * Check for invalid airport name.
	 *
	 * @param reserved is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidNumberReserved(Integer reserved) {
		// If we don't have a 3 character code it can't be valid valid
		return (reserved != null) && (reserved >= 0);
	}

	/**
	 * Check for invalid airport name.
	 *
	 * @param number is the name of the airport to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidNumber(String number) {
		// If we don't have a 3 character code it can't be valid valid
		return (number != null) && (number.length() >= 4) && (number.length() <= 5);
	}

}
