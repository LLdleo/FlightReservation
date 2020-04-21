package flight;

import java.util.ArrayList;

/**
 * This class aggregates a number of Flights. The aggregate is implemented as an ArrayList.
 * Airports can be added to the aggregate using the ArrayList interface. Objects can
 * be removed from the collection using the ArrayList interface.
 *
 * @author PoLYmer
 * @version 1.0
 * @since 2020-04-20
 *
 */

public class Flights{
    String mDepartureAirportCode;
    String mArrivalAirportCode;
    String mDepartureDate;
    String mArrivalDate;
    public ArrayList<Flight> mFlights;

    /**
     * Default constructor
     *
     * Constructor without params. Requires object fields to be explicitly
     * set using setter methods
     *
     * @pre None
     * @post member attributes are initialized to invalid default values
     */
    public Flights() {
        mDepartureAirportCode = "";
        mArrivalAirportCode = "";
        mDepartureDate = "";
        mArrivalDate = "";
        mFlights = new ArrayList<>();
    }


    public String departureAirportCode() {
        return mDepartureAirportCode;
    }

    public void departureAirportCode(String departureAirportCode) {
        this.mDepartureAirportCode = departureAirportCode;
    }

    public String arrivalAirportCode() {
        return mArrivalAirportCode;
    }

    public void arrivalAirportCode(String arrivalAirportCode) {
        this.mArrivalAirportCode = arrivalAirportCode;
    }

    public String departureDate() {
        return mDepartureDate;
    }

    public void departureDate(String departureDate) {
        this.mDepartureDate = departureDate;
    }

    public String arrivalDate() {
        return mArrivalDate;
    }

    public void arrivalDate(String arrivalDate) {
        this.mArrivalDate = arrivalDate;
    }

    public ArrayList<Flight> getFlights() {
        return mFlights;
    }

    public void setFlights(ArrayList<Flight> mFlights) {
        this.mFlights = mFlights;
    }

    public ArrayList<Flight> flights() {
        return mFlights;
    }

    public void flights(ArrayList<Flight> flights) {
        this.mFlights = flights;
    }


}
//public class Flights extends ArrayList<Flight> {
//    private static final long serialVersionUID = 1L;
//}
