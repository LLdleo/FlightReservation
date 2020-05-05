/**
 * 
 */
package dao;

import leg.*;
import leg.ConnectingLeg;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Lidian Lin
 * @version 1.0 2020-03-12
 * @since 2020-03-12
 *
 */
public class DaoConnectingLeg {
	/**
	 * Builds a collection of connecting legs from flights described in XML
	 * 
	 * Parses an XML string to read each of the connecting legs and adds each valid connecting leg
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives. 
	 * 
	 * Method iterates over the set of Airport nodes in the XML string and builds
	 * a ConnectingLeg object from the XML node string and add the ConnectingLeg object instance to
	 * the ConnectingLegs collection.
	 * 
	 * @param xmlFlights XML string containing set of ConnectingLegs
	 * @return [possibly empty] collection of ConnectingLegs in the xml string
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @pre the xmlFlights string adheres to the format specified by the server API
	 * @post the [possibly empty] set of ConnectingLegs in the XML string are added to collection
	 */
	public static ConnectingLegs addAll (String xmlFlights) throws NullPointerException {
		ConnectingLegs connectingLegs = new ConnectingLegs();
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docFlights = buildDomDoc (xmlFlights);
		NodeList nodesFlights = docFlights.getElementsByTagName("Flight");
		
		for (int i = 0; i < nodesFlights.getLength(); i++) {
			Element elementFlight = (Element) nodesFlights.item(i);
			ConnectingLeg connectingLeg = buildFlight (elementFlight);
			
			if (connectingLeg.isValid()) {
				connectingLegs.add(connectingLeg);
			}
		}
		
		return connectingLegs;
	}

	/**
	 * Creates a ConnectingLeg object from a DOM node
	 * 
	 * Processes a DOM Node that describes a ConnectingLeg and creates an Airport object from the information
	 * @param nodeFlight is a DOM Node describing a ConnectingLeg
	 * @return ConnectingLeg object created from the DOM Node representation of the ConnectingLeg
	 * 
	 * @pre nodeFlight is of format specified by CS509 server API
	 * @post ConnectingLeg object instantiated. Caller responsible for deallocating memory.
	 */
	static private ConnectingLeg buildFlight (Node nodeFlight) {
		String airplane;
		String flightTime;
		String number;
		String departureCode;
		String departureTime;
		String arrivalCode;
		String arrivalTime;
		String firstClassPrice;
		int firstClassReserved;
		String coachPrice;
		int coachReserved;

		// The airport element has attributes of Name and 3 character airport code
		Element elementFlight = (Element) nodeFlight;
		airplane = elementFlight.getAttributeNode("Airplane").getValue();
		flightTime = elementFlight.getAttributeNode("FlightTime").getValue();
		number = elementFlight.getAttributeNode("Number").getValue();

		// The latitude and longitude are child elements
		Element elementDeparture;
		elementDeparture = (Element)elementFlight.getElementsByTagName("Departure").item(0);
		departureCode = elementDeparture.getElementsByTagName("Code").item(0).getFirstChild().getNodeValue();
		departureTime = elementDeparture.getElementsByTagName("Time").item(0).getFirstChild().getNodeValue();

		Element elementArrival;
		elementArrival = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
		arrivalCode = elementArrival.getElementsByTagName("Code").item(0).getFirstChild().getNodeValue();
		arrivalTime = elementArrival.getElementsByTagName("Time").item(0).getFirstChild().getNodeValue();

		Element elementSeating;
		elementSeating = (Element)elementFlight.getElementsByTagName("Seating").item(0);
		Element elementFirstClass;
		elementFirstClass = (Element)elementSeating.getElementsByTagName("FirstClass").item(0);
		firstClassPrice = elementFirstClass.getAttributeNode("Price").getValue();
		firstClassReserved = Integer.parseInt(elementFirstClass.getFirstChild().getNodeValue());

		Element elementCoach;
		elementCoach = (Element)elementSeating.getElementsByTagName("Coach").item(0);
		coachPrice = elementCoach.getAttributeNode("Price").getValue();
		coachReserved = Integer.parseInt(elementCoach.getFirstChild().getNodeValue());

		Departure departure = new Departure(departureCode, departureTime);

		Arrival arrival = new Arrival(arrivalCode, arrivalTime);


		Seating seating = new Seating(firstClassPrice, firstClassReserved, coachPrice, coachReserved);
		/**
		 * Instantiate an empty Airport object and initialize with data from XML node
		 */
		ConnectingLeg connectingLeg = new ConnectingLeg();

		connectingLeg.airplane(airplane);
		connectingLeg.flightTime(flightTime);
		connectingLeg.number(number);
		connectingLeg.departure(departure);
		connectingLeg.arrival(arrival);
		connectingLeg.seating(seating);

		return connectingLeg;
	}

	/**
	 * Builds a DOM tree from an XML string
	 * 
	 * Parses the XML file and returns a DOM tree that can be processed
	 * 
	 * @param xmlString XML String containing set of objects
	 * @return DOM tree from parsed XML or null if exception is caught
	 */
	static private Document buildDomDoc (String xmlString) {
		/**
		 * load the xml string into a DOM document and return the Document
		 */
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xmlString));
			
			return docBuilder.parse(inputSource);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieve character data from an element if it exists
	 * 
	 * @param e is the DOM Element to retrieve character data from
	 * @return the character data as String [possibly empty String]
	 */
	private static String getCharacterDataFromElement (Element e) {
		Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	        CharacterData cd = (CharacterData) child;
	        return cd.getData();
	      }
	      return "";
	}
}
