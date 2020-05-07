package leg;

/**
 * Store information about a connecting leg's seating information.
 * Significant associations: WPI server for the structure of the data concerning seating information and how many seat types there are.
 *
 * @author Lidian Lin, Jackson Powell
 * @since 2020-04-22
 */
public class Seating {
    /** First class price as attribute */
    String firstClassPrice;

    /**
     * Get the number of first class seats reserved on this leg.
     *
     * @return the number of first class seats reserved on this leg.
     */
    public int getFirstClassReserved() {
        return firstClassReserved;
    }

    /**
     * Get the number of coach seats reserved on this leg.
     *
     * @return the number of coach seats reserved on this leg.
     */
    public int getCoachReserved() {
        return coachReserved;
    }

    /** Number of first class seats already reserved */
    int firstClassReserved;
    /** Coach price as attribute */
    String coachPrice;
    /** Number of coach seats already reserved */
    int coachReserved;

    /**
     * Constructor for seating information for a connecting leg.
     *
     * @pre The prices are in dollars and format as a valid decimal that may have a dollar sign, such as "$ 235.25
     * @post A Seating object is instantiated to hold the seating information for a connecting leg.
     * @param firstClassPrice String representation of the price of a first class seat on this connecting leg.
     * @param firstClassReserved Number of first class seats reserved on this connecting leg.
     * @param coachPrice String representation of the price of a coach seat on this connecting leg.
     * @param coachReserved Number of coach seats reserved on this connecting leg.
     */
    public Seating(String firstClassPrice, Integer firstClassReserved, String coachPrice, Integer coachReserved) {
        this.firstClassPrice = firstClassPrice;
        this.firstClassReserved = firstClassReserved;
        this.coachPrice = coachPrice;
        this.coachReserved = coachReserved;
    }

    /**
     * Parse and return the price of a first class seat in dollars.
     *
     * @pre The string representation of the price is a valid representation of a dollar amount, such as '$24.12' or '21.32'
     * @return The price of a first class seat in dollars
     */
    public double getFirstClassPrice(){
        return Double.parseDouble(this.firstClassPrice.replace('$',' ').replace(",",""));
    }
    /**
     * Parse and return the price of a coach seat in dollars.
     *
     * @pre The string representation of the price is a valid representation of a dollar amount, such as '$24.12' or '21.32'
     * @return The price of a coach seat in dollars
     */
    public double getCoachPrice(){
        return Double.parseDouble(this.coachPrice.replace('$',' ').replace(",",""));
    }

    /**
     * Get the number of seats reserved on a connecting leg for a certain type of seatType.
     *
     * @param seatType The type of seat to get the number reserved for.
     * @return The number of seats of the given seat type for a connecting leg.
     */
    public int getNumReserved(SeatTypeEnum seatType){
        if (seatType == SeatTypeEnum.FIRSTCLASS){
            return firstClassReserved;
        }
        return coachReserved;
    }

    public String toJSON() {
        return "{" +
                "firstClassPrice:\"" + firstClassPrice +
                "\", firstClassReserved:\"" + firstClassReserved +
                "\", coachPrice:\"" + coachPrice  +
                "\", coachReserved:\"" + coachReserved +
                "\"}";
    }
}
