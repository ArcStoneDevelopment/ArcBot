package Utility.Model;

import java.io.Serializable;
import java.util.ArrayList;

public interface Command extends Serializable {
    String getInvoke();
    boolean execute(CommandBox command);
    void log(boolean executed, CommandBox command);
    String getHelp(String subCommand);
    ArrayList<Permission> getPermission(String subCommand);
    ArrayList<String> getSubCommands();
}
