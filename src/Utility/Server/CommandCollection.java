package Utility.Server;

import Discord.Discord;
import Utility.Command;
import Utility.FunctionException;

import java.util.HashMap;
import java.util.Set;

public class CommandCollection {
    private HashMap<Command, Boolean> core;

    CommandCollection() {
        core = new HashMap<>() {{
            for (Command c : Discord.commands) {
                put (c, true);
            }
        }};
    }

    public Set<Command> getCommands() {
        return core.keySet();
    }

    public boolean isCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return true;
            }
        }
        return false;
    }

    public Command getCommand(String invoke) throws FunctionException {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return c;
            }
        }
        throw new FunctionException();
    }

    public void enableCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                core.remove(c);
                core.put(c, true);
                return;
            }
        }
    }

    public void disableCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                core.remove(c);
                core.put(c, false);
                return;
            }
        }
    }

    public boolean getCommandStatus(String invoke) throws FunctionException {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return core.get(c);
            }
        }
        throw new FunctionException();
    }
}
