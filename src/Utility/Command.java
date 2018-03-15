package Utility;

import java.io.Serializable;

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

}
