package LoggerFrame;

/**
 * Defines which loggers should run. The current Logger Framework offers three different logger policies: {@code Console},
 * {@code File} and {@code Discord}. Each one of these can be better understood in this enum's description of each field.
 * <br><br> <strong>An Important Note on Logger Policies!</strong>
 * <p>
 *     It is important to remember that logger policies are inclusive. This means that if you select the File policy,
 *     the Logger Framework will log to the file and to the console. If you choose the Discord policy, the framework
 *     will log to the discord server log channel, the file and to the console. Choosing the console policy will only
 *     log to the console. Log policies are inclusive of the lower logger priorities in the heirarchy.
 *     <pre>
 *         <strong> HEIRARCHY:</strong>
 *         -   Discord (Level 2).
 *         -   File (Level 1).
 *         -   Console (Level 0).
 *     </pre>
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
public enum LoggerPolicy {
    /**
     * Defines a policy which sends log messages to the {@link ConsoleLogger} only. The console logging policy is the
     * lowest in the heirarchy. As such, choosing this policy will only log to the console. This is not the recommended
     * logging policy -- it should only be used for debugging and development purposes, as the console is not always
     * available on runtime.
     */
    CONSOLE(0),

    /**
     * Defines a policy which sends log messages to the {@link ConsoleLogger} and to the {@link FileLogger}. The file
     * logging policy is the middle policy. Choosing this policy is recommended. While it does encompass the Console Logger,
     * more valuable is the ability to store data in the {@code logger.txt} file for viewing after runtime. It is important
     * to note that the {@link FileLogger} writes over the {@code logger.txt} each time the program runs. If you do not
     * want to loose the logging archive file, please rename it or move it before restarting the program.
     */
    FILE(1),

    /**
     * Defines a policy which sends log messages to the {@link ConsoleLogger}, {@link FileLogger}, and to the {@link DiscordLogger}.
     * The Discord logging policy should only be used for logging events or command responses that specifically relate
     * to a particular Discord guild. Bear in mind, if you choose this policy, it is important to provide the {@link LoggerCore}
     * with a valid Discord Server ID.
     */
    DISCORD(2);

    /**
     * The level of the enum constant in the hierarchical (0 - 2).
     */
    int value;

    /**
     * Used to keep ENUM values connected to their appropriate constants.
     * @param value
     * The integer hierarchical value to be matched with an enum constant.
     */
    LoggerPolicy(int value) {
            this.value = value;
    }

    /**
     * This is the method responsible for making all logging policies inclusive. This method will get a given enum's
     * hierarchical value and return an array of integers from zero to the hierarchical value.
     * @return int[] - The array of policy values that should be run with the given enum constant.
     */
    public int[] getLoggers() {
        switch (value) {
            case 0:
                return new int[]{0};
            case 1:
                return new int[]{1,0};
            case 2:
                return new int[]{2,1,0};
            default:
                return new int[]{-1};
        }
    }
}
