package leg;

/**
 * Store information about a connecting leg's departure event.
 * Significant associations: The structure of the data in the WPI server for the departure event information of a connecting leg.
 *
 * @author Lidian Lin, Jackson Powell
 * @since 2020-04-23
 */
public class Departure {
    /** 3 character code of departing airport */
    String code;



    /** Date and time of departure in GMT
     * @see utils.Saps TIME_FORMAT for the expected format of this varialbe*/
    String time;

    /**
     * Constructor for Departure.
     *
     * @pre The airport code and time have already been validated.
     * @post A Departure object is instantiated to represent a leg's departure event by referring to keys, not objects.
     * @see utils.Saps TIME_FORMAT for the format of the time parameter in TIME_FORMAT
     * @param code The 3-letter code of the airport that the leg is departing from.
     * @param time The string representation of the time of this departure event.
     */
    public Departure(String code, String time) {
        this.code = code;
        this.time = time;
    }

    /**
     * Get the string representation of the time of the departure.
     *
     * @return the string representation of the time of the departure.
     */
    public String getTime() {
        return time;
    }

    /**
     * Get the code for the airport that the leg is departing from.
     *
     * @return the code for the airport that the leg is departing from.
     */
    public String getCode() {
        return code;
    }

//    /** Departure Info */
//    public Departure(){
//        this.code = "";
//        this.time = "";
//    }
}
