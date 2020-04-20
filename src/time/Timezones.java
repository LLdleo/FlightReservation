package time;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jackson Powell
 * @since 2020-20-04
 * Responsibilities: Cache the timezone offsets for locations
 */
public enum Timezones {
    INSTANCE;
    /**
     * locationMapToOffset is the simple hashmap in-memory structure used for caching timezone offsets.
     * The key is the concatenation of latitude, ",", and longitude and the value is the timezone offset in hours from GMT.
     */
    private static Map<String, Double> locationMapToOffset = new HashMap<String,Double>();
    /**
     * Determine if the timezone of the given latitude and longitude has already been retrieved and cached
     *
     * @param latitude The latitude coordinate of the location to check for a timezone offset.
     * @param longitude The longitude coordinate of the location to check for a timezone offset.
     * @return True if there is a timezone offset cached for the offset, false otherwise
     */
    public boolean isLocationCached(double latitude, double longitude){
        return locationMapToOffset.containsKey(hashLocation(latitude,longitude));
    }

    /**
     * Get the timezone offset (in hours) from the cache for a given latitude and longitude
     *
     * @pre There is a timezone cached for this latitude and longitude.
     * @param latitude The latitude coordinate of the location to get the timezone offset.
     * @param longitude The longitude coordinate of the location to get the timezone offset.
     * @return The timezone offset, in hours from GMT, of the latitude and longitude.
     */
    public double getOffset(double latitude, double longitude){
        return locationMapToOffset.get(hashLocation(latitude,longitude));
    }

    /**
     * Store the given offset in the cache, associated with the respective latitude and longitude
     *
     * @pre offset is a valid timezone offset(not null and in range [-12,12]) and there is not a timezone already cached for the given location.
     * @pre latitude is in range [-90, 90] and longitude is in range [-180, 180].
     * @post There is a timezone offset stored in the cache associated with the latitude and longitude which can be retrieved for future requests.
     * @param latitude The latitude coordinate of the location to cache the timezone offset for.
     * @param longitude The longitude coordinate of the location to cache the timezone offset for.
     * @param offset The offset of the location in hours from GMT.
     */
    public void cacheTimezoneForLocation(double latitude, double longitude, double offset) {
        locationMapToOffset.put(hashLocation(latitude, longitude),offset);
    }

    /**
     * Hash function for converting the latitude and longitude location coordinates into the string key for the cache map.
     *
     * @param latitude Latitude coordinate of the location to hash into the map's key.
     * @param longitude Longitude coordinate of the location to hash into the map's key.
     * @return The hash value of the location's coordinates as a String.
     */
    private String hashLocation(double latitude, double longitude){
        return "" + latitude + "," + longitude;
    }
}
