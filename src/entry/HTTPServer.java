package entry;

import airport.Airport;
import airport.Airports;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ServerInterface;
import leg.Flights;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.SeatTypeEnum;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import reservation.Trip;
import search.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;
import time.MyTime;
import utils.Saps;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lidian Lin, Jackson Powell
 * Responsibilities: Entry point for browser-based user-interface through the use of sockets, buffered readers, and output streams
 * Significant associations: Trip for reserving seats, searchOneWayTripFlights for searching flights, searchCriteria for what's
 * needed to search for flights, search.flight and nested objects for the existence of getters to allow automatic json serialization,
 * and the json format provided by the user-interface for parsing the input correctly.
 */
public class HTTPServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(88, 10);
            while (true) {
                Socket socket = serverSocket.accept();
//                InputStream is = socket.getInputStream();
//                is.read(new byte[2048]);
                BufferedReader httpReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String requestLine = httpReader.readLine();
                String[] requestArray = requestLine.split(" ");
                String request = requestArray[1];
                String[] requests = request.split("\\?");
                HashMap<String, String> parameterPairs = new HashMap<>();


                try {
                    if (requests.length > 1) {
                        String[] parameters = requests[1].split("&");
                        for (String parameter : parameters) {
                            String[] pair = parameter.split("=");
                            parameterPairs.put(pair[0], pair[1]);
                        }
                        System.out.println(parameterPairs);
                        if (parameterPairs.get("action").equals("list")) {
                            if (parameterPairs.get("listType").equals("airports")) {
                                Airports airports = ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME);
                                Collections.sort(airports);
                                StringBuilder ssss = null;
                                for (Airport airport : airports) {
                                    ssss.append(airport.toString()).append("\r\n");
//                                    System.out.println(airport.toString());
                                }
                            } else if (parameterPairs.get("listType").equals("departing") || parameterPairs.get("listType").equals("arriving")) {
                                SearchCriteria criteria = new SearchCriteria(parameterPairs.get("depAirport"), parameterPairs.get("arrAirport"),
                                        LocalDate.parse(parameterPairs.get("day"), DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT)),
                                        parameterPairs.get("listType").equalsIgnoreCase("departing"));
                                SearchOneWayTripFlights search = new SearchOneWayTripFlights(criteria);
                                List<Flight> availableFlights = search.search();
                                if (parameterPairs.containsKey("onlyAfterGMT")){
                                    MyTime after = new MyTime(parameterPairs.get("onlyAfterGMT").replace("_"," "));
                                    availableFlights = availableFlights.stream().filter(flight -> (flight.getConnectingLegList().get(flight.getNumLegs()-1).getDepartureTime().timespan(after)) < 0).collect(Collectors.toList());
                                }
                                ObjectMapper mapper = new ObjectMapper();
                                String jsonFlights;
                                OutputStream os = socket.getOutputStream();

                                try {
                                    jsonFlights = (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availableFlights));
                                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:" + new Date() + "\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write(jsonFlights.getBytes());
                                    os.close();
                                } catch (Exception e) { // TODO: Create variations maybe for different exceptions
                                    os.write("HTTP/1.1 400 BAD\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:" + new Date() + "\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write(e.toString().getBytes());
                                    os.close();
                                }
                            }
                        } else if (parameterPairs.get("action").equalsIgnoreCase("reserve")) {
                            StringBuilder inputBody = new StringBuilder();
//                            List<String> content = httpReader.lines().collect(Collectors.toList());
//
//                            boolean seen = false;
//                            for(String line : content){
//                                if(seen){
//                                    inputBody.append(line);
//                                }
//                                else{
//                                    seen = line.contains("[");
//                                }
//                            }
//                            inputBody.append("[",0,1);
                            String line;
                            while (!(line = httpReader.readLine()).contains("Content-Length:")) {
                                //System.out.println(line);
                            }
                            httpReader.readLine();
                            String add;
                            String breakOn = "}]";
                            boolean keepGoing = true;
                            long start = System.currentTimeMillis();
                            while (keepGoing && System.currentTimeMillis() - start < 1000) {
                                add = httpReader.readLine();
                                inputBody.append(add);
                                System.out.println(add);
                                System.out.println("asda");
                                keepGoing = !add.contains(breakOn);
                                if(!keepGoing){
                                    System.out.println("hit");
                                    break;
                                }
                            }
                            System.out.println("yay");
                            System.out.println(inputBody);
                            OutputStream os = socket.getOutputStream();
                            try {
                                JSONArray json = (JSONArray) new JSONParser().parse(inputBody.toString());
                                Flights flightsToReserve = new Flights();
                                List<SeatTypeEnum> seatTypes = new ArrayList<>();
                                if (json.size() > 2 || json.size() < 1) {
                                    throw new InvalidParameterException("Can only reserve one or two flights at a time");
                                }

                                for (Object flight : json) {
                                    SeatTypeEnum seatType = SeatTypeEnum.COACH;
                                    leg.Flight newFlight;
                                    ConnectingLegs legsToReserve = new ConnectingLegs();
                                    JSONObject jsonFlight = (JSONObject) flight;
                                    int numLegs = Integer.parseInt((jsonFlight.get("numLegs")).toString());
                                    if (numLegs > 3 || numLegs < 1) {
                                        throw new InvalidParameterException("Each flight can have at most three connecting legs");
                                    }
                                    JSONArray legs = (JSONArray) jsonFlight.get("connectingLegList");
                                    for (Object leg : legs) {
                                        JSONObject jsonLeg = (JSONObject) leg;
                                        String depCode = (((JSONObject) jsonLeg.get("departureAirport")).get("mCode")).toString();
                                        String arrCode = (((JSONObject) jsonLeg.get("arrivalAirport")).get("mCode")).toString();
                                        String flightTime = (jsonLeg.get("flightTime")).toString();
                                        String number = jsonLeg.get("flightNumber").toString();
                                        String model = (((JSONObject) jsonLeg.get("airplane")).get("mModel")).toString();

                                        JSONObject depGMTTIme = (JSONObject) ((JSONObject) jsonLeg.get("departureTime")).get("gmtTime");
                                        String dyear = String.format("%04d", Integer.parseInt(depGMTTIme.get("year").toString()));
                                        String dupmonth = depGMTTIme.get("month").toString().toLowerCase();
                                        String dmonth = dupmonth.substring(0,1).toUpperCase() + dupmonth.substring(1);
                                        String dday = String.format("%02d", Integer.parseInt(depGMTTIme.get("dayOfMonth").toString()));
                                        String dhour = String.format("%02d", Integer.parseInt(depGMTTIme.get("hour").toString()));
                                        String dminute = String.format("%02d", Integer.parseInt(depGMTTIme.get("minute").toString()));
                                        String depTime = dyear + " " + dmonth + " " + dday + " " + dhour + ":" + dminute;

                                        JSONObject arrGMTTIme = (JSONObject) ((JSONObject) jsonLeg.get("arrivalTime")).get("gmtTime");
                                        String ayear = String.format("%04d", Integer.parseInt(arrGMTTIme.get("year").toString()));
                                        String aupmonth = arrGMTTIme.get("month").toString().toLowerCase();
                                        String amonth = aupmonth.substring(0,1).toUpperCase() + aupmonth.substring(1);
                                        String aday = String.format("%02d", Integer.parseInt(arrGMTTIme.get("dayOfMonth").toString()));
                                        String ahour = String.format("%02d", Integer.parseInt(arrGMTTIme.get("hour").toString()));
                                        String aminute = String.format("%02d", Integer.parseInt(arrGMTTIme.get("minute").toString()));
                                        String arrTime = ayear + " " + amonth + " " + aday + " " + ahour + ":" + aminute;

                                        // Don't care about seating information at this point since it will be refreshed
                                        legsToReserve.add(new ConnectingLeg(model, flightTime, number, depCode, depTime,
                                                arrCode, arrTime, "1", "1",
                                                "1", "1"));
                                        String seatChosen = jsonLeg.get("seating").toString();
                                        seatType = seatChosen.equalsIgnoreCase("firstClass") ? SeatTypeEnum.FIRSTCLASS : SeatTypeEnum.COACH;
                                    }
                                    newFlight = new leg.Flight(legsToReserve);
                                    seatTypes.add(seatType);
                                    flightsToReserve.add(newFlight);
                                }
                                boolean success = false;
                                if (flightsToReserve.size() == 2) {
                                    success = new Trip(flightsToReserve.get(0), flightsToReserve.get(1), seatTypes.get(0), seatTypes.get(1)).reserveSeats();
                                } else {
                                    success = new Trip(flightsToReserve.get(0), seatTypes.get(0)).reserveSeats();
                                }
                                if (success) {
                                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:" + new Date() + "\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write("Flights were reserved successfully".getBytes());
                                }
                                else {
                                    os.write("HTTP/1.1 400 OK\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:" + new Date() + "\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write("Flights were not reserved successfully. Seats may no longer be available".getBytes());
                                }
                                os.close();

                            }

                            catch (Exception e) { // TODO: Create variations maybe for different exceptions
                                os.write("HTTP/1.1 400 BAD\r\n".getBytes());
                                os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                os.write("Server:gybs\r\n".getBytes());
                                os.write(("Date:" + new Date() + "\r\n").getBytes());
                                os.write("\r\n".getBytes());
                                os.write(e.toString().getBytes());
                                os.close();
                                e.printStackTrace();
                            }
                            System.out.println(inputBody);
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
