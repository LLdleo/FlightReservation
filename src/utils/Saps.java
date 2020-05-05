package utils;

/**
 * @author blake, Jackson Powell
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
	/**
	 * Format string for times in WPI server (Y=year, M=month, D=day, H=hour, M=minute)
	 */
	public static final String TIME_FORMAT = "yyyy MMM dd HH:mm";
	/**
	 * Team name for which server to access
	 */
	public static final String TEAMNAME = "PoLYmer";
	/**
	 * Format string for dates used in query strings for WPI server
	 */
	public static final String SERVER_DATE_FORMAT = "yyyy_MM_dd";
	/**
	 * The number of seconds that the system will try to acquire the lock before timing out.
	 * @see reservation.ServerLockException For the exception thrown when this timeout is met.
	 */
	public static final int LOCK_TIMEOUT_SECONDS= 5;
	/**
	 * The number of seconds that the system will try to reconnect to either the WPI or time server.
	 */
	public static final int CONNECTION_TIMEOUT_SECONDS = 5;
	/**
	 * Exception messages
	 */
	public static final String CONNECTION_EXCEPTION_MESSAGE= "connection lost with external service";
	public static final String LOCK_EXCEPTION_MESSAGE = "database cannot be locked";

}
