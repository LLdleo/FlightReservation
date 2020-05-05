
package leg;

import airplane.Airplane;
import airplane.AirplaneCache;
import airplane.Airplanes;
import dao.ServerInterface;
import utils.Saps;

import java.util.Comparator;

/**
 * This class holds values pertaining to a single Connecting Leg. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and 
 * setter methods.
 * 
 * @author Lidian Lin
 * @version 1.0 2020-02-12
 * @since 2020-02-12
 * 
 */
public class ConnectingLeg implements Comparable<ConnectingLeg>, Comparator<ConnectingLeg> {
	
	/*
	 * Airport attributes as defined by the CS509 server interface XML
	 */

	/** Airplane type as an attribute */
	private String mAirplane;
	
	/** Time of the flight in minutes */
	private String mFlightTime;

	/** Flight number either 4 or 5 character */
	private String mNumber;

	/** Departure Info */
	private Departure mDeparture;

	/** Arrival Info */
	private Arrival mArrival;

	/** Seating Info */
	private Seating mSeating;
	
	/**
	 * Default constructor
	 * 
	 * Constructor without params. Requires object fields to be explicitly
	 * set using setter methods
	 * 
	 * @pre None
	 * @post member attributes are initialized to invalid default values
	 */	
	public ConnectingLeg() {
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
	public ConnectingLeg(String airplane, String flightTime, String number, String departureCode, String departureTime,
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
		if (!isValidTime(departureTime))
			throw new IllegalArgumentException(departureTime);
		if (!isValidCode(arrivalCode))
			throw new IllegalArgumentException(arrivalCode);
		if (!isValidTime(arrivalTime))
			throw new IllegalArgumentException(arrivalTime);
		if (!isValidPrice(firstClassPrice))
			throw new IllegalArgumentException(firstClassPrice);
		if (!isValidNumberReserved(firstClassReserved))
			throw new IllegalArgumentException(String.valueOf(firstClassReserved));
		if (!isValidPrice(coachPrice))
			throw new IllegalArgumentException(coachPrice);
		if (!isValidNumberReserved(coachReserved))
			throw new IllegalArgumentException(String.valueOf(coachReserved));

		mAirplane = airplane;
		mFlightTime = flightTime;
		mNumber = number;
		mDeparture = new Departure(departureCode,departureTime);
		mArrival = new Arrival(arrivalCode,arrivalTime);
		mSeating = new Seating(firstClassPrice,firstClassReserved,coachPrice,coachReserved);
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
	public ConnectingLeg(String airplane, String flightTime, String number, String departureCode, String departureTime,
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
		if (!isValidTime(departureTime))
			throw new IllegalArgumentException(departureTime);
		if (!isValidCode(arrivalCode))
			throw new IllegalArgumentException(arrivalCode);
		if (!isValidTime(arrivalTime))
			throw new IllegalArgumentException(arrivalTime);
		if (!isValidPrice(firstClassPrice))
			throw new IllegalArgumentException(firstClassPrice);
		if (!isValidNumberReserved(tmpFirstClassReserved))
			throw new IllegalArgumentException(firstClassReserved);
		if (!isValidPrice(coachPrice))
			throw new IllegalArgumentException(coachPrice);
		if (!isValidNumberReserved(tmpCoachReserved))
			throw new IllegalArgumentException(coachReserved);

		mAirplane = airplane;
		mFlightTime = flightTime;
		mNumber = number;
		mDeparture = new Departure(departureCode,departureTime);
		mArrival = new Arrival(arrivalCode,arrivalTime);
		mSeating = new Seating(firstClassPrice,tmpFirstClassReserved,coachPrice,tmpCoachReserved);
	}

	/**
	 * Convert object to printable string of format "Code, (lat, lon), Name"
	 * 
	 * @return the object formatted as String to display
	 */
	public String toString() {
		return mAirplane + ", " + mFlightTime + ", " + mNumber + ", "
				+ mDeparture.code + ", " + mDeparture.time + ", " +
				mArrival.code + ", " + mArrival.time + ", " +
				mSeating.firstClassPrice + ", " + mSeating.firstClassReserved + ", " +
				mSeating.coachPrice + ", " + mSeating.coachReserved;
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

	/**
	 * Get the String representation of this leg's flight time.
	 *
	 * @return the string representation of this leg's flight time.
	 */
	public String flightTime() {
		return mFlightTime;
	}

	/**
	 * Set the flight time for this connecting leg.
	 *
	 * @param flightTime The flight time to set.
	 * @throws IllegalArgumentException If flight time is not valid
	 */
	public void flightTime(String flightTime) throws IllegalArgumentException {
		if (isValidFlightTime(flightTime))
			mFlightTime = flightTime;
		else
			throw new IllegalArgumentException (flightTime);
	}

	/**
	 * Get the identifying number for this connecting leg.
	 *
	 * @return the identifying number for this connecting leg.
	 */
	public String number() {
		return mNumber;
	}

	/**
	 * Set the identifying number for this connecting leg.
	 *
	 * @param number The number to set as the identifier for this connecting leg.
	 * @throws IllegalArgumentException If the given number is not valid.
	 */
	public void number(String number) throws IllegalArgumentException{
		if (isValidNumber(number))
			mNumber = number;
		else
			throw new IllegalArgumentException (number);
	}

	/**
	 * Get the departure event information for this connecting leg.
	 *
	 * @return the departure event information for this connecting leg.
	 */
	public Departure departure() {
		return mDeparture;
	}

	/**
	 * Set the departure event info for this connecting leg.
	 *
	 * @param departure the departure event to set.
	 */
	public void departure(Departure departure) {
		mDeparture = departure;
	}

	/**
	 * Get the arrival event information for this connecting leg.
	 *
	 * @return the arrival event information for this connecting leg.
	 */
	public Arrival arrival() {
		return mArrival;
	}

	/**
	 * Set the arrival event information for this connecting leg.
	 *
	 * @param arrival the arrival event information for this connecting leg.
	 */
	public void arrival(Arrival arrival) {
		mArrival = arrival;
	}

	/**
	 * Get the seating information for this connecting leg.
	 *
	 * @return the seating information for this connecting leg.
	 */
	public Seating seating() {
		return mSeating;
	}

	/**
	 * Set the seating information for this connecting leg.
	 *
	 * @param seating the seating information to set for this connecting leg.
	 */
	public void seating(Seating seating) {
		mSeating = seating;
	}

	/**
	 * Compare two airports based on 3 character code
	 * 
	 * This implementation delegates to the case insensitive version of string compareTo
	 * @return results of String.compareToIgnoreCase
	 */
	public int compareTo(ConnectingLeg other) {
		return this.mNumber.compareToIgnoreCase(other.mNumber);
	}
	
	/**
	 * Compare two airports alphabetically for sorting, ordering
	 * 
	 * Delegates to airport1.compareTo for ordering by 3 character code
	 * 
	 * @param connectingLeg1 the first airport for comparison
	 * @param connectingLeg2 the second / other airport for comparison
	 * @return -1 if airport1  less than airport2, +1 if airport1 greater than airport2, zero ==
	 */
	public int compare(ConnectingLeg connectingLeg1, ConnectingLeg connectingLeg2) {
		return connectingLeg1.compareTo(connectingLeg2);
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
		if (!(obj instanceof ConnectingLeg))
			return false;
		
		// if all fields are equal, the Airports are the same
		ConnectingLeg rhs = (ConnectingLeg) obj;
		return ((rhs.mAirplane.equalsIgnoreCase(mAirplane)) &&
				(rhs.mFlightTime.equalsIgnoreCase(mFlightTime)) &&
				(rhs.mNumber.equalsIgnoreCase(mNumber)) &&
				(rhs.mDeparture.code.equalsIgnoreCase(mDeparture.code)) &&
				(rhs.mDeparture.time.equalsIgnoreCase(mDeparture.time)) &&
				(rhs.mArrival.code.equalsIgnoreCase(mArrival.code)) &&
				(rhs.mArrival.time.equalsIgnoreCase(mArrival.time)));
	}
	
	/**
	 * Determine if object instance has valid attribute data
	 * 
	 * Verifies the airplane model is not null and not an empty string.
	 * Verifies number is 4 or 5 characters in length.
	 * Verifies flight time is not null and not an empty string.
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

		// If we don't have a 4 or 5 character code, object isn't valid
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

	/**
	 * Check for invalid string.
	 *
	 * @param name is the string to validate.
	 * @return false if null or empty, else assume valid and return true.
	 */
	public boolean isValidName (String name) {
		// If the name is null or empty it can't be valid
		return (name != null) && (!name.equals(""));
	}
	
	/**
	 * Check for invalid airplane model name.
	 * 
	 * @param airplane is the name of the airplane model to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidAirplane(String airplane) {
		return isValidName(airplane);
	}

	/**
	 * Check for invalid flight time.
	 *
	 * @param flightTime is the string representation of the flight time to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidFlightTime(String flightTime) {
		return isValidName(flightTime);
	}

	/**
	 * Check for invalid gmt time.
	 *
	 * @param time is the string representation of the gmt time to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidTime(String time) {
		return isValidName(time);
	}

	/**
	 * Check for invalid price.
	 *
	 * @param price is the string representation of a seat's price to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidPrice(String price) {
		return isValidName(price);
	}

	/**
	 * Check for invalid number of reserved seats.
	 *
	 * @param reserved is the number of seats reserved for a certain seat type.
	 * @return false if null or negative, else assume valid and return true
	 */
	public boolean isValidNumberReserved(Integer reserved) {
		return (reserved != null) && (reserved >= 0);
	}

	/**
	 * Check for invalid identifying number.
	 *
	 * @param number is the name of the airport to validate
	 * @return false if null or is not 4 or 5 characters, else assume valid and return true
	 */
	public boolean isValidNumber(String number) {
		// If we don't have a 4 or 5 character number it can't be valid valid
		return (number != null) && (number.length() >= 4) && (number.length() <= 5);
	}

	/**
	 * Check if this leg has any seats that could still be reserved (coach or first class)
	 *
	 * @see SeatTypeEnum SeatTypeEnum for the possible seats that could be reserved that this function checks.
	 * @pre This is a valid leg with a valid airplane model which has associated information in the WPI server. This leg has recent reservation information updated for accurate results.
	 * @inv Server flight information is only accessed, not modified during this function
	 * @post It is determined whether this leg could be included in a set of available flights that could be reserved.
	 * @return True if there is still at least one seat of any seat type that can be reserved, false otherwise.
	 */
	public boolean hasAnySeatsLeft(){
		Airplane airplane = AirplaneCache.INSTANCE.getAirplaneByModel(this.airplane());
		return this.mSeating.coachReserved < airplane.coachSeats() || this.mSeating.firstClassReserved < airplane.firstClassSeats();
	}

}
