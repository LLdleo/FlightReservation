package reservation;

/**
 * @author Jackson Powell
 * @since 2020-04-24
 * Responsibilities: Indicate that the system was able to obtain the server lock after some amount of time.
 */
public class ServerLockException extends Exception {
    /**
     * message is the message describing the meaning of this exception.
     */
    private String message;

    /**
     * Constructor for ServerLockException indication that the server was unable to be locked.
     *
     * @see utils.Saps For the number of seconds after which the system will timeout when trying to obtain the server lock.
     * @pre The system should have tried to acquire the lock for some period of time before throwing this exception.
     * @post A ServerLockException object will be instantiated that will need to be caught if thrown.
     * @param message The message describing any more details about this exception.
     */
    public ServerLockException(String message){
        this.message = message;
    }

    /**
     * Return the string describing the cause of this exception.
     *
     * @return the string describing the cause of this exception.
     */
    public String toString(){
        return "System could not lock WPI server: " + this.message;
    }
}
