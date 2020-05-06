package airport;

import airplane.Airplane;
import leg.SeatTypeEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Airport class
 * Significant associations: Airport for the functionality being tested and WPI server for the structure of the data available.
 *
 * @author Jackson Powell
 * @since 2020-05-05
 */
public class AirportTest {
    @Test
    public void testConstructors() {
        String invalid = "";
        boolean success = false;
        try {
            new Airport(invalid, "IAD", 0, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Houston", invalid, 0, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport(invalid, "CLE", "0", "0");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Washington", invalid, "1", "2");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Washington", "as", "1", "2");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Washington", "four", "1", "2");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", "-100", "2");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", "1", "-200");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", "a", "2");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", "1", "b");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", -90.1, 3);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", 12, -181);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", "1", "2");
            success = true;
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
        Assert.assertTrue(success);
        success = false;
        try {
            new Airport("Cleveland", "CLE", 1, 2);
            success = true;
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testValidityMethodAndSetters() {
        Airport empty = new Airport();
        Assert.assertFalse(empty.isValid());
        try {
            empty.latitude("a");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.latitude(),1);
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.latitude("-110");
        }catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.latitude(),1);
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.latitude(-120);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.latitude(),1);
        }

        Assert.assertFalse(empty.isValid());
        try {
            empty.longitude("a");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.longitude(),1);
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.longitude("-190");
        }catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.longitude(),1);
        }

        Assert.assertFalse(empty.isValid());
        try {
            empty.longitude(-189);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(Double.MAX_VALUE,  empty.longitude(),1);
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.code("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.code());
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.code("23");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.code());
        }
        Assert.assertFalse(empty.isValid());
        try {
            empty.name("");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("", empty.name());
        }
    }

    @Test
    public void testValidSetters() {
        Airport empty = new Airport();
        empty.name("man");
        Assert.assertEquals("man", empty.getmName());
        Assert.assertFalse(empty.isValid());

        empty.code("BOS");
        Assert.assertEquals("BOS", empty.getmCode());
        Assert.assertFalse(empty.isValid());

        empty.longitude("1.21");
        Assert.assertEquals(1.21, empty.getmLongitude(),0);
        Assert.assertFalse(empty.isValid());

        empty.latitude("2");
        Assert.assertEquals(2, empty.getmLatitude(),0);
        Assert.assertTrue(empty.isValid());
    }

    @Test
    public void testComparisons() {
        Airport air1 = new Airport("Boston", "BOS", 1, 3);
        Airport air2 = new Airport("Boston", "BOS", 1, 3);
        Assert.assertEquals(air1, air2);
        Assert.assertNotEquals(null, air1);
        Assert.assertEquals(0, air1.compareTo(air2));
    }
}

