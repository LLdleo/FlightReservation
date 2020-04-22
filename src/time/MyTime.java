package time;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Jackson Powell
 * Responsibilities: Convert between local/GMT and calculate timespans
 */
public class MyTime {
    /**
     * gmtTime is the time in GMT (Greenwich Mean Time)
     */
    private LocalDateTime gmtTime;
    /**
     * localTime is the time in the timezone of the associated latitude and longitude
     */
    private LocalDateTime localTime;
    /**
     * assocLatitude is the latitude coordinate of the location associated with this time
     */
    private double assocLatitude;
    /**
     * assocLongitude is the longitude coordinate of the location associated with this item
     */
    private double assocLongitude;

    /**
     * Constructor for MyTime object without knowing local time before-hand
     *
     * @param gmtTime The time in the timezone of GMT (Greenwich Mean Time)
     * @param assocLatitude The latitude coordinate of the location associated with this time.
     * @param assocLongitude The longitude coordinate of the location associated with this time.
     */
    public MyTime(LocalDateTime gmtTime, double assocLatitude, double assocLongitude){
        this.gmtTime = gmtTime;
        this.assocLatitude = assocLatitude;
        this.assocLongitude = assocLongitude;
        this.localTime = calculateLocalTime(gmtTime, assocLatitude, assocLongitude);
    }

    /**
     * Calculate the local time given the GMT time and location information
     *
     * @param gmtTime The GMT time to be converted.
     * @param latitude The latitude of the location in the local timezone to convert to.
     * @param longitude The longitude of the location in the local timezone to convert to.
     * @return The converted time in the timezone of the location with latitude and longitude coordinates.
     */
    public static LocalDateTime calculateLocalTime(LocalDateTime gmtTime, double latitude, double longitude){
        double offset = TimezoneInterface.INSTANCE.getTimezoneOffset(latitude, longitude);
        long offsetHours = (long) offset;
        long offsetMinutes = (long)((offset % 1) * 60);
        return gmtTime.plusHours(offsetHours).plusMinutes(offsetMinutes);
    }

    /**
     * Calculate the time between two times.
     *
     * @param other The second time to compare to this object.
     * @return The absolute value of the time difference between this object and other
     */
    public Duration timespan(MyTime other){
        return Duration.between(this.gmtTime,other.gmtTime);
    }
}
