package test;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;
import leg.ConnectingLeg;
import leg.ConnectingLegs;

import java.io.*;
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
                                Airports airports = ServerInterface.INSTANCE.getAirports(parameterPairs.get("teamName"));
                                Collections.sort(airports);
                                StringBuilder ssss = null;
                                for (Airport airport : airports) {
                                    ssss.append(airport.toString()).append("\r\n");
//                                    System.out.println(airport.toString());
                                }
                            }
                            else if (parameterPairs.get("listType").equals("departing") || parameterPairs.get("listType").equals("arriving")) {
                                ConnectingLegs connectingLegs = ServerInterface.INSTANCE.getFlights(parameterPairs.get("teamName"), parameterPairs.get("listType"), parameterPairs.get("airport"), parameterPairs.get("day"));
                                Collections.sort(connectingLegs);
                                StringBuilder ssss = new StringBuilder("[");
//                                os.write("[".getBytes());
                                for (ConnectingLeg connectingLeg : connectingLegs) {
                                    ssss.append(connectingLeg.toJson()).append(",");
//                                    os.write((flight.toJson()+",").getBytes());
//                                    System.out.println(flight.toJson());
                                }
                                ssss.append("]");
                                System.out.println(ssss);
                                ssss.deleteCharAt(ssss.length()-2);

                                OutputStream os = socket.getOutputStream();
                                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                                os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                                os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
                                os.write("Server:gybs\r\n".getBytes());
                                os.write(("Date:"+new Date()+"\r\n").getBytes());
                                os.write("\r\n".getBytes());
                                os.write(ssss.toString().getBytes());
                                os.close();
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
