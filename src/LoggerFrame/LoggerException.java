package LoggerFrame;

/**
 * The Logger Exception is the Logger Framework's way of responding to a variety of cases that may prevent proper logging
 * from occurring. Each message that is appended to the Exception object is informative and explains the exact details
 * surrounding the exception and its cause.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
public class LoggerException extends Exception {

    /**
     * This constructor does nothing more than call the {@code Exception} constructor.
     * @param message
     * The exception's error message to be attached at the top of the stacktrace.
     */
    public LoggerException(String message) {
        super(message);
    }

    /**
     * Provides quick access to the exception's error message. Before doing any other debugging, please be sure to
     * read this. All the current framework LoggerException {@code throw} statements have very concise, descriptive
     * error messages.
     * @return String - The exception's error message.
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
