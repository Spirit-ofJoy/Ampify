package ampify_player;

/**
 * An exception while reading an SRT file.
 *
 * @author Sayed Zameer Qasim
 */

public class SRTReaderException extends ampify_player.SRTException {

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SRTReaderException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param message the exception message
     */
    public SRTReaderException(String message) {
        super(message);
    }
    /**
     * @param cause the cause
     */
    public SRTReaderException(Throwable cause) {
        super(cause);
    }
}
