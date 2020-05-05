package dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import utils.Saps;

/**
 * @author Jackson Powell
 * @since 2020-05-05
 * Responsibilities: Test the server interface methods by calling them and checking that something is returned and that the method does as is documented
 * Significant associations: ServerInterface for the functionality being tested and wpi server for providing consistent information and responses for locking.
 */
public class ServerInterfaceTest {

    @Test
    public void testResetAndLocking() {
        Assert.assertTrue(ServerInterface.INSTANCE.reset(Saps.TEAMNAME));
        boolean success = ServerInterface.INSTANCE.lock(Saps.TEAMNAME);
        Assert.assertTrue(success);
        Assert.assertTrue(ServerInterface.INSTANCE.reset(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME));
        Assert.assertTrue(ServerInterface.INSTANCE.lock(Saps.TEAMNAME)); // Can't test lock conflicts on same system
        Assert.assertTrue(ServerInterface.INSTANCE.unlock(Saps.TEAMNAME));
    }
}
