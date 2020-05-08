package leg;

/**
 * Enumerate possible seat types.
 * Significant associations: WPI server for the types of seats provided on their flights.
 *
 * @author Jackson Powell
 * @since 2020-04-21
 */
public enum SeatTypeEnum {
    /**
     * COACH represents the seat type of coach seating.
     */
    COACH,
    /**
     * FIRSTCLASS represents the seat type of first class seating.
     */
    FIRSTCLASS;

    /**
     * Return a string representation of whichever enum is selected.
     *
     * @return a string representation of the value of this enum object.
     */
    public String toString(){
        return this == SeatTypeEnum.COACH ? "coach" : "first class";
    }
}
