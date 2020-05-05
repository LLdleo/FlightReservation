package leg;

import airport.Airport;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test ConnectingLeg object.
 * Significant associations: ConnectingLeg for the functionality being tested and WPI server for the structure of the data that ConnectingLeg is based on.
 */
public class ConnectingLegTest {

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
}
