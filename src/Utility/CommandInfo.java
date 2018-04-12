package Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandInfo {

    private String invoke;
    private HashMap<String, SubCommand> commands;

    public CommandInfo(String invoke) {
        this.invoke = invoke;
        commands = new HashMap<>();
    }

    public void addCommand(String key, String usage, String description, Permission permission) {
        commands.put(key.toLowerCase(), new SubCommand(usage, description, permission));
    }

    public Set<String> keys() {
        return commands.keySet();
    }

    public Permission getPermission(String subCommand) {
        return commands.get(subCommand).permission;
    }

    public String getUsage(String subCommand) {
        return commands.get(subCommand).usage;
    }

    public String getDescription(String subCommand) {
        return commands.get(subCommand).description;
    }

    public String getInvoke() {
        return this.invoke;
    }
}

class SubCommand {
    String usage;
    String description;
    Permission permission;
    SubCommand(String usage, String description, Permission permission) {

    }
}
