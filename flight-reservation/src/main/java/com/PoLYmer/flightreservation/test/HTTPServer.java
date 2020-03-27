package com.PoLYmer.flightreservation.test;

import com.PoLYmer.flightreservation.airport.Airport;
import com.PoLYmer.flightreservation.airport.Airports;
import com.PoLYmer.flightreservation.dao.ServerInterface;
import com.PoLYmer.flightreservation.flight.Flight;
import com.PoLYmer.flightreservation.flight.Flights;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class HTTPServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 10);
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
                if (requests.length>1) {
                    String[] parameters = requests[1].split("&");
                    for (String parameter: parameters) {
                        String[] pair = parameter.split("=");
                        parameterPairs.put(pair[0], pair[1]);
                    }
                    System.out.println(parameterPairs);
                    if (parameterPairs.get("action").equals("list")){
                        if (parameterPairs.get("listType").equals("airports")) {
                            Airports airports = ServerInterface.INSTANCE.getAirports(parameterPairs.get("teamName"));
                            Collections.sort(airports);
                            for (Airport airport : airports) {
                                System.out.println(airport.toString());
                            }
                        }
                        else if (parameterPairs.get("listType").equals("departing") || parameterPairs.get("listType").equals("arriving")) {
                            Flights flights = ServerInterface.INSTANCE.getFlights(parameterPairs.get("teamName"), parameterPairs.get("listType"), parameterPairs.get("airport"), parameterPairs.get("day"));
                            Collections.sort(flights);
                            for (Flight flight: flights) {
                                System.out.println(flight.toString());
                            }
                        }
                    }
                }
//                System.out.println(Arrays.toString(requestArray));
                OutputStream os = socket.getOutputStream();

                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
                os.write("Content-Length:38\r\n".getBytes());
                os.write("Server:gybs\r\n".getBytes());
                os.write(("Date:"+new Date()+"\r\n").getBytes());
                os.write("\r\n".getBytes());
                os.write("<body>".getBytes());
                os.write("<h1>hello! </h1>".getBytes());
                os.write("<h3>HTTP Server! </h3>".getBytes("utf-8"));
                os.write("</body>".getBytes());
                os.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
