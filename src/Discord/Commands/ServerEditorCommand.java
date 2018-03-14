package Discord.Commands;

import Utility.Model.*;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// TODO: JavaDocs for this class and its methods.

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
                        textChannel(command, server);
                        return true;
                    case "permission":
                        permission(command, server);
                    default:
                }
            } else {
                arcBot(command, server);
                return true;
            }
        } catch (PermissionException e) {
//            TODO: Permission error responses.
        } catch (SyntaxException e) {
//            TODO: Proper messaging and error logging needs to be written.
//            TODO: A re-write of the number system to reduce confusion.
            switch (e.getIntCause()) {
                case 1:
                    System.out.println("1");
                    break;
                case 2:
                    System.out.println("2");
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                case 5:
                    System.out.println("5");
                    break;
                case 6:
                    System.out.println("6");
                    break;
                case 7:
                    System.out.println("7");
                    break;
                case 8:
                    System.out.println("8");
                    break;
            }
        }
        return false;
    }

    private void arcBot(CommandBox command, Server server) throws PermissionException {
        if (command.getEvent().getAuthor().getIdLong() == server.getOwnerID()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("__**ArcBot Server Settings.**__");
            eb.setDescription("*" +
                    "The following is a compilation of the settings for your server. As the owner of this discord server, " +
                    "you have the ability to edit any of these settings to fit your needs. We recommend you read the following " +
                    " wiki page to learn how to set up your server: " +
                    "https://github.com/ArcStoneDevelopment/ArcBot/wiki/Setting-Up-Your-ArcBot" +
                    "*");

            StringBuilder sb2 = new StringBuilder();
            for (Function f : server.getFunctions()) {
                if (f.isEnabled()) {
                    sb2.append(":white_check_mark: - ");
                    sb2.append(f.getName());
                } else {
                    sb2.append(":x: - ");
                    sb2.append(f.getName());
                }
                sb2.append("\n");
            }
            eb.addField("Functions", sb2.toString(), true);

            StringBuilder sb1 = new StringBuilder();
            for (Command c : server.getCommands()) {
                if (server.getCommandStatus(c.getInvoke())) {
                    sb1.append(":white_check_mark: - ");
                    sb1.append(c.getInvoke());
                } else {
                    sb1.append(":x: - ");
                    sb1.append(c.getInvoke());
                }
                sb1.append("\n");
            }
            eb.addField("Commands", sb1.toString(), true);

            StringBuilder sb3 = new StringBuilder();
            for (String s : server.getTextChannelNames()) {
                if (!server.textChannelsInit(s)) {
                    sb3.append(s);
                    sb3.append( " - Not Initialized.");
                } else {
                    sb3.append(s);
                    sb3.append( " - ");
                    sb3.append(command.getEvent().getGuild().getTextChannelById(server.getTextChannelID(s)).getAsMention());
                }
                sb3.append("\n");
            }
            eb.addField("Text Channels", sb3.toString(), false);

            StringBuilder sb4 = new StringBuilder();
            for (Permission p : new ArrayList<>(Arrays.asList(Permission.OWNER, Permission.STAFFTEAM, Permission.DONATOR))) {
                boolean hasRole = false;
                sb4.append("__**");
                sb4.append(p.name());
                sb4.append("**__");
                sb4.append("\n");
                for (Role r : command.getEvent().getGuild().getRoles()) {
                    if (p == server.getPermission(r)) {
                        sb4.append(r.getName());
                        hasRole = true;
                        sb4.append("\n");
                    }
                }
                if (!hasRole) {
                    sb4.append("No Roles.");
                    sb4.append("\n");
                }
                sb4.append("\n");
            }

            eb.addField("Permission Levels:", sb4.toString(), false);

            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new PermissionException();
        }
    }

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

    private void permission(CommandBox command, Server server ) throws PermissionException, SyntaxException {
        if (command.getEvent().getAuthor().getIdLong() == server.getOwnerID()) {
            if (command.getArgs().length >= 3) {
                if (command.getEvent().getMessage().getMentionedRoles().size() == 1) {
                    if (server.isPermission(command.getArgs()[1])) {
                        Permission permission = Permission.valueOf(command.getArgs()[1].toUpperCase());
                        server.setPermission(command.getEvent().getMessage().getMentionedRoles().get(0), permission);
                    } else {
                        throw new SyntaxException(8);
                    }
                } else {
                    throw new SyntaxException(7);
                }
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void textChannel(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (command.getEvent().getAuthor().getIdLong() == server.getOwnerID()) {
            if (command.getArgs().length == 2) {
                if (server.getTextChannelNames().contains(command.getArgs()[1].toLowerCase())) {
                    server.initTextChannel(command.getArgs()[1].toLowerCase(), command.getEvent().getChannel().getIdLong());
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

    /**
     * This method is left intentionally empty. This command is entirely excluded from the help mechanism because it is
     * only available to the owner of the discord server. Calling these methods will return default values for the given
     * return data types.
     * @param subCommand
     * The subCommand whose help values are to be returned.
     * @return String - Since this method is left blank, the return here will be an empty string.
     */
    @Override
    public String getHelp(String subCommand) {
        return "";
    }

    /**
     * This method is left intentionally empty. This command is entirely excluded from the help mechanism because it is
     * only available to the owner of the discord server. Calling these methods will return default values for the given
     * return data types.
     * @param subCommand
     * The subCommand whose help values are to be returned.
     * @return {@code ArrayList<Permission>} - Since this method is left blank, the return here will be a singleton list
     * with {@code Permission.DEFAULT}.
     */
    @Override
    public ArrayList<Permission> getPermission(String subCommand) {
        return new ArrayList<>(Collections.singletonList(Permission.DEFAULT));
    }

    /**
     * This method is left intentionally empty. This command is entirely excluded from the help mechanism because
     * it is only available to the owner of the discord server. Calling these methods will return default values for the
     * given return data types.
     * @return {@code ArrayList<String>} - Since this method is left blank, the return here will be a singleton list with
     * an empty string.
     */
    @Override
    public ArrayList<String> getSubCommands() {
        return new ArrayList<>(Collections.singletonList(""));
    }
}
