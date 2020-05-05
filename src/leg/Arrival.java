package leg;

/**
 * @author Lidian Lin, Jackson Powell
 * @since 2020-04-24
 * Responsibilities: Store information about a connecting leg's arrival event.
 * Significant associations: The data provided by the WPI server for a connecting leg's arrival information.
 */
public class Arrival {
    /** 3 character code of arrival airport */
    String code;



    /** Date and time of arrival in GMT
     * @see utils.Saps TIME_FORMAT for the format expected for this field.
     * */
    String time;
    /**
     * Constructor for Arrival.
     *
     * @pre The airport code and time have already been validated.
     * @post An Arrival object is instantiated to represent a leg's arrival event by referring to keys, not objects.
     * @see utils.Saps for the format of the time parameter in TIME_FORMAT
     * @param code The 3-letter code of the airport that the leg is arriving at.
     * @param time The string representation the time of this arrival event.
     */
    public Arrival(String code, String time) {
        this.code = code;
        this.time = time;
    }
    /**
     * Get the code for the airport that the leg is departing from.
     *
     * @return the code for the airport that the leg is departing from.
     */
    public String getCode() {
        return code;
    }
    /**
     * Get the string representation of the time of the arrival.
     *
     * @return the string representation of the time of the arrival.
     */
    public String getTime() {
        return time;
    }
}
