package Utility;

/**
 * Defines the SyntaxException. This exception is used to indicate an error during the runtime of a command and communicate
 * the cause back to the primary execution method for a proper response to be sent by the bot. The integer {@code cause} value
 * is different for each command, so the meaning of each integer should be properly explained in the javadocs of each
 * command in {@link Discord.Commands}.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
public class SyntaxException extends Exception {
    /**
     * The integer {@code cause} value for this exception.
     */
    private int cause;

    /**
     * Construct a new SyntaxException requiring an integer {@code cause} value.
     * @param cause
     * The integer {@code cause} value.
     */
    public SyntaxException(int cause) {
        this.cause = cause;
    }

    /**
     * Access the integer {@code cause} value.
     * @return int
     */
    public int getIntCause() {
        return cause;
    }
}
