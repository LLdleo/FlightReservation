package timeUtils;

import java.util.Arrays;

public class Datetime {
    String year;
    String month;
    String day;
    String time;
    String timeZone;
    HourMinute hourMinute = new HourMinute();

    public void setDatetime(String year, String month, String day, String time, String timeZone, String hour, String minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.timeZone = timeZone;
        this.hourMinute.setHourMinute(hour, minute);
    }

    public void setDatetimeFromString(String datetime){
        String[] datetimeArray = datetime.split(" ");
        if (isValid(datetimeArray)) {
            String[] hmArray = datetimeArray[3].split(":");
            setDatetime(datetimeArray[0], datetimeArray[1], datetimeArray[2], datetimeArray[3], datetimeArray[4], hmArray[0], hmArray[1]);
        }
    }

    public boolean isValid(String[] array){
        return array.length == 5 & array[0].equals("2020") & array[2].length() == 2 & array[3].length() == 5;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public HourMinute getHourMinute() {
        return hourMinute;
    }
}