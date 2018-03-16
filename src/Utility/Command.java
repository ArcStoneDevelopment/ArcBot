package Utility;

import LoggerFrame.Logger;
import LoggerFrame.LoggerCore;
import LoggerFrame.LoggerException;
import LoggerFrame.LoggerPolicy;

import java.io.Serializable;

public interface Command extends Serializable {
    String getInvoke();
    boolean execute(CommandBox command);

    @Logger(LoggerPolicy.DISCORD)
    default void log(boolean executed, CommandBox command) {
        try {
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), executed, command.getEvent().getGuild(),
                    command.getEvent().getMember().getEffectiveName() + " executed `" + command.getRaw() + "`");
        } catch (LoggerException e) {
            LoggerCore.exceptionLogger(e);
        }
    }

}
