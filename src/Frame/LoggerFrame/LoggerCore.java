package Frame.LoggerFrame;

import Utility.Servers;
import net.dv8tion.jda.core.entities.Guild;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides a single access point for logging, regardless of {@link LoggerPolicy}. The LoggerCore acts as a "middleman"
 * or a daemon serving as a parser and connector for methods and Logging classes based on the annotation. This class also
 * holds instances of each of the Loggers. The Logger classes are not designed as singletons, but instantiating other instances
 * of the logger classes could cause errors.
 * <br><br> <Strong>What is method inference?</Strong>
 * <p> Typically, the logger framework calls make you pass in a parameter which looks like this: {@code new Object(){}.getClass().getEnclosingMethod()}.
 * We find this to be cumbersome and unnecessary in most cases. Method inference allows the calling method to be inferred, meaning
 * you can leave out this extra parameter. However, there are a few situations where you cannot use method inference and still
 * need to include this extra parameter.
 * <br><br> <strong>When can you use method inference?</strong>
 * <p> The following conditions must be met to use method inference:
 * <ol>
 *     <li>The method must be public.</li>
 *     <li>Only one method in the same class with the same name has a logger annotation. (Multiple methods with the same name are
 *     eligible for method inference if and only if only one of those methods has a logger annotation.)</li>
 * </ol>
 * <p> The following is an example of code that will not work with method inference because two methods have the same name and
 * both have the logger annotation. If only one had the logger annotation (remove the annotation from one of the two methods) the
 * code would run fine.:
 * <pre>
 *     {@literal @}{@code Logger (LoggerPolicy.DISCORD)
 *     public void method(int parameter) {
 *         //Other code here
 *         LoggerCore.log(true, guild, "Log this!");
 *     }
 *     }
 *     {@literal @}{@code Logger (LoggerPolicy.DISCORD)
 *     public void method(String parameter) {
 *         //Other code here
 *         LoggerCore.log(true, guild, "Log this!");
 *     }
 *     }</pre>
 *
 * @author ArcStone Development LLC
 * @since v1.5
 * @version v2.0
 */
public class LoggerCore {
    /**
     * Once the {@link LoggerPolicy} has been parsed from the annotation, this hashmap is responsible for choosing the
     * proper logger object based on {@link LoggerPolicy} value. The loggers hashmap contains the only instances of the
     * logger classes that should ever be instantiated in the Logger Framework.
     */
    private static HashMap<Integer, Loggable> loggers = new HashMap<>(){{
        put (0, new ConsoleLogger());
        put (2, new DiscordLogger());
        put (1, new FileLogger());
    }};

    /**
     * Internal method for method reflection inference.
     * @return Method - the reflected method object representing the method which called the logging framework.
     * @throws LoggerException
     * Thrown if there is a {@code ClassNotFoundException}, if the method cannot be accurately inferred, or if the logger
     * annotation is not present on the inferred method. For an accurate cause, please call the {@code .getMessage()} on the
     * exception object.
     */
    private static Method parseMethod() throws LoggerException {
        try {
            ArrayList<Method> methods = new ArrayList<>(
                    List.of(Class.forName(Thread.currentThread().getStackTrace()[3].getClassName()).getMethods()));
            String name = Thread.currentThread().getStackTrace()[3].getMethodName();
            for (Method m : new ArrayList<>(methods)) {
                if (!(m.getName().equals(name))) {
                    methods.remove(m);
                }
            }
            for (Method m : new ArrayList<>(methods)) {
                if (!(m.isAnnotationPresent(Logger.class))) {
                    methods.remove(m);
                }
            }
            if (methods.size() == 0) {
                throw new LoggerException("Logger annotation not found!");
            } else if (methods.size() > 1) {
                throw new LoggerException("Calling method could not be inferred!");
            }
            return methods.get(0);
        } catch (ClassNotFoundException e) {
            throw new LoggerException("Class Not Found!");
        }
    }

    /**
     * Properly invokes logging mechanisms for classes which are eligible for method inference who are logging with the
     * {@code DISCORD} policy. If the requirements for inference are not met, use a direct call with the enclosing method command.
     * @param actionSuccess
     * A boolean to determine if the method/function being logged carried out its intended job correctly. Should be true
     * if it did, and no otherwise.
     * @param guild
     * The guild that is associated with the action being logged. This should never be null. If you do not wish to include
     * a guild, use the other version of this method rather than setting this parameter to null. This parameter is required
     * for the {@code Discord} {@link LoggerPolicy}. Failure to provide this when this LoggerPolicy is used will result
     * in a {@link LoggerException}.
     * @param message
     * The actual message to be logged. You didn't think we forgot about this right? A leading space for formatting reasons
     * is NOT required.
     * @throws LoggerException
     * If the method cannot be inferred accurately for reflection, or if the DISCORD policy is not specified, a logger
     * exception may be thrown. Because there are so many points of error in a framework this complex, it is important to
     * read the {@code .getMessage()} for the logger exception to pinpoint the exact cause of error.
     */
    public static void log(boolean actionSuccess, Guild guild, String message) throws LoggerException {
        log(parseMethod(), actionSuccess, guild, message);
    }

    /**
     * Properly invokes logging mechanisms for classes which are eligible for method inference who are not logging with
     * the {@code DISCORD} policy.  If the requirements for inference are not met, use a direct call with the enclosing method command.
     * @param actionSuccess
     * A boolean to determine if the method/function being logged carried out its intended job correctly. Should be true
     * if it did, and no otherwise.
     * @param message
     * The actual message to be logged. You didn't think we forgot about this right? A leading space for formatting reasons
     * is NOT required.
     * @throws LoggerException
     * If the method cannot be inferred accurately for reflection, or if the DISCORD policy is specified, a logger
     * exception may be thrown. Because there are so many points of error in a framework this complex, it is important to
     * read the {@code .getMessage()} for the logger exception to pinpoint the exact cause of error.
     */
    public static void log(boolean actionSuccess, String message) throws LoggerException {
        log(parseMethod(), actionSuccess, null, message);
    }

    /**
     * The core logging mechanism method. All other {@code log()} method implementations refer to this one.
     * <br> This method does a number of things to correctly invoke the proper logging mechanisms for logging.
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
     * The guild that is associated with the action being logged. This should never be null. If you do not wish to include
     * a guild, use the other version of this method rather than setting this parameter to null. This parameter is required
     * for the {@code Discord} {@link LoggerPolicy}. Failure to provide this when this LoggerPolicy is used will result
     * in a {@link LoggerException}.
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
            int[] logs = method.getAnnotation(Logger.class).value().getLoggers();
            for (int i : logs) {
                if ((i == 2) && (guild == null || !(Servers.activeServers.containsKey(guild.getIdLong())) ||
                    Servers.activeServers.get(guild.getIdLong()).drop)) {
                    throw new LoggerException("Error finding server for Discord LoggerPolicy");
                }
                loggers.get(i).log(actionSuccess, guild, message);
            }
    }

    /**
     * Refer to the primary {@code log()} method without sending a Guild parameter. TThis method has
     * a null-handler built in to allow the {@code Guild} parameter to not be required. The caveat with this method is
     * that any methods that have a {@link LoggerPolicy} of {@code Discord} that call this method will get a {@link LoggerException}
     * because there is never a declared guild object to log to.
     * @param method
     * This variable is very particular. Knowledge of reflection is by no means required to use the logger framework. The
     * exact universal code that should be sent as this parameter is {@code new Object(){}.getClass().getEnclosingMethod()}.
     * Using that code will give this logging method the required information to run the annotation parsing statements
     * correctly.
     * @param actionSuccess
     * A boolean to determine if the method/function being logged carried out its intended job correctly. Should be true
     * if it did, and no otherwise.
     * @param message
     * The actual message to be logged. You didn't think we forgot about this right? A leading space for formatting reasons
     * is NOT required.
     * @throws LoggerException
     * Exception thrown if anything goes wrong in the logging process. Because there are so many possible errors, it is
     * important to read the {@code .getMessage()} from the exception object if you get caught up with this exception.
     */
    public static void log(Method method, boolean actionSuccess, String message) throws LoggerException {
        log(method, actionSuccess, null, message);
    }

    /**
     * This is a special logging mechanic that should only be invoked inside catch statements for the LoggerException.
     * The {@link LoggerException} is a strange situation. The problem is, if there is an issue with the logger, how are
     * you supposed to log the issue? This method provides a direct exception-free connection to the {@link ConsoleLogger}
     * specifically to log {@link LoggerException} messages. This should only be used inside the catch block for this
     * exception.
     * @param e
     * The LoggerException that is preventing logging from occurring.
     */
    public static void exceptionLogger(LoggerException e) {
        ConsoleLogger consoleLogger = (ConsoleLogger)loggers.get(0);
        consoleLogger.log(false, null, "EXCEPTION: " + e.getMessage());
    }
}
