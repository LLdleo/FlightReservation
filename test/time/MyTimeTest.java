package time;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author Jackson Powell
 * @since 2020-04-22
 * Responsibilities: Test time functions in MyTime, such as calculating timespan or local time.
 */
public class MyTimeTest {

    @Test
    public void testTimespan() {
        MyTime time1 = new MyTime("2020 May 10 20:45",0,0);
        MyTime time2 = new MyTime("2020 May 11 00:15",0,0);
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
        MyTime time1 = new MyTime("2020 May 10 20:45",0,0);
        MyTime time2 = new MyTime("2020 May 12 05:00",0,0);
        double expectedDiff = 32.25;
        double actualDiff = time1.timespan(time2);
        Assert.assertEquals(expectedDiff,actualDiff,0);
    }
}
