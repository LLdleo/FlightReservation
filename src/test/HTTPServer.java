package test;

import airport.Airport;
import airport.Airports;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ServerInterface;
import flight.Flights;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import leg.SeatTypeEnum;
import search.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;
import utils.Saps;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
                String requestLine=httpReader.readLine();
                String[] requestArray = requestLine.split(" ");
                String request = requestArray[1];
                String[] requests = request.split("\\?");
                HashMap<String, String> parameterPairs = new HashMap<>();


                try{
                    if (requests.length>1) {
                        String[] parameters = requests[1].split("&");
                        for (String parameter: parameters) {
                            String[] pair = parameter.split("=");
                            parameterPairs.put(pair[0], pair[1]);
                        }
                        System.out.println(parameterPairs);
                        if (parameterPairs.get("action").equals("list")){
                            if (parameterPairs.get("listType").equals("airports")) {
                                Airports airports = ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME);
                                Collections.sort(airports);
                                StringBuilder ssss = null;
                                for (Airport airport : airports) {
                                    ssss.append(airport.toString()).append("\r\n");
//                                    System.out.println(airport.toString());
                                }
                            }
                            else if (parameterPairs.get("listType").equals("departing") || parameterPairs.get("listType").equals("arriving")) {
                                SearchCriteria criteria = new SearchCriteria(parameterPairs.get("depAirport"), parameterPairs.get("arrAirport"),
                                        LocalDate.parse(parameterPairs.get("day"), DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT)),
                                        parameterPairs.get("listType").equalsIgnoreCase("departing"));
                                SearchOneWayTripFlights search = new SearchOneWayTripFlights(criteria);
                                List<Flight> availableFlights = search.search();
                                ObjectMapper mapper = new ObjectMapper();
                                String jsonFlights;
                                OutputStream os = socket.getOutputStream();

                                try{
                                    jsonFlights = (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availableFlights));
                                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:"+new Date()+"\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write(jsonFlights.getBytes());
                                    os.close();
                                }
                                catch(Exception e){ // TODO: Create variations maybe for different exceptions
                                    os.write("HTTP/1.1 400 BAD\r\n".getBytes());
                                    os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                    os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                    os.write("Server:gybs\r\n".getBytes());
                                    os.write(("Date:"+new Date()+"\r\n").getBytes());
                                    os.write("\r\n".getBytes());
                                    os.write(e.toString().getBytes());
                                    os.close();
                                }
                            }
                        }
                    }

                }
                catch (Exception e) {
                    System.out.println(e.toString());
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
