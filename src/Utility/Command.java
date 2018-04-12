package Utility;

import Frame.BotFrame.CommandBox;
import Frame.LoggerFrame.Logger;
import Frame.LoggerFrame.LoggerCore;
import Frame.LoggerFrame.LoggerException;
import Frame.LoggerFrame.LoggerPolicy;

import java.io.Serializable;

/**
 * Defines methods expected for public use in all classes that represent Discord commands. Each command is casted back to
 * an instance of this type for use -- any extra public methods added to command classes need to be declared here in order
 * to be available for use.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public interface Command extends Serializable {

    CommandInfo getInfo();
    /**
     * Access the invoke key for this command. In order for a given command to run, the {@link Frame.BotFrame.Listeners.MessageListener}
     * compares the invoke key from a {@link CommandBox} with this invoke key. This String should be the expected invoke
     * key for a command, in all lowercase.
     * @return String - Invoke
     */
    String getInvoke();

    /**
     * Provides all execution code to be run when this command is called. This is the entry point for execution. The
     * original {@link CommandBox} is passed as an argument containing all the information that the parser has gathered
     * about the event. However, it is the responsibility of the execution method to check permission requirements, perform
     * tasks, and send responses. Private sub-methods may be used to delegate sub-commands out of the {@code execute()} method.
     * @param command
     * The {@link CommandBox} surrounding the JDA Message Event that called this command.
     * @return boolean - If the {@code execute()} method performed the desired outcome of a command, this should be true. If the
     * method had any issues (ex. permission errors, syntax errors, etc), this should be false.
     */
    boolean execute(CommandBox command);

    /**
     * Default logging method for commands. This method does not have to be overridden. Overriding this is not recommended
     * as it removes the standardization provided by the default method structure. This method invokes the {@link Frame.LoggerFrame}
     * framework to log a message, and the formats for the output messages are avilable in those javadocs.
     * @param executed
     * The boolean value returned by the {@code execute()} method.
     * @param command
     * The same {@link CommandBox} that called the running of the {@code execute()} method.
     */
    @Logger(LoggerPolicy.DISCORD)
    default void log(boolean executed, CommandBox command) {
        try {
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), executed, command.getEvent().getGuild(),
                    command.getEvent().getMember().getEffectiveName() + " executed `" + command.getRaw() + "`");
        } catch (LoggerException e) {
            LoggerCore.exceptionLogger(e);
        }
    }

}
