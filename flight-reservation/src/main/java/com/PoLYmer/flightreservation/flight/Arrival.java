package com.PoLYmer.flightreservation.flight;

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
    public Arrival(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
