package leg;

import airport.Airport;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test ConnectingLeg object.
 * Significant associations: ConnectingLeg for the functionality being tested and WPI server for the structure of the data that ConnectingLeg is based on.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class ConnectingLegTest {
    /**
     * Test validity checkers in the constructor that takes integers for the number of reserved seats.
     */
    @Test
    public void testConstructorWithIntegers() {
        String invalid = "";
        // new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
        boolean success = false;
        try {
            new ConnectingLeg(invalid, "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", invalid, "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", invalid, "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "854332", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", invalid, "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLEe", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CL", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", invalid, "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", invalid, "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DeCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCAe", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", invalid, "28.42", 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", invalid, 2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", -2, "14.78", 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, invalid, 129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", -129);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        try{
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
        }catch (Exception e){
            Assert.fail();
        }
    }

    /**
     * Test validity checkers for the constructor that takes strings for the number of seats reserved,
     */
    @Test
    public void testConstructorWithOutIntegers() {
        String invalid = "";
        // new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", 2, "14.78", 129);
        boolean success = false;
        try {
            new ConnectingLeg(invalid, "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", invalid, "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", invalid, "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "854332", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", invalid, "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLEe", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CL", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", invalid, "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", invalid, "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DeCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCAe", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", invalid, "28.42", "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", invalid, "2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "-2", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", invalid, "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "-129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "bf", "14.78", "129");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "a");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        try {
            new ConnectingLeg("767", "39", "8543", "CLE", "2020 May 10 23:38", "DCA", "2020 May 11 00:17", "28.42", "2", "14.78", "129");
        }
        catch (Exception e){
            Assert.fail();
        }
    }

    /**
     * Test validity checkers in setters and that getters return the correct data.
     */
    @Test
    public void testSettersGetters() {
        ConnectingLeg empty = new ConnectingLeg();
        try {
            empty.number("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.number());
        }
        try {
            empty.number("233");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.number());
        }
        try {
            empty.number("214123");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.number());
        }
        try {
            empty.airplane("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.airplane());
        }
        try {
            empty.flightTime("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.flightTime());
        }
        empty.arrival(new Arrival("BOS", "2020 May 23 23:03"));
        Assert.assertEquals("BOS",empty.arrival().getCode());
        Assert.assertEquals("2020 May 23 23:03", empty.arrival().getTime());
        empty.departure(new Departure("IAD", "2020 May 30 13:45"));
        Assert.assertEquals("IAD",empty.departure().getCode());
        Assert.assertEquals("2020 May 30 13:45", empty.departure().getTime());
        empty.seating(new Seating("24.12",104, "12.31",200));
        Assert.assertEquals(24.12,empty.seating().getFirstClassPrice(),0);
        Assert.assertEquals(104,empty.seating().getFirstClassReserved());
        Assert.assertEquals(12.31,empty.seating().getCoachPrice(),0);
        Assert.assertEquals(200,empty.seating().getCoachReserved());
        Assert.assertEquals(104,empty.seating().getNumReserved(SeatTypeEnum.FIRSTCLASS));
        Assert.assertEquals(200,empty.seating().getNumReserved(SeatTypeEnum.COACH));
        Assert.assertFalse(empty.isValid());
        empty.airplane("767");
        Assert.assertFalse(empty.isValid());
        Assert.assertEquals("767",empty.airplane());
        empty.flightTime("43");
        Assert.assertFalse(empty.isValid());
        Assert.assertEquals("43",empty.flightTime());
        empty.number("4522");
        Assert.assertTrue(empty.isValid());
        Assert.assertEquals("4522",empty.number());
        Assert.assertFalse(empty.hasAnySeatsLeft());
        empty.seating(new Seating("24.12",103, "12.31",200));
        Assert.assertTrue(empty.hasAnySeatsLeft());
        empty.seating(new Seating("24.12",104, "12.31",199));
        Assert.assertTrue(empty.hasAnySeatsLeft());
    }
}
