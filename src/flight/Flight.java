package flight;

import leg.ConnectingLeg;
import leg.ConnectingLegs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class holds values pertaining to a single Flight. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and
 * setter methods.
 *
 * @author PoLYmer
 * @version 1.0
 * @since 2020-04-20
 *
 */

public class Flight {
    String mDepartureAirportCode;
    String mArrivalAirportCode;
    ConnectingLegs mConnectingLegArray = new ConnectingLegs();

    /**
     * Default constructor
     *
     * Constructor without params. Requires object fields to be explicitly
     * set using setter methods
     *
     * @pre None
     * @post member attributes are initialized to invalid default values
     */
    public Flight() {
        mDepartureAirportCode = "";
        mArrivalAirportCode = "";
        mConnectingLegArray = new ConnectingLegs();
    }

    public String departureAirportCode() {
        return mDepartureAirportCode;
    }

    public void departureAirportCode(String departureAirportCode) {

        mDepartureAirportCode = departureAirportCode;
    }

    public String arrivalAirportCode() {
        return mArrivalAirportCode;
    }

    public void arrivalAirportCode(String arrivalAirportCode) {
        mArrivalAirportCode = arrivalAirportCode;
    }

    public ConnectingLegs connectingLegArray() {
        return mConnectingLegArray;
    }

    public void connectingLegArray(ConnectingLegs connectingLegArray) {
        mConnectingLegArray = connectingLegArray;
    }

    /**
     * Transform to XML
     * TODO: Migrate to Reservation Class
     *
     * @pre None
     * @post member attributes are initialized to invalid default values
     */
    public String toXML() throws ParserConfigurationException {
        // 创建DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 创建DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();
        // 创建Document
        Document document = builder.newDocument();
        // 创建根节点
        Element flights = document.createElement("Flights");
        for (ConnectingLeg connectingLeg: mConnectingLegArray) {
            // 创建子节点，并设置属性
            Element flight = document.createElement("Flight");
            flight.setAttribute("number", connectingLeg.number());
            flights.appendChild(flight);
        }
        // 将根节点添加到Document下
        document.appendChild(flights);
        return document.toString();
    }

    public String toJSON(){
        StringBuilder json = new StringBuilder("[");
        for (ConnectingLeg connectingLeg: mConnectingLegArray) {
            json.append(connectingLeg.toJson());
            json.append(",");
        }
        json.append("]");
        return json.toString();
    }


}
