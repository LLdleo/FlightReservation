
package airplane;

import leg.SeatTypeEnum;
import utils.Saps;

import java.util.Comparator;

/**
 * This class holds values pertaining to a single Airport. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and 
 * setter methods.
 * 
 * @author Lidian Lin, Jackson Powell
 * @version 1.0
 * @since 2020-04-22
 * 
 */
public class Airplane implements Comparable<Airplane>, Comparator<Airplane> {

	/*
	 * Airport attributes as defined by the CS509 server interface XML
	 */
	/** Name of company that manufactures the airplane as an attribute */
	private String mManufacturer;

	/** Model of the airplane as attribute */
	private String mModel;

	/** Maximum number of first-class seats as integer */
	private Integer mFirstClassSeats;

	/** Maximum number of coach (economy) seats as integer */
	private Integer mCoachSeats;

	/**
	 * Default constructor
	 *
	 * Constructor without params. Requires object fields to be explicitly
	 * set using setter methods
	 *
	 * @pre None
	 * @post member attributes are initialized to invalid default values
	 */
	public Airplane () {
		mManufacturer = "";
		mModel = "";
		mFirstClassSeats = 0;
		mCoachSeats = 0;
	}

	/**
	 * Initializing constructor.
	 *
	 * All attributes are initialized with specified input values following validation for reasonableness.
	 *
	 * @param manufacturer The human readable name of the airplane
	 * @param model The model of the airplane
	 * @param firstClassSeats The number
	 * @param coachSeats The east/west coordinate of the airport
	 *
	 * @pre manufacturer and model are not empty, firstClassSeats and coachSeats are positive whole numbers
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException if any parameter is determined invalid
	 */
	public Airplane(String manufacturer, String model, Integer firstClassSeats, Integer coachSeats) {
		if (!isValidString(manufacturer))
			throw new IllegalArgumentException(manufacturer);
		if (!isValidString(model))
			throw new IllegalArgumentException(model);
		if (firstClassSeats <= 0)
			throw new IllegalArgumentException(Integer.toString(firstClassSeats));
		if (coachSeats <= 0)
			throw new IllegalArgumentException(Integer.toString(coachSeats));

		mManufacturer = manufacturer;
		mModel = model;
		mFirstClassSeats = firstClassSeats;
		mCoachSeats = coachSeats;
	}

	/**
	 * Initializing constructor with all params as type String. Converts latitude and coachSeats
	 * values to required double format before delegating to ctor.
	 *
	 * @param manufacturer The human readable name of the airport
	 * @param model The 3 letter code for the airport
	 * @param firstClassSeats is the string representation of firstClassSeats in integer format
	 * @param coachSeats is the String representation of the coachSeats in integer format
	 *
	 * @pre the firstClassSeats and coachSeats are valid String representations of valid firstClass/coach values
	 * @post member attributes are initialized with input parameter values
	 * @throws IllegalArgumentException is any parameter is invalid
	 */
	public Airplane(String manufacturer, String model, String firstClassSeats, String coachSeats) {
		int tmpFirstClassSeats, tmpCoachSeats;
		try {
			tmpFirstClassSeats = Integer.parseInt(firstClassSeats);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("First Class Seats must be a positive whole number", ex);
		}
		
		try {
			tmpCoachSeats = Integer.parseInt(coachSeats);
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException ("Coach Seats must be a positive whole number", ex);
		}
		
		// validate converted values
		if (!isValidString(manufacturer))
			throw new IllegalArgumentException(manufacturer);
		if (!isValidString(model))
			throw new IllegalArgumentException(model);
		if (tmpFirstClassSeats <= 0)
			throw new IllegalArgumentException(firstClassSeats);
		if (tmpCoachSeats <= 0)
			throw new IllegalArgumentException(coachSeats);

		mManufacturer = manufacturer;
		mModel = model;
		mFirstClassSeats = tmpFirstClassSeats;
		mCoachSeats = tmpCoachSeats;
	}

	/**
	 * Convert object to printable string of format "Manufacturer, Model, (FirstClassSeats, CoachSeats)"
	 * 
	 * @return the object formatted as String to display
	 */
	public String toString() {

		return mManufacturer + "," +
				mModel + ", " +
				"(" + String.format("%d", mFirstClassSeats) + ", " +
				String.format("%d", mCoachSeats) + ")";
	}
	
	/**
	 * Set the airplane name
	 * 
	 * @param manufacturer The human readable name of the airport
	 * @throws IllegalArgumentException if name is invalid
	 */
	public void manufacturer (String manufacturer) throws IllegalArgumentException{
		if (isValidString(manufacturer))
			mManufacturer = manufacturer;
		else
			throw new IllegalArgumentException (manufacturer);
	}
	
	/**
	 * get the airplane manufacturer
	 * 
	 * @return Airplane manufacturer
	 */
	public String manufacturer() {
		return mManufacturer;
	}
	
	/**
	 * set the airport 3 letter code
	 * 
	 * @param model The 3 letter code for the airport
	 * @throws IllegalArgumentException if code is invalid
	 */
	public void model(String model) throws IllegalArgumentException{
		if (isValidString(model))
			mModel = model;
		else
			throw new IllegalArgumentException (model);
	}
	
	/**
	 * Get the airplane model
	 * 
	 * @return The airplane model
	 */
	public String model() {
		return mModel;
	}
	
	/**
	 * Set the firstClassSeats for the airplane
	 * 
	 * @param firstClassSeats The max number of first class seats on a certain airplane.
	 * @throws IllegalArgumentException if firstClassSeats is invalid, less than or equal to 0
	 */
	public void firstClassSeats(Integer firstClassSeats) throws IllegalArgumentException{
		if (firstClassSeats > 0)
			mFirstClassSeats = firstClassSeats;
		else
			throw new IllegalArgumentException (Double.toString(firstClassSeats));
	}
	/**
	 * Set the firstClassSeats for the airplane
	 *
	 * @param firstClassSeats The max number of first class seats on a certain airplane.
	 * @throws IllegalArgumentException if firstClassSeats is invalid, less than or equal to 0
	 */
	public void firstClassSeats(String firstClassSeats) throws IllegalArgumentException{
		try {
			int tmp = Integer.parseInt(firstClassSeats);
			if (tmp > 0) {
				mFirstClassSeats = tmp;
			}
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException (firstClassSeats);
		}
	}
	
	/**
	 * Get the firstClassSeats for the airplane
	 * 
	 * @return The firstClassSeats for the airplane
	 */
	public Integer firstClassSeats() {
		return mFirstClassSeats;
	}

	/**
	 * Set the coachSeats for the airplane
	 *
	 * @param coachSeats The maximum number of coach seats on this airplane.
	 * @throws IllegalArgumentException if coachSeats is invalid, less than or equal to 0
	 */
	public void coachSeats(Integer coachSeats) {
		if (coachSeats > 0)
			mCoachSeats = coachSeats;
		else
			throw new IllegalArgumentException (Double.toString(coachSeats));
	}
	/**
	 * Set the coachSeats for the airplane
	 *
	 * @param coachSeats The maximum number of coach seats on this airplane.
	 * @throws IllegalArgumentException if coachSeats is invalid, less than or equal to 0
	 */
	public void coachSeats(String coachSeats) {
		try {
			int tmp = Integer.parseInt(coachSeats);
			if (tmp > 0) {
				mCoachSeats = tmp;
			}
		} catch (NullPointerException | NumberFormatException ex) {
			throw new IllegalArgumentException (coachSeats);
		}
	}

	/**
	 * Get the manufacturer of the airplane
	 *
	 * @return the manufacturer of the airplane
	 */
	public String getmManufacturer() {
		return mManufacturer;
	}

	/**
	 * Get the model of the airplane.
	 *
	 * @return the model of the airplane.
	 */
	public String getmModel() {
		return mModel;
	}

	/**
	 * Get the maximum number of first class seats on the airplane
	 *
	 * @return the maximum number of first class seats on the airplane
	 */
	public Integer getmFirstClassSeats() {
		return mFirstClassSeats;
	}

	/**
	 * Get the maximum number of coach seats on the airplane.
	 *
	 * @return the maximum number of coach seats on the airplane.
	 */
	public Integer getmCoachSeats() {
		return mCoachSeats;
	}

	/**
	 * Get the coachSeats for the airplane
	 *
	 * @return The coachSeats for the airplane
	 */
	public Integer coachSeats() {
		return mCoachSeats;
	}

	/**
	 * Compare two airports based on 3 character code
	 * 
	 * This implementation delegates to the case insensitive version of string compareTo
	 * @return results of String.compareToIgnoreCase
	 */
	public int compareTo(Airplane other) {
		return (this.mManufacturer.compareToIgnoreCase(other.mManufacturer) & this.mModel.compareToIgnoreCase(other.mModel));
	}
	
	/**
	 * Compare two airports alphabetically for sorting, ordering
	 * 
	 * Delegates to airplane1.compareTo for ordering by manufacturer and model
	 * 
	 * @param airplane1 the first airport for comparison
	 * @param airplane2 the second / other airport for comparison
	 * @return -1 if airport1  less than airport2, +1 if airport1 greater than airport2, zero ==
	 */
	public int compare(Airplane airplane1, Airplane airplane2) {
		return airplane1.compareTo(airplane2);
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
		if (!(obj instanceof Airplane))
			return false;
		
		// if all fields are equal, the Airports are the same
		Airplane rhs = (Airplane) obj;
		return (rhs.mManufacturer.equalsIgnoreCase(mManufacturer)) &&
				(rhs.mModel.equalsIgnoreCase(mModel)) &&
				(rhs.mFirstClassSeats.equals(mFirstClassSeats)) &&
				(rhs.mCoachSeats.equals(mCoachSeats));
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
		
		// If the manufacturer name isn't valid, the object isn't valid
		if ((mManufacturer == null) || (mManufacturer.equals("")))
			return false;
		
		// If the model isn't valid, object isn't valid
		if ((mModel == null) || (mModel.equals("")))
			return false;
		
		// Verify that the max number of seats is non-negative
		return ((mFirstClassSeats >= 0) && (mCoachSeats >= 0));
	}
	
	/**
	 * Check for invalid airport name.
	 * 
	 * @param string is the string to validate
	 * @return false if null or empty string, else assume valid and return true
	 */
	public boolean isValidString(String string) {
		// If the name is null or empty it can't be valid
		return (string != null) && (!string.equals(""));
	}

	/**
	 * Get the number of seats of a certain seatType on this airplane.
	 *
	 * @param seatType The type of seat to check for the number of seats.
	 * @return The number of seats of a certain seatType on this airplane.
	 */
	public int getNumSeats(SeatTypeEnum seatType){
		return seatType == SeatTypeEnum.FIRSTCLASS ? firstClassSeats() : coachSeats();
	}

}
