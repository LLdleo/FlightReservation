package leg;

public class Departure {
    /* 3 character code of departing airport */
    String code;
    /* Date and time of departure in GMT */
    String time;

    public void departure(String code, String time) {
        this.code = code;
        this.time = time;
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

//    /** Departure Info */
//    public Departure(){
//        this.code = "";
//        this.time = "";
//    }
}
