
package dao;

import airplane.Airplane;
import airplane.Airplanes;
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
 * Parse the xml returned for the airplane query
 *
 * @author Jackson Powell
 * @version 1.0
 * @since 2020-04-22
 *
 */
public class DaoAirplane {
	/**
	 * Builds a collection of airplanes from airplanes described in XML
	 * 
	 * Parses an XML string to read each of the airplanes and adds each valid airplanes
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives. 
	 * 
	 * Method iterates over the set of airplanes nodes in the XML string and builds
	 * an airplanes object from the XML node string and add the airplanes object instance to
	 * the airplanes collection.
	 * 
	 * @param xmlAirplanes XML string containing set of airplanes
	 * @return [possibly empty] collection of Airplanes in the xml string
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @pre the xmlAirplanes string adheres to the format specified by the server API
	 * @post the [possibly empty] set of Airplanes in the XML string are added to collection
	 */
	public static Airplanes addAll (String xmlAirplanes) throws NullPointerException {
		Airplanes airplanes = new Airplanes();
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docAirplanes = buildDomDoc (xmlAirplanes);
		NodeList nodesAirplanes = docAirplanes.getElementsByTagName("Airplane");
		
		for (int i = 0; i < nodesAirplanes.getLength(); i++) {
			Element elementAirport = (Element) nodesAirplanes.item(i);
			Airplane airplane = buildAirplane(elementAirport);
			
			if (airplane.isValid()) {
				airplanes.add(airplane);
			}
		}
		
		return airplanes;
	}

	/**
	 * Creates an Airplane object from a DOM node
	 * 
	 * Processes a DOM Node that describes an Airplane and creates an Airplane object from the information
	 * @param nodeAirplane is a DOM Node describing an Airplane
	 * @return Airplane object created from the DOM Node representation of the Airport
	 * 
	 * @pre nodeAirplane is of format specified by CS509 server API
	 * @post Airplane object instantiated. Caller responsible for deallocating memory.
	 */
	static private Airplane buildAirplane (Node nodeAirplane) {
		String manufacturer;
		String model;
		Integer firstClassSeats;
		Integer coachSeats;
		
		// The airplane element has attributes of Manufacturer and model
		Element elementAirplane = (Element) nodeAirplane;
		manufacturer = elementAirplane.getAttributeNode("Manufacturer").getValue();
		model = elementAirplane.getAttributeNode("Model").getValue();
		
		// The first class and coach seats are child elements
		Element elementSeats;
		elementSeats = (Element)elementAirplane.getElementsByTagName("FirstClassSeats").item(0);
		firstClassSeats = Integer.parseInt(getCharacterDataFromElement(elementSeats));

		elementSeats = (Element)elementAirplane.getElementsByTagName("CoachSeats").item(0);
		coachSeats = Integer.parseInt(getCharacterDataFromElement(elementSeats));

		return new Airplane(manufacturer,model,firstClassSeats,coachSeats);
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
		/*
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
