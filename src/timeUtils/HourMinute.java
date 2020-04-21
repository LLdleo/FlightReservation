package timeUtils;

public class HourMinute {
    String hour;
    String minute;

    public void setHourMinute(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return toInt(hour);
    }

    public int getMinute() {
        return toInt(minute);
    }

    public int toInt(String string){
        return Integer.parseInt(string);
    }
}
