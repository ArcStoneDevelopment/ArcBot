package Discord.Commands;

import Utility.Command;
import Frame.BotFrame.CommandBox;
import Utility.Servers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @deprecated
 * Provides the {@code -stop} command.
 * <br> This command is used for safe shutdown of the bot. It forces saving up the {@link Servers} map to SQL and then
 * exits the program. This command is unsafe in proper/public implementations of the bot because it allows the entire
 * bot to be shut down through discord. As such, this command has been marked deprecated (not to be used) and it will
 * be rewritten to be more secure in a future update.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class StopCommand implements Command {

    /**
     * Access the invoke key for this Command.
     * @return String - "{@code stop}"
     */
    @Override
    public String getInvoke() {
        return "stop";
    }

    /**
     * The execution for this command is fairly simple because there are no sub commands or anything like that. This command
     * only functions if the sender ID matches the discord ID of the head developer of the discord bot (for security reasons).
     * If this check is validated, the {@link Servers}.save() method is called and then a timer task is declared to exit
     * the java program.
     * @param command
     * The {@link CommandBox} surrounding the JDA Message Event that called this command.
     * @return boolean - True if the command execution performed the desired result, false otherwise.
     */
    @Override
    public boolean execute(CommandBox command) {
        try {
            if (!(command.getEvent().getAuthor().getIdLong() == 185873161009496064L)) {
                return false;
            }
            Servers.save();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 1000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            return false;
        }

    }
}
