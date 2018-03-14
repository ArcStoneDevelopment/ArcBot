package Utility.Model;

import Utility.Logger;

import java.io.Serializable;
import java.util.ArrayList;

public interface Command extends Serializable {
    String getInvoke();
    boolean execute(CommandBox command);
    default void log(boolean executed, CommandBox command, Server server) {
        if (executed)
        {
            Logger.infoLog(server, "{+} **" + command.getRaw() + "** executed by " + command.getEvent().getMember().getEffectiveName());
        } else
        {
            Logger.errorLog(server, "{-} **" + command.getRaw() + "** executed by " + command.getEvent().getMember().getEffectiveName());
        }
    }
    String getHelp(String subCommand);
    ArrayList<Permission> getPermission(String subCommand);
    ArrayList<String> getSubCommands();
}
