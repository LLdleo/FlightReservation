package time;

import utils.Saps;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * @throws IllegalArgumentException If Latitude is not in range [-90,90] or longitude is not in range [-180, 180]
     * @param gmtTime The time in the timezone of GMT (Greenwich Mean Time)
     * @param assocLatitude The latitude coordinate of the location associated with this time.
     * @param assocLongitude The longitude coordinate of the location associated with this time.
     */
    public MyTime(LocalDateTime gmtTime, double assocLatitude, double assocLongitude) throws IllegalArgumentException{
        if (assocLatitude < Saps.MIN_LATITUDE || assocLatitude > Saps.MAX_LATITUDE){
            throw new IllegalArgumentException("Latitude not in range ["+Saps.MIN_LATITUDE+","+Saps.MAX_LATITUDE+"]: " + assocLatitude);
        }
        if (assocLongitude< Saps.MIN_LONGITUDE || assocLatitude > Saps.MAX_LONGITUDE){
            throw new IllegalArgumentException("Longitude not in range ["+Saps.MIN_LONGITUDE+","+Saps.MAX_LONGITUDE+"]: " + assocLongitude);
        }
        this.gmtTime = gmtTime;
        this.assocLatitude = assocLatitude;
        this.assocLongitude = assocLongitude;
        this.localTime = calculateLocalTime(gmtTime, assocLatitude, assocLongitude);
    }

    /**
     *
     * @param gmtTime The time in the timezone of GMT (Greenwich Mean Time) in the string format of WPI server (YYYY MMM DD HH:MM)
     * @param assocLatitude The latitude coordinate of the location associated with this time.
     * @param assocLongitude The longitude coordinate of the location associated with this time.
     * @throws IllegalArgumentException
     */
    public MyTime(String gmtTime, double assocLatitude, double assocLongitude) throws IllegalArgumentException{
        if (assocLatitude < Saps.MIN_LATITUDE || assocLatitude > Saps.MAX_LATITUDE){
            throw new IllegalArgumentException("Latitude not in range ["+Saps.MIN_LATITUDE+","+Saps.MAX_LATITUDE+"]: " + assocLatitude);
        }
        if (assocLongitude< Saps.MIN_LONGITUDE || assocLatitude > Saps.MAX_LONGITUDE){
            throw new IllegalArgumentException("Longitude not in range ["+Saps.MIN_LONGITUDE+","+Saps.MAX_LONGITUDE+"]: " + assocLongitude);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Saps.TIME_FORMAT);
        this.gmtTime = LocalDateTime.parse(gmtTime,formatter);
        this.assocLatitude = assocLatitude;
        this.assocLongitude = assocLongitude;
        this.localTime = calculateLocalTime(this.gmtTime, assocLatitude, assocLongitude);
    }

    /**
     * Constructor for MyTime that only cares about GMT time and defaults latitude and longitude to 0
     *
     * @param gmtTime The string representation of the GMT time as 'yyyy MMM dd HH:mm'
     */
    public MyTime(String gmtTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Saps.TIME_FORMAT);
        this.gmtTime = LocalDateTime.parse(gmtTime,formatter);
        this.assocLongitude = 0;
        this.assocLatitude = 0;
        this.localTime = this.gmtTime;
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
     * Calculate the time between two times in hours.
     * Calculate the time between the start (this object) to the end (other)
     *
     * @param other The second time to compare to this object.
     * @return The time difference of this object and other.
     */
    public double timespan(MyTime other){
        Duration diff = Duration.between(this.gmtTime,other.gmtTime);
        return diff.toHours() + ((double)(diff.toMinutes()%60))/60;
    }
}
