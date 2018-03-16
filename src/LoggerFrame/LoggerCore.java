package LoggerFrame;

import Utility.Servers;
import net.dv8tion.jda.core.entities.Guild;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Provides a single access point for logging, regardless of {@link LoggerPolicy}. The LoggerCore acts as a "middleman"
 * or a daemon serving as a parser and connector for methods and Logging classes based on the annotation. This class also
 * holds instances of each of the Loggers. The Logger classes are not designed as singletons, but instantiating other instances
 * of the logger classes could cause errors.
 *
 * @author ArcStone Development LLC
 * @since v1.5
 * @version v1.5
 */
public class LoggerCore {
    /**
     * Once the {@link LoggerPolicy} has been parsed from the annotation, this hashmap is responsible for choosing the
     * proper logger object based on {@link LoggerPolicy} value. The loggers hashmap contains the only instances of the
     * logger classes that should ever be instantiated in the Logger Framework.
     */
    private static HashMap<Integer, Loggable> loggers = new HashMap<>(){{
        put (0, new ConsoleLogger());
        put (1, new DiscordLogger());
        put (2, new FileLogger());
    }};

    /**
     * This method does a number of things to correctly invoke the proper logging mechanisms.
     * <br><br><strong>The log() Process</strong>
     * <ol>
     *     <li>Determines if the {@link Logger} annotation is present.</li>
     *     <li>Gets the int[] array from the {@link Logger} annotation's {@link LoggerPolicy}.</li>
     *     <li>Invokes loggers from the {@code loggers} hashmap to match each int in the array.</li>
     * </ol>
     * @param method
     * This variable is very particular. Knowledge of reflection is by no means required to use the logger framework. The
     * exact universal code that should be sent as this parameter is {@code new Object(){}.getClass().getEnclosingMethod()}.
     * Using that code will give this logging method the required information to run the annotation parsing statements
     * correctly.
     * @param actionSuccess
     * A boolean to determine if the method/function being logged carried out its intended job correctly. Should be true
     * if it did, and no otherwise.
     * @param guild
     * The JDA guild object of the guild that is registered in {@link Servers}. This is required to be a proper guild if the
     * {@link LoggerPolicy} is set to {@code Discord}. All logging policies add the serverID to the log message, if it
     * is available/contextual -- providing this ID is ideal. If you choose to not provide a guild, fill this parameter
     * with {@code null}. This standardizes error checking and prevents random memory usages due to unnecessary parameters.
     * @param message
     * The actual message to be logged. You didn't think we forgot about this right? A leading space for formatting reasons
     * is NOT required.
     * @throws LoggerException
     * Exception thrown if anything goes wrong in the logging process. Because there are so many possible errors, it is
     * important to read the {@code .getMessage()} from the exception object if you get caught up with this exception.
     */
    public static void log(Method method, boolean actionSuccess, Guild guild, String message) throws LoggerException {
            if (!(method.isAnnotationPresent(Logger.class))) {
                throw new LoggerException("Logger annotation not present!");
            }
            Logger loggerRetention = method.getAnnotation(Logger.class);
            int[] logs = loggerRetention.value().getLoggers();
            for (int i : logs) {
                if ((i == 2) && (guild == null || !(Servers.activeServers.containsKey(guild.getIdLong())) ||
                    Servers.activeServers.get(guild.getIdLong()).drop)) {
                    throw new LoggerException("Error finding server for Discord LoggerPolicy");
                }
                loggers.get(i).log(actionSuccess, guild, message);
            }
    }
}
