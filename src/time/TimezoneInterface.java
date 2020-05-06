package time;

import dao.ServerAccessException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import utils.QueryFactory;
import utils.Saps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Interface with timezone service and convert from/to JSON.
 * Uses org.json.simple for parsing JSON, imported using Maven.
 * Significant associations: ipgeolocation for the api for determining timezone offsets based on latitude and longitude and airport for what information is provided for determining the timezone offset for an offset.
 *
 * @author Jackson Powell
 */
public enum TimezoneInterface {
    INSTANCE;

    /**
     * apiKey is the key associated with an ipgeolocation account for accessing their timezone service
     */
    private final String apiKey = "22be5e5090ac4ea2b9af5002a054256e";
    /**
     * mConnectionURL is the Universal Resource Locator (web address) of the ipgeolocation timezone service
     */
    private final String mConnectionURL = "https://api.ipgeolocation.io/timezone?apiKey=" + apiKey;

    /**
     * Retrieve the GMT offset of a location
     * <p>
     * Retrieve the GMT offset of a location based on latitude and longitude from ipgeolocation or local cache
     *
     * @throws ServerAccessException if there was an issue connecting with ipgeolocation.
     * @throws InvalidParameterException If latitude is not in [-90,90] or longitude is not in [-180,180]
     * @pre Latitude is in range [-90,90] and longitude is in range [-180,180].
     * @post Timezone offset for location is cached if not already cached.
     * @param latitude  The latitude of a location to get the timezone offset for.
     * @param longitude The longitude of a location to get the timezone offset for.
     * @return The GMT offset for the given latitude and longitude. Null is returned if there was a problem connecting
     */
    public Double getTimezoneOffset(double latitude, double longitude) throws ServerAccessException, InvalidParameterException {
        if(latitude < Saps.MIN_LATITUDE || latitude > Saps.MAX_LATITUDE || longitude < Saps.MIN_LONGITUDE || longitude > Saps.MAX_LONGITUDE){
            throw new InvalidParameterException("Latitude or longitude not in ranges [-90,90] or [-180,180]");
        }
        if(Timezones.INSTANCE.isLocationCached(latitude, longitude)){
            return Timezones.INSTANCE.getOffset(latitude, longitude);
        }
        else{
            Double offset = getTimezoneOffsetFromAPI(latitude, longitude);
            Timezones.INSTANCE.cacheTimezoneForLocation(latitude,longitude,offset);
            return offset;
        }
    }
    /**
     * Retrieve the GMT offset of a location from ipgeolocation
     * <p>
     * Retrieve the GMT offset of a location based on latitude and longitude from ipgeolocation using HTTP GET protocol
     *
     * @throws ServerAccessException if there was an issue connecting with ipgeolocation.
     * @pre Latitude is in range [-90,90] and longitude is in range [-180,180]. Location has not been cached.
     * @param latitude  The latitude of a location to get the timezone offset for.
     * @param longitude The longitude of a location to get the timezone offset for.
     * @return The GMT offset for the given latitude and longitude. Null is returned if there was a problem connecting
     */
    private Double getTimezoneOffsetFromAPI(double latitude, double longitude) throws ServerAccessException{
        URL url;
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuilder result = new StringBuilder();
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        while(true) {
            try {
                // ipgeolocation doesn't work with either coordinate as zero, so get approximation.
                if (latitude == 0) latitude += .0000001;
                if (longitude == 0) longitude += .0000001;
                /*
                 * Create an HTTP connection to the server for a GET
                 * QueryFactory provides the parameter annotations for the HTTP GET query string
                 */

                url = new URL(mConnectionURL + QueryFactory.getTimezoneOffset(latitude, longitude));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

            /* If response code of SUCCESS read the XML string returned
              line by line to build the full return string
             */
                int responseCode = connection.getResponseCode();
                if (responseCode >= HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                }
                JSONObject data = (JSONObject) new JSONParser().parse(result.toString());
                return Double.parseDouble(data.get("timezone_offset").toString());
            } catch (IOException e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                try{
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (Exception e2){
                    e2.printStackTrace();
                }
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("There was an issue connecting with the timezone service");
            } catch (ParseException e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                try{
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (Exception e2){
                    e2.printStackTrace();
                }
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("There was an issue interpreting the timezone for a request");
            }
        }
    }
}
