package Discord.Commands;

import Utility.Command;
import Utility.CommandBox;
import Utility.Servers;

import java.util.Timer;
import java.util.TimerTask;

public class StopCommand implements Command {
    @Override
    public String getInvoke() {
        return "stop";
    }

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
