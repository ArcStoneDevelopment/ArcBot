package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Levels.LevelUser;
import Utility.*;
import Utility.Server.Server;

public class LevelEditCommand implements Command {

    private CommandInfo info;


    public LevelEditCommand() {
        info = new CommandInfo("leveledit");
        info.addCommand("block", "-leveledit block [@user]", "Ban a user from the level function.", Permission.OWNER);
        info.addCommand("reset", "-leveledit reset [@user]", "Reset a user's level data to 0.", Permission.OWNER);
        info.addCommand("unblock", "-leveledit unblock [@user]", "Lift a ban on a user from the level function.", Permission.OWNER);
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
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (command.getArgs().length >= 1 && command.getEvent().getMessage().getMentionedUsers().size() == 1) {
                switch (command.getArgs()[0]) {
                    case "block":
                        block(command, server);
                        return true;
                    case "unblock":
                        unBlock(command, server);
                        return true;
                    case "reset":
                        resetUser(command, server);
                        return true;
                    default:
                        throw new SyntaxException(0);
                }
            } else {
                throw new SyntaxException(0);
            }
        } catch (SyntaxException e) {
            switch (e.getIntCause()) {
                case 0:
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                            new Frame.ResponseFrame.ErrorResponse(2))).queue();
                    break;
                case 1:
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                            new Frame.ResponseFrame.LevelResponse(2))).queue();
                    break;
                case 2:
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                            new Frame.ResponseFrame.LevelResponse(3))).queue();
                    break;
                case 3:
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                            new Frame.ResponseFrame.LevelResponse(4))).queue();
                    break;
            }
        } catch (PermissionException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                    new Frame.ResponseFrame.ErrorResponse(1))).queue();
        } catch (ServerException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                    new Frame.ResponseFrame.LevelResponse(2))).queue();
        }
        return false;
    }

    private void block(CommandBox command, Server server) throws PermissionException, SyntaxException, ServerException {
        if (server.getPermissions().hasPermission(command.getEvent().getMember(), Permission.OWNER)) {
            if (server.getLevels().hasLevelUser(command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong())) {
                LevelUser user = server.getLevels().getLevelUser(
                        command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong());
                if (user.getDisabled()) {
                    throw new SyntaxException(2);
                }
                user.setDisabled(true);
                //MESSAGE HERE
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void unBlock (CommandBox command, Server server) throws PermissionException, SyntaxException, ServerException {
        if (server.getPermissions().hasPermission(command.getEvent().getMember(), Permission.OWNER)) {
            if (server.getLevels().hasLevelUser(command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong())) {
                LevelUser user = server.getLevels().getLevelUser(
                        command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong());
                if (!(user.getDisabled())) {
                    throw new SyntaxException(3);
                }
                user.setDisabled(false);
                //MESSAGE HERE
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }

    private void resetUser(CommandBox command, Server server) throws PermissionException, SyntaxException, ServerException {
        if (server.getPermissions().hasPermission(command.getEvent().getMember(), Permission.OWNER)) {
            if (server.getLevels().hasLevelUser(command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong())) {
                LevelUser user = server.getLevels().getLevelUser(
                        command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong());
                user.reset();
                //MESSAGE HERE
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new PermissionException();
        }
    }
}
