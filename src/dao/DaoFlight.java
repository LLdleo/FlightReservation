/**
 * 
 */
package dao;

import flight.Flight;
import flight.Flights;
import leg.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import timeUtils.Datetime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author PoLYmer
 * @version 1.0
 * @since 2020-04-20
 *
 */
public class DaoFlight {
	/**
	 * Builds a collection of airports from airports described in XML
	 * 
	 * Parses an XML string to read each of the airports and adds each valid airport 
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives. 
	 * 
	 * Method iterates over the set of Airport nodes in the XML string and builds
	 * an Airport object from the XML node string and add the Airport object instance to
	 * the Airports collection.
	 * 
	 * @param flights XML string containing set of airports
	 * @return [possibly empty] collection of Airports in the xml string
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @pre the xmlAirports string adheres to the format specified by the server API
	 * @post the [possibly empty] set of Airports in the XML string are added to collection
	 */
	public static void search(Flights flights, String departureAirport, String arrivalAirport, String departureDay, String arrivalDay, String arrivalDatetime, int number, ConnectingLegs legs){
		//TODO: Check Correctness
		ConnectingLegs connectingLegs = ServerInterface.INSTANCE.getLegs("PoLYmer", "departing", departureAirport, departureDay);
		number += 1;
		if (number == 4){return;}
		for (ConnectingLeg connectingLeg: connectingLegs){
			if (legs.isEmpty()){
				flightAdd(flights, departureAirport, arrivalAirport, departureDay, arrivalDay, number, legs, connectingLeg);
			}
			else if (timeCompare(connectingLeg.departure().getTime(), arrivalDatetime)){
				flightAdd(flights, departureAirport, arrivalAirport, departureDay, arrivalDay, number, legs, connectingLeg);
			}
		}
	}

	private static void flightAdd(Flights flights, String departureAirport, String arrivalAirport, String departureDay, String arrivalDay, int number, ConnectingLegs legs, ConnectingLeg connectingLeg) {
		if (connectingLeg.arrival().getCode().equals(arrivalAirport)) {
			ConnectingLegs tmpLegs = new ConnectingLegs();
			tmpLegs.addAll(legs);
			tmpLegs.add(connectingLeg);
			Flight flight = new Flight();
			flight.connectingLegArray(tmpLegs);
			System.out.println(flight.connectingLegArray());
			flights.mFlights.add(flight);
		}
		else{
			ConnectingLegs tmpLegs = new ConnectingLegs();
			tmpLegs.addAll(legs);
			tmpLegs.add(connectingLeg);
			search(flights, connectingLeg.arrival().getCode(), arrivalAirport, departureDay, arrivalDay, connectingLeg.arrival().getTime(),number, tmpLegs);
		}
	}

	public static int toInt(String string){
		return Integer.parseInt(string);
	}

	public static boolean timeCompare(String time1, String time2){
		// TODO: Simplify new function
		Datetime datetime1 = new Datetime();
		datetime1.setDatetimeFromString(time1);
		Datetime datetime2 = new Datetime();
		datetime2.setDatetimeFromString(time2);
		if (datetime1.getYear().equals(datetime2.getYear()) & datetime1.getMonth().equals(datetime2.getMonth())) {
			int dateInterval = toInt(datetime1.getDay())-toInt(datetime2.getDay());
			if (dateInterval ==0 | dateInterval==1){
				int timeInterval = 24*60*dateInterval + (datetime1.getHourMinute().getHour()-datetime2.getHourMinute().getHour())*60+ datetime1.getHourMinute().getMinute()-datetime2.getHourMinute().getMinute();
				return timeInterval >= 30 & timeInterval <= 240;
			}
		}
		return false;
	}

	/**
	 * Creates an Airport object from a DOM node
	 * 
	 * Processes a DOM Node that describes an Airport and creates an Airport object from the information
	 * @param departureAirport is a DOM Node describing an Airport
	 * @return Airport object created from the DOM Node representation of the Airport
	 * 
	 * @pre nodeAirport is of format specified by CS509 server API
	 * @post airport object instantiated. Caller responsible for deallocating memory.
	 */
	static public Flights buildFlight (String departureAirport, String arrivalAirport, String departureDate, String arrivalDate) {
		Flights flights = new Flights();
		flights.departureAirportCode(departureAirport);
		flights.arrivalAirportCode(arrivalAirport);
		flights.departureDate(departureDate);
		flights.arrivalDate(arrivalDate);
//		flights.flights();
		ConnectingLegs legs = new ConnectingLegs();

		search(flights, departureAirport, arrivalAirport, departureDate, arrivalDate,null,0, legs);

		return flights;
	}
}
