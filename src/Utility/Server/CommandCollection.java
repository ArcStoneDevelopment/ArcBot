package Utility.Server;

import Discord.Discord;
import Utility.Command;
import Utility.ServerException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * A wrapper for the {@link Command} HashMap that stores Server-Specific data about each {@link Command}.
 *
 * @author ArcStone Development LLC
 * @version v2.0
 * @since v2.0
 */
public class CommandCollection implements Serializable {

    /**
     * The primary HashMap of data that is encapsulated in this object.
     * <br> The boolean in this map is the active-state of the command. This determines whether or not execution should
     * occur on a given command when called.
     */
    private HashMap<Command, Boolean> core;

    /**
     * Package-Private constructor which populates the {@code core} HashMap with the {@link Command} objects from
     * {@link Discord}. Each commands starts with an active-state of true.
     */
    CommandCollection() {
        core = new HashMap<>() {{
            for (Command c : Discord.commands) {
                put (c, true);
            }
        }};
    }

    /**
     * Access an immutable {@code Set} containing all of the command objects.
     * @return {@code Set<Command>}
     */
    public Set<Command> getCommands() {
        return core.keySet();
    }

    /**
     * Determine if a given String is an invoke key for a registered command.
     * @param invoke
     * The string to test against the {@code core} HashMap.
     * @return boolean - True if the String is a valid invoke key, false otherwise.
     */
    public boolean isCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Access a {@link Command} object by its invoke key.
     * @param invoke
     * The invoke key of the desired command.
     * @return {@link Command} - The object which matches the given invoke key.
     * @throws ServerException
     * This exception is thrown when the {@code invoke} parameter is not a valid {@link Command} invoke key.
     */
    public Command getCommand(String invoke) throws ServerException {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return c;
            }
        }
        throw new ServerException();
    }

    /**
     * Change the active-state of a given command to true (Enable a command). This method is very graceful about two things:
     * (1) If the invoke key is not a valid {@link Command} invoke key, the method will exit without doing anything. (2) If the
     * command is already active, this method will also exit without doing anything.
     * @param invoke
     * The invoke key of the command whose status should be changed.
     */
    public void enableCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                core.remove(c);
                core.put(c, true);
                return;
            }
        }
    }

    /**
     * Change the active-state of a given command to false (Disable a command). This method is very graceful about two things:
     * (1) If the invoke key is not a valid {@link Command} invoke key, the method will exit without doing anything. (2) If the
     * command is already disabled, this method will also exit without doing anything.
     * @param invoke
     * The invoke key of the command whose status should be changed.
     */
    public void disableCommand(String invoke) {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                core.remove(c);
                core.put(c, false);
                return;
            }
        }
    }

    /**
     * Get the active-state of a given command by its invoke key.
     * @param invoke
     * The invoke key of the {@link Command} whose status should be returned.
     * @return boolean - True if the command is enabled, false if it is disabled.
     * @throws ServerException
     * This exception is thrown when the given invoke key is not a valid {@link Command} invoke key.
     */
    public boolean getCommandStatus(String invoke) throws ServerException {
        for (Command c : core.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return core.get(c);
            }
        }
        throw new ServerException();
    }
}
