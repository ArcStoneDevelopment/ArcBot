package LoggerFrame;

import net.dv8tion.jda.core.entities.Guild;

/**
 * Serves as a standardization gateway for {@link ConsoleLogger}, {@link DiscordLogger} and {@link FileLogger}. In order
 * for any of the three layers of logging to be accessed by the {@link LoggerCore}, each logger needs to use the same method
 * for logging, and it needs to be castable to the same type. This interfaces solves both of those problems. This also
 * leaves the logger framework open and very easy to expand in the future.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
interface Loggable {
    /**
     * As of now, this is the only logger method available. This method will produce output in the form:
     * <br> Standard Form: {@code [MMM dd, yyyy] - [hh:mm:ss aa] [[SERVER ID: <ID>]] {<Status>} <Message>}
     * <br> Real Example: {@code {Mar 15, 2018} - [12:32:00 AM] [[SERVER ID: -1]] {+} Bot Loaded!}
     * @param success
     * If the method fully achieved the expected outcome/made the expected changes this should be true. Otherwise, false.
     * @param serverID
     * The Guild object related to the {@link Utility.Server} object. This is required if the {@link LoggerPolicy} is
     * {@code Discord}. If the logger policy is not set to this, and/or the action carried out was not connected to a
     * particular server, this should be {@code null} (for standardization).
     * @param message
     * The String message to be logged. This should be short, reasonable and understandable. Furthermore, this should not
     * include a leading space.
     */
    void log(boolean success, Guild guild, String message);
}
