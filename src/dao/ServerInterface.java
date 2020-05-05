/**
 *
 */
package dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import airplane.Airplanes;
import airport.Airports;
import leg.ConnectingLeg;
import leg.ConnectingLegs;

import leg.SeatTypeEnum;
import reservation.Trip;
import utils.QueryFactory;
import utils.Saps;


/**
 * This class provides an interface to the CS509 server. It provides sample methods to perform
 * HTTP GET and HTTP POSTS
 *
 * @author blake, Lidian Lin, Jackson Powell
 * @version 1.1
 * @since 2016-02-24
 *
 */
public enum ServerInterface {
    INSTANCE;

    /**
     * mUrlBase is the Universal Resource Locator (web address) of the CS509 reservation server
     */
    private final String mUrlBase = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";

    /**
     * Return a collection of all the airports from server
     *
     * Retrieve the list of airports available to the specified teamName via HTTPGet of the server
     *
     * @pre System is connected to the internet and the WPI server is available
     * @post A collection of airports is populated with some or no results or an exception is thrown and needs to be caught.
     * @throws ServerAccessException if there was an issue connecting with the WPI server
     * @param teamName identifies the name of the team requesting the collection of airports
     * @return collection of Airports from server or null if error.
     */
    public Airports getAirports(String teamName) throws ServerAccessException {

        URL url;
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuffer result = new StringBuffer();

        String xmlAirports;
        Airports airports;
        boolean success = false;
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        while (!success) {
            try {
                /**
                 * Create an HTTP connection to the server for a GET
                 * QueryFactory provides the parameter annotations for the HTTP GET query string
                 */
                url = new URL(mUrlBase + QueryFactory.getAirports(teamName));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", teamName);

                /**
                 * If response code of SUCCESS read the XML string returned
                 * line by line to build the full return string
                 */
                int responseCode = connection.getResponseCode();
                if (responseCode >= HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    encoding = (encoding == null ? "UTF-8" : encoding);

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                }
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of airports from the WPI Server");
            } catch (Exception e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of airports from the WPI Server for some other reason");
            }
        }

        xmlAirports = result.toString();
        airports = DaoAirport.addAll(xmlAirports);
        return airports;

    }


    /**
     * Return a collection of all the airplanes from server
     *
     * Retrieve the list of airports available to the specified teamName via HTTPGet of the server
     *
     * @pre System is connected to the internet and the WPI server is available
     * @post A collection of airplanes is populated with some or no results or an exception is thrown and needs to be caught.
     * @throws ServerAccessException if there was an issue connecting with the WPI server
     * @param teamName identifies the name of the team requesting the collection of airplanes
     * @return collection of Airplanes from server or null if error.
     */
    public Airplanes getAirplanes(String teamName) throws ServerAccessException {

        URL url;
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuffer result = new StringBuffer();

        String Airplanes;
        boolean success = false;
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        while (!success) {
            try {
                /**
                 * Create an HTTP connection to the server for a GET
                 * QueryFactory provides the parameter annotations for the HTTP GET query string
                 */
                url = new URL(mUrlBase + QueryFactory.getAirplanes(teamName));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", teamName);

                /**
                 * If response code of SUCCESS read the XML string returned
                 * line by line to build the full return string
                 */
                int responseCode = connection.getResponseCode();
                if (responseCode >= HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    encoding = (encoding == null ? "UTF-8" : encoding);

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                }
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of airplanes from the WPI Server");
            } catch (Exception e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of airplanes from the WPI Server for some other reason");
            }
        }

        Airplanes = result.toString();
        return DaoAirplane.addAll(Airplanes);

    }


    /**
     * Return a collection of all the airports from server
     *
     * Retrieve the list of airports available to the specified teamName via HTTPGet of the server
     *
     * @pre System is connected to the internet and the WPI server is available
     * @post A collection of connecting legs is populated with some or no results or an exception is thrown and needs to be caught.
     * @throws ServerAccessException if there is an issue connecting with the WPI server
     * @param teamName identifies the name of the team requesting the collection of airports
     * @return collection of Airports from server or null if error.
     */
    public ConnectingLegs getLegs(String teamName, String listType, String airportCode, String day) throws ServerAccessException {

        URL url;
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuffer result = new StringBuffer();

        String xmlFlights;
        ConnectingLegs connectingLegs;
        boolean success = false;
        long startLockTimer = System.currentTimeMillis();
        long endLockTimer;
        while (!success) {
            try {
                /**
                 * Create an HTTP connection to the server for a GET
                 * QueryFactory provides the parameter annotations for the HTTP GET query string
                 */
                url = new URL(mUrlBase + QueryFactory.getLegs(teamName, listType, airportCode, day));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", teamName);

                /**
                 * If response code of SUCCESS read the XML string returned
                 * line by line to build the full return string
                 */
                int responseCode = connection.getResponseCode();
                if (responseCode >= HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    encoding = (encoding == null ? "UTF-8" : encoding);

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                }
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of connecting legs from the WPI Server");
            } catch (Exception e) {
                e.printStackTrace();
                endLockTimer = System.currentTimeMillis();
                if ((endLockTimer - startLockTimer) / 1000 > Saps.CONNECTION_TIMEOUT_SECONDS)
                    throw new ServerAccessException("System could not access the list of connecting legs from the WPI Server for some other reason");
            }
        }

        xmlFlights = result.toString();
        connectingLegs = DaoConnectingLeg.addAll(xmlFlights);
        return connectingLegs;
    }

    /**
     * Lock the database for updating by the specified team. The operation will fail if the lock is held by another team.
     *
     * @post database locked
     *
     * @param teamName is the name of team requesting server lock
     * @return true if the server was locked successfully, else false
     */
    public boolean lock(String teamName) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(mUrlBase);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", teamName);
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String params = QueryFactory.lock(teamName);

            connection.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(params);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' to lock database");
            System.out.println(("\nResponse Code : " + responseCode));

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            System.out.println(response.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Unlock the database previous locked by specified team. The operation will succeed if the server lock is held by the specified
     * team or if the server is not currently locked. If the lock is held be another team, the operation will fail.
     *
     * The server interface to unlock the server interface uses HTTP POST protocol
     *
     * @post database unlocked if specified teamName was previously holding lock
     *
     * @param teamName is the name of the team trying to unlock the database
     * @return true if the server was successfully unlocked.
     */
    public boolean unlock(String teamName) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(mUrlBase);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            String params = QueryFactory.unlock(teamName);

            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(params);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' to unlock database");
            System.out.println(("\nResponse Code : " + responseCode));

            if (responseCode >= HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                System.out.println(response.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reserve seats for a flight on the database.
     *
     * The server interface to reserve seats on the server uses HTTP POST protocol
     *
     * @pre Database is locked by this system.
     * @inv Database remains locked throughout this method.
     * @post Database is locked and has received the data and should update the number of reservations if successful. Caller should unlock if not reserving any more seats.
     *
     * @param teamName is the name of the team holding the lock
     * @param flightToReserve is the legs to reserve each with the specified seat type.
     * @return true if the server was successfully unlocked.
     */
    public boolean reserve(String teamName, Trip flightToReserve) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(mUrlBase);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", teamName);
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String params = QueryFactory.reserve(teamName, tripToXML(flightToReserve));

            connection.setDoOutput(true);
            //connection.setDoInput(true);

            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(params);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' to reserve on the database");
            System.out.println(("\nResponse Code : " + responseCode));

            if (responseCode >= HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                System.out.println(response.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reset the database to the initial stage, no reservation is made
     *
     * The server interface to reset the database uses HTTP GET protocol
     *
     * @pre System is being used in testing.
     * @post The data in the WPI server is reset to its initial state.
     * @param teamName is the name of the team holding the lock
     * @return true if the server was successfully reset.
     */
    public boolean reset(String teamName) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(mUrlBase + QueryFactory.resetDB(teamName));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            connection.setDoOutput(true);
            //connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'GET' to reset database");
            System.out.println(("\nResponse Code : " + responseCode));

            if (responseCode >= HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                StringBuffer response = new StringBuffer();
//
//                while ((line = in.readLine()) != null) {
//                    response.append(line);
//                }
//                in.close();

                //System.out.println(response.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Convert a trip into the xml string needed to reserve a seat of the trip's seat type on each connecting leg.
     *
     * @pre tripToConvert is a valid trip with valid flight(s) and seatType(s).
     * @post A string is created that can be used as the query parameter for xml flights for reserving seats on the WPI server.
     * @param tripToConvert The trip to convert into XML so that it can be reserved.
     * @return The XML string needed to reserve the seats for the trip.
     */
    private String tripToXML(Trip tripToConvert) {
        String xmlString = "<Flights>";
        String seatTypeString = tripToConvert.getSeatType() == SeatTypeEnum.FIRSTCLASS ? "FirstClass" : "Coach";
        Iterator<ConnectingLeg> legs = tripToConvert.getOutgoingFlight().getLegs();
        while (legs.hasNext()) {
            ConnectingLeg thisLeg = legs.next();
            xmlString += "<Flight number=\"" + thisLeg.number() + "\" seating=\"" + seatTypeString + "\"/>";
        }
        if(tripToConvert.getReturnFlight() != null){
            String returnSeatTypeString = tripToConvert.getReturnSeatType() == SeatTypeEnum.FIRSTCLASS ? "FirstClass" : "Coach";
            legs = tripToConvert.getReturnFlight().getLegs();
            while (legs.hasNext()) {
                ConnectingLeg thisLeg = legs.next();
                xmlString += "<Flight number=\"" + thisLeg.number() + "\" seating=\"" + returnSeatTypeString + "\"/>";
            }
        }
        return xmlString + "</Flights>";
    }
}
