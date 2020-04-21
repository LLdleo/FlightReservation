/**
 * 
 */
package utils;

/**
 * @author blake
 * 
 * System Adaptive Parameters (SAPS)
 * 
 * Constants and values for limits, bounds and configuration of system
 *
 */
public class Saps {
	/**
	 * Constant values used for latitude and longitude range validation
	 */
	public static final double MAX_LATITUDE = 90.0;
	public static final double MIN_LATITUDE = -90.0;
	public static final double MAX_LONGITUDE = 180.0;
	public static final double MIN_LONGITUDE = -180.0;
	/**
	 * Constant values used for flight validation
	 */
	public static final int MAX_LEGS = 3;
	public static final int MAX_LAYOVER_TIME_HOURS = 4;
	public static final double MIN_LAYOVER_TIME_HOURS = 0.5;
	
}
