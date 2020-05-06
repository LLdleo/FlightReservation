package time;

import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Test time functions in MyTime, such as calculating timespan or local time.
 *
 * @author Jackson Powell
 * @since 2020-04-22
 */
public class MyTimeTest {

    @Test
    public void testTimespan() {
        MyTime time1 = new MyTime("2020 May 10 20:45");
        MyTime time2 = new MyTime("2020 May 11 00:15");
        double expectedDiff = 3.5;
        double actualDiff = time1.timespan(time2);
        Assert.assertEquals(expectedDiff,actualDiff,0);
        double flippedDiff = time2.timespan(time1);
        Assert.assertEquals(-expectedDiff,flippedDiff,0);
        double noDiff = time1.timespan(time1);
        Assert.assertEquals(0,noDiff,0);
    }

    @Test
    public void testLongTimespan() {
        MyTime time1 = new MyTime("2020 May 10 20:45");
        MyTime time2 = new MyTime("2020 May 12 05:00");
        double expectedDiff = 32.25;
        double actualDiff = time1.timespan(time2);
        Assert.assertEquals(expectedDiff,actualDiff,0);
    }

    @Test
    public void testParseServerDate() {
        String parseString = "2020 May 10 20:12";
        LocalDateTime actual = MyTime.parseServerDateTimeString(parseString);
        LocalDateTime expected = LocalDateTime.of(2020,5,10,20,12);
        Assert.assertTrue(expected.isEqual(actual));

        parseString = "2021 Feb 28 02:07";
        actual = MyTime.parseServerDateTimeString(parseString);
        expected = LocalDateTime.of(2021,2,28,2,7);
        Assert.assertTrue(expected.isEqual(actual));
    }

    @Test
    public void testDayTimespans() {
        String from = "2020 May 11 00:00";
        MyTime fromTest = new MyTime(from);
        double expectedLast = 0;
        double expectedNext = 24;
        double actualLast = fromTest.getTimeToLastDay();
        double actualNext = fromTest.getTimeToNextDay();
        Assert.assertEquals(expectedLast,actualLast,0);
        Assert.assertEquals(expectedNext,actualNext,0);

        from = "2020 May 16 23:59";
        fromTest = new MyTime(from);
        expectedLast = 23.98;
        expectedNext = 1.0/60;
        actualLast = fromTest.getTimeToLastDay();
        actualNext = fromTest.getTimeToNextDay();
        Assert.assertEquals(expectedLast,actualLast,.01);
        Assert.assertEquals(expectedNext,actualNext,.01);

        from = "2020 May 20 17:15";
        fromTest = new MyTime(from);
        expectedLast = 17.25;
        expectedNext = 6.75;
        actualLast = fromTest.getTimeToLastDay();
        actualNext = fromTest.getTimeToNextDay();
        Assert.assertEquals(expectedLast,actualLast,0);
        Assert.assertEquals(expectedNext,actualNext,0);
    }

    @Test
    public void testCalculateLocalTime() {
        try {
            LocalDateTime gmtTime = LocalDateTime.of(2020, 5, 12, 8, 0);
            String gmtString = "2020 May 12 08:00";
            double sampleLatitude = 40.000158;
            double sampleLongitude = -82.887198;
            // double expectedOffset = -5;
            MyTime dateTime = new MyTime(gmtTime, sampleLatitude, sampleLongitude);
            MyTime stringTime = new MyTime(gmtString,sampleLatitude,sampleLongitude);
            LocalDateTime expectedLocal = LocalDateTime.of(2020,5,12, 3,0);
            Assert.assertTrue(dateTime.getGmtTime().isEqual(stringTime.getGmtTime()));
            Assert.assertTrue(dateTime.getLocalTime().isEqual(stringTime.getLocalTime()));
            Assert.assertTrue(dateTime.getLocalTime().isEqual(expectedLocal));

            gmtTime = LocalDateTime.of(2020, 4, 30, 20, 45);
            gmtString = "2020 Apr 30 20:45";
            sampleLatitude = 29;
            sampleLongitude = 76;
            // double expectedOffset = 5.5;
            dateTime = new MyTime(gmtTime, sampleLatitude, sampleLongitude);
            stringTime = new MyTime(gmtString,sampleLatitude,sampleLongitude);
            expectedLocal = LocalDateTime.of(2020,5,1, 2,15);
            Assert.assertTrue(dateTime.getGmtTime().isEqual(stringTime.getGmtTime()));
            Assert.assertTrue(dateTime.getLocalTime().isEqual(stringTime.getLocalTime()));
            Assert.assertTrue(dateTime.getLocalTime().isEqual(expectedLocal));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
    @Test
    public void testParameterExceptions() {
        boolean success = false;
        LocalDateTime sampleDay = LocalDateTime.of(2020,5,10,2,32);
        String sample = "2020 May 02 02:15";
        try{
            new MyTime(sampleDay,90.01,2);
            Assert.fail();
        }
        catch(IllegalArgumentException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);

        success = false;
        try{
            new MyTime(sample,25,-181);
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);

        success = false;
        try{
            new MyTime(sampleDay,25,181);
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);

        success = false;
        try{
            new MyTime(sample,-100,12);
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            success = true;
        }
        catch (Exception e){
            Assert.fail();
        }
        Assert.assertTrue(success);

    }
}
