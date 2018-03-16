package Utility;

import java.io.Serializable;

public interface Command extends Serializable {
    String getInvoke();
    boolean execute(CommandBox command);
    default void log(boolean executed, CommandBox command, Server server) {}

}
