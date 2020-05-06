package dao;

/**
 * Represent that an issue occurred when accessing the WPI server or the time service.
 * Associations: Does not rely on anything, but is used to represent and issue in the Server and timezone interfaces.
 *
 * @author Jackson Powell
 * @since 2020-05-0
 */
public class ServerAccessException extends Exception{
    /**
     * message is the message describing the reason for the exception.
     */
    private String message;

    /**
     * Constructor for ServerAccessException indication that the server was unable to be locked.
     *
     * @pre The system should have tried to access the server for some period of time before throwing this exception.
     * @post A ServerAccessException object will be instantiated that will need to be caught if thrown.
     * @param message The message describing any more details about this exception.
     */
    public ServerAccessException(String message){
        this.message = message;
    }

    /**
     * Return the string describing the cause of this exception.
     *
     * @return the string describing the cause of this exception.
     */
    public String toString(){
        return this.message;
    }
}
