
package driver;

import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import airport.Airport;
import airport.Airports;
import com.fasterxml.jackson.databind.ObjectMapper;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import dao.ServerInterface;

import leg.Flights;
import leg.SeatTypeEnum;
import org.apache.commons.cli.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import reservation.Trip;
import search.Flight;
import search.SearchCriteria;
import search.SearchOneWayTripFlights;
import time.MyTime;
import utils.Saps;

/**
 * Old command line entry point to the system used for the prototype. Does not work with added functionality.
 *
 * @author Lidian Lin
 * @since 2020-03-12
 * @version 1.0
 * @deprecated This entry point does not support current operations search, filter, sort, and reservation operations.
 */
public class Driver {

	/**
	 * Entry point for CS509 sample code driver
	 * 
	 * This driver will retrieve the list of airports from the CS509 server and print the list 
	 * to the console sorted by 3 character airport code
	 * 
	 * @param args is the arguments passed to java vm in format of "CS509.sample teamName" where teamName is a valid team
	 */
	public static void main(String[] args) {
		Options options = new Options();

		// list, resetDB, lockDB, unlockDB, buyTickets
		Option actionOption = new Option("a", "action", true, "action");
		actionOption.setRequired(true);
		options.addOption(actionOption);

		// airports, airplanes, departing, arriving
		Option listTypeOption = new Option("l", "listType", true, "list type");
		listTypeOption.setRequired(true);
		options.addOption(listTypeOption);

		Option teamNameOption = new Option("t", "team", true, "team name");
		teamNameOption.setRequired(true);
		options.addOption(teamNameOption);

		Option airportCodeOption = new Option("c", "airport", true, "airport code");
		airportCodeOption.setRequired(false);
		options.addOption(airportCodeOption);

		Option dayOption = new Option("d", "day", true, "day");
		dayOption.setRequired(false);
		options.addOption(dayOption);

		Option departOption = new Option("e", "depart", true, "depart airport");
		departOption.setRequired(false);
		options.addOption(departOption);

		Option arrivalOption = new Option("r", "arrival", true, "arrival airport");
		arrivalOption.setRequired(false);
		options.addOption(arrivalOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
		}

		String action = cmd.getOptionValue("action");
		String listType = cmd.getOptionValue("listType");
		String teamName = cmd.getOptionValue("team");
		String airportCode = cmd.getOptionValue("airport");
		String day = cmd.getOptionValue("day");
		String departAirportCode = cmd.getOptionValue("depart");
		String arrivalAirportCode = cmd.getOptionValue("arrival");

		try {

			if (action.equals("list")) {
				if (listType.equals("airports")) {
					Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
					Collections.sort(airports);
					for (Airport airport : airports) {
						System.out.println(airport.toString());
					}
				} else if (listType.equals("departing") || listType.equals("arriving")) {
					ConnectingLegs connectingLegs = ServerInterface.INSTANCE.getLegs(teamName, listType, airportCode, day);
					Collections.sort(connectingLegs);
					for (ConnectingLeg connectingLeg : connectingLegs) {
						System.out.println(connectingLeg.toString());
					}
				} else if (listType.equals("oneWay")){
					SearchCriteria criteria = new SearchCriteria(departAirportCode, arrivalAirportCode,
							LocalDate.parse(day, DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT)),
							true);
					SearchOneWayTripFlights search = new SearchOneWayTripFlights(criteria);
					List<Flight> availableFlights = search.search();
//					if (parameterPairs.containsKey("onlyAfterGMT")){
//						MyTime after = new MyTime(parameterPairs.get("onlyAfterGMT").replace("_"," "));
//						availableFlights = availableFlights.stream().filter(flight -> (flight.getConnectingLegList().get(flight.getNumLegs()-1).getDepartureTime().timespan(after)) < 0).collect(Collectors.toList());
//					}
					ObjectMapper mapper = new ObjectMapper();
					String jsonFlights = "[\n";

					for (Flight flight:availableFlights) {
						jsonFlights+=flight.toJSON();
						jsonFlights+=",\n";
//						System.out.println(flight.toJSON());
					}
					jsonFlights+="]";
					System.out.println(jsonFlights);


//					try {
//						jsonFlights = (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availableFlights));
//						System.out.println(Arrays.toString(jsonFlights.getBytes()));
//					} catch (Exception e) { // TODO: Do I Need Try Catch?
//						System.out.println(Arrays.toString(e.toString().getBytes()));
//					}
				}
			}
//			else if (action.equals("reserve")) {
//				StringBuilder inputBody = new StringBuilder();
//				httpReader.readLine();
//				String add;
//				String breakOn = "}]";
//				boolean keepGoing = true;
//				long start = System.currentTimeMillis();
//				while (keepGoing && System.currentTimeMillis() - start < 1000) {
//					add = httpReader.readLine();
//					inputBody.append(add);
//					System.out.println(add);
//					System.out.println("asda");
//					keepGoing = !add.contains(breakOn);
//					if(!keepGoing){
//						System.out.println("hit");
//						break;
//					}
//				}
//				System.out.println("yay");
//				System.out.println(inputBody);
//				OutputStream os = socket.getOutputStream();
//				try {
//					JSONArray json = (JSONArray) new JSONParser().parse(inputBody.toString());
//					Flights flightsToReserve = new Flights();
//					List<SeatTypeEnum> seatTypes = new ArrayList<>();
//					if (json.size() > 2 || json.size() < 1) {
//						throw new InvalidParameterException("Can only reserve one or two flights at a time");
//					}
//
//					for (Object flight : json) {
//						SeatTypeEnum seatType = SeatTypeEnum.COACH;
//						leg.Flight newFlight;
//						ConnectingLegs legsToReserve = new ConnectingLegs();
//						JSONObject jsonFlight = (JSONObject) flight;
//						int numLegs = Integer.parseInt((jsonFlight.get("numLegs")).toString());
//						if (numLegs > 3 || numLegs < 1) {
//							throw new InvalidParameterException("Each flight can have at most three connecting legs");
//						}
//						JSONArray legs = (JSONArray) jsonFlight.get("connectingLegList");
//						for (Object leg : legs) {
//							JSONObject jsonLeg = (JSONObject) leg;
//							String depCode = (((JSONObject) jsonLeg.get("departureAirport")).get("mCode")).toString();
//							String arrCode = (((JSONObject) jsonLeg.get("arrivalAirport")).get("mCode")).toString();
//							String flightTime = (jsonLeg.get("flightTime")).toString();
//							String number = jsonLeg.get("flightNumber").toString();
//							String model = (((JSONObject) jsonLeg.get("airplane")).get("mModel")).toString();
//
//							JSONObject depGMTTIme = (JSONObject) ((JSONObject) jsonLeg.get("departureTime")).get("gmtTime");
//							String dyear = String.format("%04d", Integer.parseInt(depGMTTIme.get("year").toString()));
//							String dupmonth = depGMTTIme.get("month").toString().toLowerCase();
//							String dmonth = dupmonth.substring(0,1).toUpperCase() + dupmonth.substring(1);
//							String dday = String.format("%02d", Integer.parseInt(depGMTTIme.get("dayOfMonth").toString()));
//							String dhour = String.format("%02d", Integer.parseInt(depGMTTIme.get("hour").toString()));
//							String dminute = String.format("%02d", Integer.parseInt(depGMTTIme.get("minute").toString()));
//							String depTime = dyear + " " + dmonth + " " + dday + " " + dhour + ":" + dminute;
//
//							JSONObject arrGMTTIme = (JSONObject) ((JSONObject) jsonLeg.get("arrivalTime")).get("gmtTime");
//							String ayear = String.format("%04d", Integer.parseInt(arrGMTTIme.get("year").toString()));
//							String aupmonth = arrGMTTIme.get("month").toString().toLowerCase();
//							String amonth = aupmonth.substring(0,1).toUpperCase() + aupmonth.substring(1);
//							String aday = String.format("%02d", Integer.parseInt(arrGMTTIme.get("dayOfMonth").toString()));
//							String ahour = String.format("%02d", Integer.parseInt(arrGMTTIme.get("hour").toString()));
//							String aminute = String.format("%02d", Integer.parseInt(arrGMTTIme.get("minute").toString()));
//							String arrTime = ayear + " " + amonth + " " + aday + " " + ahour + ":" + aminute;
//
//							// Don't care about seating information at this point since it will be refreshed
//							legsToReserve.add(new ConnectingLeg(model, flightTime, number, depCode, depTime,
//									arrCode, arrTime, "1", "1",
//									"1", "1"));
//							String seatChosen = jsonLeg.get("seating").toString();
//							seatType = seatChosen.equalsIgnoreCase("firstClass") ? SeatTypeEnum.FIRSTCLASS : SeatTypeEnum.COACH;
//						}
//						newFlight = new leg.Flight(legsToReserve);
//						seatTypes.add(seatType);
//						flightsToReserve.add(newFlight);
//					}
//					boolean success;
//					if (flightsToReserve.size() == 2) {
//						success = new Trip(flightsToReserve.get(0), flightsToReserve.get(1), seatTypes.get(0), seatTypes.get(1)).reserveSeats();
//					} else {
//						success = new Trip(flightsToReserve.get(0), seatTypes.get(0)).reserveSeats();
//					}
//					if (success) {
//						os.write("HTTP/1.1 200 OK\r\n".getBytes());
//						os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
//						os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
//						os.write("Server:gybs\r\n".getBytes());
//						os.write(("Date:" + new Date() + "\r\n").getBytes());
//						os.write("\r\n".getBytes());
//						os.write("Flights were reserved successfully".getBytes());
//					}
//					else {
//						os.write("HTTP/1.1 400 OK\r\n".getBytes());
//						os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
//						os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
//						os.write("Server:gybs\r\n".getBytes());
//						os.write(("Date:" + new Date() + "\r\n").getBytes());
//						os.write("\r\n".getBytes());
//						os.write("Flights were not reserved successfully. Seats may no longer be available".getBytes());
//					}
//					os.close();
//
//				}
//
//				catch (Exception e) { // TODO: Create variations maybe for different exceptions
//					os.write("HTTP/1.1 400 BAD\r\n".getBytes());
//					os.write("Access-Control-Allow-Origin: *\r\n".getBytes());
//					os.write("Content-Type:application/json;charset=utf-8\r\n".getBytes());
//					os.write("Server:gybs\r\n".getBytes());
//					os.write(("Date:" + new Date() + "\r\n").getBytes());
//					os.write("\r\n".getBytes());
//					os.write(e.toString().getBytes());
//					os.close();
//					e.printStackTrace();
//				}
//				System.out.println(inputBody);
//			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
}
