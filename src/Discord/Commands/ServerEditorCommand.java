package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Frame.FunctionFrame.Function;
import Utility.*;
import Utility.Server.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerEditorCommand implements Command {

    private CommandInfo info;

    public ServerEditorCommand() {
        info = new CommandInfo("arcbot");
        info.addCommand("[]", "-arcbot", "See the current settings for your server.", Permission.SERVER_OWNER);
        info.addCommand("command", "-arbot command [invoke] [enable/disable]", "Enable or disable a command with the given invoke key.", Permission.SERVER_OWNER);
        info.addCommand("function", "-arcbot function [function name] [enable/disable]", "Enable or disable a function (and its accompanying commands) with the given name.", Permission.SERVER_OWNER);
        info.addCommand("permission", "-arbot permission [permission name] [@role]", "Assign a bot permission level to a particular server role.", Permission.SERVER_OWNER);
        info.addCommand("textchannel", "-arcbot textchannel [name]", "Designate the current text channel as the channel associated with the given name for bot use.", Permission.SERVER_OWNER);
    }

    @Override
    public CommandInfo getInfo() {
        return info;
    }

    @Override
    public String getInvoke() {
        return info.getInvoke();
    }

    @Override
    public boolean execute(CommandBox command) {
        try {
            if (command.getArgs().length > 1) {
                switch (command.getArgs()[0].toLowerCase()) {
                    case "command":
                        command(command);
                        return true;
                    case "function":
                        function(command);
                        return true;
                    case "textchannel":
                        textChannel(command);
                        return true;
                    case "permission":
                        permission(command);
                        return true;
                    default:
                }
            } else {
                arcBot(command);
                return true;
            }
        } catch (PermissionException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(1))).queue();
        } catch (SyntaxException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(e.getIntCause()))).queue();
        } catch (ServerException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(-1))).queue();
            return false;
        }
        return false;
    }

    private void arcBot(CommandBox command) throws PermissionException, ServerException {
        if (command.getEvent().getAuthor().getIdLong() == command.getServer().getOwnerID()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(255,0,255));
            eb.setTitle("__**ArcBot Server Settings.**__");
            eb.setDescription("*" +
                    "The following is a compilation of the settings for your server. As the owner of this discord server, " +
                    "you have the ability to edit any of these settings to fit your needs. We recommend you read the following " +
                    " wiki page to learn how to set up your server: " +
                    "https://github.com/ArcStoneDevelopment/ArcBot/wiki/Setting-Up-Your-ArcBot" +
                    "*");

            StringBuilder sb2 = new StringBuilder();
            for (Function f : command.getServer().getFunctions().getFunctions()) {
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
            for (Command c : command.getServer().getCommands().getCommands()) {
                if (c.getInvoke().equalsIgnoreCase("stop")) {
                    continue;
                }
                if (command.getServer().getCommands().getCommandStatus(c.getInvoke())) {
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
            for (String s : command.getServer().getTextChannels().getNames()) {
                if (!command.getServer().getTextChannels().isActive(s)) {
                    sb3.append(s);
                    sb3.append( " - Not Initialized.");
                } else {
                    sb3.append(s);
                    sb3.append( " - ");
                    sb3.append(command.getEvent().getGuild().getTextChannelById(command.getServer().getTextChannels().getID(s)).getAsMention());
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
                    if (p == command.getServer().getPermissions().getPermission(r)) {
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

    private void command(CommandBox command) throws PermissionException, SyntaxException, ServerException {
        if (command.getEvent().getGuild().getOwner().getUser().getIdLong() == command.getServer().getOwnerID()) {
            if (command.getArgs().length == 3) {
                String invokeKey = command.getArgs()[1];
                if (command.getServer().getCommands().isCommand(invokeKey)) {
                    if (command.getArgs()[2].equalsIgnoreCase("enable")) {
                        if (!(command.getServer().getCommands().getCommandStatus(invokeKey))) {
                            switch (invokeKey) {
                                case "level":
                                    if (!command.getServer().getFunctions().getFunction("level").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "leveledit":
                                    if (!command.getServer().getFunctions().getFunction("level").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "appeal":
                                    if (!command.getServer().getFunctions().getFunction("appeal").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "report":
                                    if (!command.getServer().getFunctions().getFunction("report").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "apply":
                                    if (!command.getServer().getFunctions().getFunction("apply").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                            }
                            command.getServer().getCommands().enableCommand(invokeKey);
                            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(11))).queue();
                        } else {
                            throw new SyntaxException(3);
                        }
                    } else if (command.getArgs()[2].equalsIgnoreCase("disable")) {
                        if (command.getServer().getCommands().getCommandStatus(invokeKey)) {
                            switch (invokeKey) {
                                case "level":
                                    if (command.getServer().getFunctions().getFunction("level").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "leveledit":
                                    if (command.getServer().getFunctions().getFunction("level").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "appeal":
                                    if (command.getServer().getFunctions().getFunction("appeal").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "report":
                                    if (command.getServer().getFunctions().getFunction("report").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                                case "apply":
                                    if (command.getServer().getFunctions().getFunction("apply").isEnabled()) {
                                        throw new SyntaxException(4);
                                    }
                                    break;
                            }
                            command.getServer().getCommands().disableCommand(invokeKey);
                            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(11))).queue();
                        } else {
                            throw new SyntaxException(2);
                        }
                    } else {
                        throw new SyntaxException(0);
                    }
                } else {
                    throw new SyntaxException(1);
                }
            } else {
                throw new SyntaxException(0);
            }
        } else {
            throw new PermissionException();
        }
    }
    
    private void function(CommandBox command) throws PermissionException, SyntaxException, ServerException {
        if (command.getEvent().getAuthor().getIdLong() == command.getServer().getOwnerID()) {
            if (command.getArgs().length == 3) {
                if (command.getServer().getFunctions().isFunction(command.getArgs()[1].toLowerCase())) {
                    if (command.getArgs()[2].equalsIgnoreCase("enable")) {
                        if (!(command.getServer().getFunctions().getFunction(command.getArgs()[1].toLowerCase()).isEnabled())) {
                            command.getServer().getFunctions().getFunction(command.getArgs()[1].toLowerCase()).setEnabled(true);
                            switch (command.getArgs()[1].toLowerCase()) {
                                case "level":
                                    command.getServer().getCommands().enableCommand("level");
                                    break;
                                case "leveledit":
                                    command.getServer().getCommands().enableCommand("leveledit");
                                    break;
                                case "appeal":
                                case "apply":
                                case "report":
                                    command.getServer().getCommands().enableCommand("report");
                                    break;
                            }
                            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(12))).queue();
                        } else {
                            throw new SyntaxException(7);
                        }
                    } else if (command.getArgs()[2].equalsIgnoreCase("disable")) {
                        if (command.getServer().getFunctions().getFunction(command.getArgs()[1].toLowerCase()).isEnabled()) {
                            command.getServer().getFunctions().getFunction(command.getArgs()[1].toLowerCase()).setEnabled(false);
                            switch (command.getArgs()[1].toLowerCase()) {
                                case "level":
                                    command.getServer().getCommands().disableCommand("level");
                                    break;
                                case "leveledit":
                                    command.getServer().getCommands().enableCommand("leveledit");
                                    break;
                                case "appeal":
                                case "apply":
                                case "report":
                                    command.getServer().getCommands().disableCommand("report");
                                    break;
                            }
                            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(12))).queue();
                        } else {
                            throw new SyntaxException(6);
                        }
                    } else {
                        throw new SyntaxException(0);
                    }
                } else {
                    throw new SyntaxException(5);
                }
            } else {
                throw new SyntaxException(0);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void permission(CommandBox command) throws PermissionException, SyntaxException {
        if (command.getEvent().getAuthor().getIdLong() == command.getServer().getOwnerID()) {
            if (command.getArgs().length >= 3) {
                if (command.getEvent().getMessage().getMentionedRoles().size() == 1) {
                    if (command.getServer().getPermissions().isPermission(command.getArgs()[1])) {
                        Permission permission = Permission.valueOf(command.getArgs()[1].toUpperCase());
                        command.getServer().getPermissions().setPermission(command.getEvent().getMessage().getMentionedRoles().get(0), permission);
                        command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(14))).queue();
                    } else {
                        throw new SyntaxException(9);
                    }
                } else {
                    throw new SyntaxException(8);
                }
            } else {
                throw new SyntaxException(0);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void textChannel(CommandBox command) throws PermissionException, SyntaxException, ServerException {
        if (command.getEvent().getAuthor().getIdLong() == command.getServer().getOwnerID()) {
            if (command.getArgs().length == 2) {
                if (command.getServer().getTextChannels().getNames().contains(command.getArgs()[1].toLowerCase())) {
                    command.getServer().getTextChannels().initialize(command.getArgs()[1].toLowerCase(), command.getEvent().getChannel().getIdLong());
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.MasterResponse(13))).queue();
                } else {
                    throw new SyntaxException(10);
                }
            } else {
                throw new SyntaxException(0);
            }
        } else {
            throw new PermissionException();
        }
    }
}
