package ampify_player;

/**
 * Any exceptions related to SRT.
 *
 * @author Sayed Zameer Qasim
 */
public class SRTException extends RuntimeException{
    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SRTException(String message, Throwable cause) {
        super(message,cause);
    }
    /**
     * @param message the exception message
     */
    public SRTException(String message) {
        super(message);
    }
    /**
     * @param cause the cause
     */
    public SRTException(Throwable cause) {
        super(cause);
    }
}
