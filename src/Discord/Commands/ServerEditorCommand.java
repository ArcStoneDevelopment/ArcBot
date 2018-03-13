package Discord.Commands;

import Utility.Model.*;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.ArrayList;

public class ServerEditorCommand implements Command {
    @Override
    public String getInvoke() {
        return "arcbot";
    }

    @Override
    public boolean execute(CommandBox command) {
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (command.getArgs().length > 1) {
                switch (command.getArgs()[0].toLowerCase()) {
                    case "command":
                        command(command, server);
                        return true;
                    case "function":
                        function(command, server);
                        return true;
                    case "textchannel":
                    case "permission":
                    default:
                }
            } else {
                arcBot(command, server);
            }
        } catch (PermissionException e) {
            
        } catch (SyntaxException e) {
            switch (e.getIntCause()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
            }
        }
    }

    /**
     * Sub-method to handle the {@code -arcbot command} sub-command.
     * @param command
     * The {@link CommandBox} that called this command. Sent from the {@code execute()} method.
     * @param server
     * The server that the command was issued from. This is also the server that the operation will be performed on.
     * @throws PermissionException
     * Thrown when the user does not have permission to run the command. In this case, the permission required is owner
     * of the guild.
     * @throws SyntaxException
     * The Syntax exception is thrown if there is any problem with the structure of the command. This includes invalid
     * actions. The integer cause values for this exception are:
     * <ol>
     *     <li>The command arguments length is not the required length (3).</li>
     *     <li>The command given (in the second argument) is not a valid command.</li>
     *     <li>The third argument is not {@code enable} or {@code disable}.</li>
     *     <li>The command is already enabled when given a command to enable.</li>
     *     <li>The command is already disabled when given a command to disable.</li>
     * </ol>
     */
    private void command(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (command.getEvent().getGuild().getOwner().getUser().getIdLong() == server.getOwnerID()) {
            if (command.getArgs().length == 3) {
                String invokeKey = command.getArgs()[1];
                if (server.isCommand(invokeKey)) {
                    if (command.getArgs()[2].equalsIgnoreCase("enable")) {
                        if (!(server.getCommandStatus(invokeKey))) {
                            server.enableCommand(invokeKey);
                        } else {
                            throw new SyntaxException(4);
                        }
                    } else if (command.getArgs()[2].equalsIgnoreCase("disable")) {
                        if (server.getCommandStatus(invokeKey)) {
                            server.disableCommand(invokeKey);
                        } else {
                            throw new SyntaxException(5);
                        }
                    } else {
                        throw new SyntaxException(3);
                    }
                } else {
                    throw new SyntaxException(2);
                }
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }
    
    private void function(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (command.getEvent().getAuthor().getIdLong() == server.getOwnerID()) {
            if (command.getArgs().length == 3) {
                if (server.isFunction(command.getArgs()[1].toLowerCase())) {
                    if (command.getArgs()[2].equalsIgnoreCase("enable")) {
                        if (!(server.getFunction(command.getArgs()[1].toLowerCase()).isEnabled())) {
                            server.getFunction(command.getArgs()[1].toLowerCase()).setEnabled(true);
                        } else {
                            throw new SyntaxException(4);
                        }
                    } else if (command.getArgs()[2].equalsIgnoreCase("disable")) {
                        if (server.getFunction(command.getArgs()[1].toLowerCase()).isEnabled()) {
                            server.getFunction(command.getArgs()[1].toLowerCase()).setEnabled(false);
                        } else {
                            throw new SyntaxException(5);
                        }
                    } else {
                        throw new SyntaxException(3);
                    }
                } else {
                    throw new SyntaxException(6);
                }
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void textChannel(CommandBox command, Server server) throws PermissionException, SyntaxException {

    }

    private void arcBot(CommandBox command, Server server) throws PermissionException {
        if (command.getEvent().getAuthor().getIdLong() == server.getOwnerID()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("__**ArcBot Server Settings.**__");
            eb.setDescription("*These are all the settings specific to your server. You may edit the settings with*");
        } else {
            throw new PermissionException();
        }
    }

    @Override
    public void log(boolean executed, CommandBox command) {

    }

    @Override
    public String getHelp(String subCommand) {
        return null;
    }

    @Override
    public ArrayList<Permission> getPermission(String subCommand) {
        return null;
    }

    @Override
    public ArrayList<String> getSubCommands() {
        return null;
    }
}
