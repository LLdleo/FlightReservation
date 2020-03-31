package leg;

public class Seating {
    /* First class price as attribute */
    String firstClassPrice;
    /* Number of first class seats already reserved */
    int firstClassReserved;
    /* Coach price as attribute */
    String coachPrice;
    /* Number of coach seats already reserved */
    int coachReserved;
    /** Seating Info */
    public void seating(String firstClassPrice, Integer firstClassReserved, String coachPrice, Integer coachReserved) {
        this.firstClassPrice = firstClassPrice;
        this.firstClassReserved = firstClassReserved;
        this.coachPrice = coachPrice;
        this.coachReserved = coachReserved;
    }
}
