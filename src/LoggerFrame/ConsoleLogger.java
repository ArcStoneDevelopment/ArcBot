package LoggerFrame;

import Utility.SystemTime;
import net.dv8tion.jda.core.entities.Guild;

/**
 * The console logger is a wrapper for the basic {@code System.out} java print streams. This allows the normal print
 * to play "nicely" with the logger framework and allows standard formatting of messages.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
class ConsoleLogger implements Loggable {
    /**
     * Log the given message to the {@code System.out} stream if {@code success}, otherwise, log to the {@code System.err}
     * stream. If the guild parameter is not null, it is appended to the log message.
     * <br><br> <strong>Message Format</strong>
     * <br> Standard Form: {@code [MMM dd, yyyy] - [hh:mm:ss aa] <SERVER ID: <ID>> {<Status>} <Message>}
     * <br> Real Example: {@code {Mar 15, 2018} - [12:32:00 AM] <SERVER ID: -1> {+} Bot Loaded!}
     * @param success
     * If the method fully achieved the expected outcome/made the expected changes this should be true. Otherwise, false.
     * @param guild
     * The Guild object related to the {@link Utility.Server} object. This is required if the {@link LoggerPolicy} is
     * {@code Discord}. If the logger policy is not set to this, and/or the action carried out was not connected to a
     * particular server, this should be {@code null} (for standardization).
     * @param message
     * The String message to be logged. This should be short, reasonable and understandable. Furthermore, this should not
     * include a leading space.
     */
    @Override
    public void log(boolean success, Guild guild, String message) {
        long guildID;
        if (guild == null) {
            guildID = -1;
        } else {
            guildID = guild.getIdLong();
        }

        if (success) {
            System.out.println(SystemTime.getTime() + "<SERVER ID: " + guildID + "> {+} " + message);
        } else {
            System.err.println(SystemTime.getTime() + "<SERVER ID: " + guildID + "> {-} " + message);
        }
    }
}
