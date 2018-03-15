package Discord.Commands;

import Utility.Command;
import Utility.CommandBox;
import Utility.Servers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @deprecated - For removal before production release.
 */
// TODO: Remove this command before production release. The stop command is not to be used when released.
public class StopCommand implements Command {
    @Override
    public String getInvoke() {
        return "stop";
    }

    @Override
    public boolean execute(CommandBox command) {
        try {
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
