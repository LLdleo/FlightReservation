package datetime;

public class Datetime {
    String year;
    String month;
    String day;
    String time;
    String timeZone;

    public void setDatetime(String year, String month, String day, String time, String timeZone){
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.timeZone = timeZone;
    }

    public void setDatetimeFromString(String datetime){
        String[] datetimeArray = datetime.split(" ");
        if (isValid(datetimeArray)) {
            setDatetime(datetimeArray[0], datetimeArray[1], datetimeArray[2], datetimeArray[3], datetimeArray[4]);
        }
    }

    public boolean isValid(String[] array){
        return array.length == 5 & array[0].equals("2020") & array[2].length() == 2 & array[3].length() == 5;
    }
}