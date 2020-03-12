package flight;

public class Arrival {
    /* 3 character code of arrival airport */
    String code;
    /* Date and time of arrival in GMT */
    String time;
    /** Arrival Info */
    public void arrival(String code, String time) {
        this.code = code;
        this.time = time;
    }
}
