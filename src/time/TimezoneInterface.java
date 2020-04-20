package time;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import utils.QueryFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * @author Jackson Powell
 * Responsibilities: Interface with timezone service and convert from/to JSON
 * Uses org.json.simple for parsing JSON, imported using Maven.
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
     * @pre Latitude is in range [-90,90] and longitude is in range [-180,180].
     * @param latitude  The latitude of a location to get the timezone offset for.
     * @param longitude The longitude of a location to get the timezone offset for.
     * @return The GMT offset for the given latitude and longitude. Null is returned if there was a problem connecting
     */
    public Double getTimezoneOffset(double latitude, double longitude) {
        URL url;
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuffer result = new StringBuffer();
        try {
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
        } catch (IOException e) { // TODO: Maybe incorporate handleConnectionLost use case stuff
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * TODO: Figure out how we want to handle this
     *
     * @return
     */
    private boolean handleConnectionLost() {
        return true;
    }
}
