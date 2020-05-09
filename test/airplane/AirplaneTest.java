package airplane;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import leg.SeatTypeEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test methods in Airplane class.
 * Significant associations: Airplane for the functionality being tested and WPI server for what data is provided and needs to be checked.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class AirplaneTest {
    /**
     * Test whether each constructor validates the arguments correctly.
     */
    @Test
    public void testConstructors() {
        String invalid = "";
        boolean success = false;
        try{
            new Airplane(invalid,"747",0,0);
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("man", invalid,0,0);
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane(invalid, "767", "0", "0");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer",invalid, "1", "2");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", "-1", "2");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", "1", "-2");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", "a", "2");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", "1", "b");
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", -2, 3);
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", 12, -786);
            Assert.fail();
        }catch (IllegalArgumentException e){
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", "1", "2");
            success = true;
        }catch (IllegalArgumentException e){
            Assert.fail();
        }
        Assert.assertTrue(success);
        success = false;
        try{
            new Airplane("manufacturer","A310", 1, 2);
            success = true;
        }catch (IllegalArgumentException e){
            Assert.fail();
        }
        Assert.assertTrue(success);
    }

    /**
     * Test whether setters validate the correct value.
     */
    @Test
    public void testValidityMethodAndSetters() {
        Airplane empty = new Airplane();
        Assert.assertFalse(empty.isValid());
        try{
            empty.firstClassSeats("a");
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals(0, (long) empty.firstClassSeats());
        }
        Assert.assertFalse(empty.isValid());
        empty.firstClassSeats("-1");
        Assert.assertEquals(0, (long) empty.firstClassSeats());
        Assert.assertFalse(empty.isValid());
        try{
            empty.firstClassSeats(-1);
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals(0, (long) empty.firstClassSeats());
        }

        Assert.assertFalse(empty.isValid());
        try{
            empty.coachSeats("a");
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals(0, (long) empty.coachSeats());
        }
        Assert.assertFalse(empty.isValid());
        empty.coachSeats("-19");
        Assert.assertEquals(0, (long) empty.coachSeats());

        Assert.assertFalse(empty.isValid());
        try{
            empty.coachSeats(-19);
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals(0, (long) empty.coachSeats());
        }
        Assert.assertFalse(empty.isValid());
        try{
            empty.model("");
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals("", empty.model());
        }
        Assert.assertFalse(empty.isValid());
        try{
            empty.manufacturer("");
            Assert.fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals("",  empty.manufacturer());
        }
    }

    /**
     * Test argument checking on setters.
     */
    @Test
    public void testValidSetters() {
        Airplane empty = new Airplane();
        empty.manufacturer("man");
        Assert.assertEquals("man",empty.getmManufacturer());
        Assert.assertFalse(empty.isValid());

        empty.model("mod");
        Assert.assertEquals("mod",empty.getmModel());
        Assert.assertTrue(empty.isValid());

        empty.firstClassSeats("1");
        Assert.assertEquals(1,(long)empty.getmFirstClassSeats());

        empty.coachSeats("2");
        Assert.assertEquals(2,(long) empty.getmCoachSeats());

        empty.firstClassSeats(4);
        empty.coachSeats(10);
        Assert.assertEquals(10, empty.getNumSeats(SeatTypeEnum.COACH));
        Assert.assertEquals(4, empty.getNumSeats(SeatTypeEnum.FIRSTCLASS));
    }

    /**
     * Test equals and compareTo methods.
     */
    @Test
    public void testComparisons() {
        Airplane air1 = new Airplane("man", "A320", 1, 3);
        Airplane air2 = new Airplane("man","a320",1,3);
        Assert.assertEquals(air1, air2);
        Assert.assertNotEquals(null, air1);
        Assert.assertEquals(0,air1.compareTo(air2));
    }
}
