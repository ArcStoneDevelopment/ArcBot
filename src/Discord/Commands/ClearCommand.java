package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Utility.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.*;

/**
 * Provides the {@code -clear} command and all various sub-commands.
 * <br> This command has two sub-commands. The first is {@code -clear [number of messages]} which clears the given number
 * of messages from the current channel. The second is {@code -clear all} which clears all of the messages (up to the time
 * limit in JDA) from the current channel.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public class ClearCommand implements Command {

    /**
     * Access the invoke key for this Command.
     * @return String - "{@code clear}"
     */
    @Override
    public String getInvoke() {
        return "clear";
    }

    /**
     * The main point of execution for this command. This method finds the {@link Server} object for the guild through
     * {@link Servers}, then it parses the arguments to determine the sub-command to be ran.
     * @param command
     * The parsed {@link CommandBox} for the event which triggered this command.
     * @return boolean - If the desired outcome occurred, this will be true. If the desired outcome did not occur, for any
     * reason, this will be false.
     */
    @Override
    public boolean execute(CommandBox command) {
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (command.getArgs().length == 1) {
                switch (command.getArgs()[0]) {
                    case "all":
                        clearAll(command, server);
                        return true;
                    default:
                        clearNum(command, server);
                        return true;
                }
            } else {
                throw new SyntaxException(0);
            }
        } catch (PermissionException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(1))).queue();
        } catch (SyntaxException e) {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                    new Frame.ResponseFrame.ErrorResponse(3, new ArrayList<>(Collections.singleton("*Please enter a valid number of messages.*"))))).queue();
        }
        return false;
    }

    /**
     * This sub-method is specifically for the {@code -clear all} command. In order for this command to run, the user
     * must be in the STAFFTEAM {@link Permission} level.
     * @param command
     * The parsed {@link CommandBox} for the event which triggered this command.
     * @param server
     * The {@link Server} object for this event.
     * @throws PermissionException
     * When a user is not in the STAFFTEAM permission level, this exception is thrown.
     */
    private void clearAll(CommandBox command, Server server) throws PermissionException {
        if (server.hasPermission(command.getEvent().getMember(), Permission.STAFFTEAM)) {
            MessageHistory history = new MessageHistory(command.getEvent().getChannel());
            List<Message> messages;
            while (true) {
                try {
                    messages = history.retrievePast(1).complete();
                    messages.get(0).delete().queue();
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            Message response = command.getEvent().getChannel().sendMessage(
                    Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.SuccessResponse(0))).complete();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    response.delete().complete();
                }
            }, 3000);
        } else {
            throw new PermissionException();
        }
    }

    /**
     * Thus sub-method is specifically for the {@code -clear [number of messages]} command. In order for this command to run,
     * the user must be in the STAFFTEAM {@link Permission} level, and the first argument in the {@link CommandBox}'s
     * arguments array should be a valid integer.
     * @param command
     * This parsed {@link CommandBox} for the event which triggered this command.
     * @param server
     * The {@link Server} object for this event.
     * @throws PermissionException
     * When a user is not in the STAFFTEAM permission level, this exception is thrown.
     * @throws SyntaxException
     * When the first argument in the {@link CommandBox}'s arguments array is not able to be parsed as a valid integer,
     * this exception is thrown.
     */
    private void clearNum(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (server.hasPermission(command.getEvent().getMember(), Permission.STAFFTEAM)) {
            try {
                int numMessage = Integer.parseInt(command.getArgs()[0]);
                MessageHistory history = new MessageHistory(command.getEvent().getChannel());
                List<Message> messages = history.retrievePast(numMessage).complete();
                for (Message m : messages) {
                    m.delete().queue();
                }
            } catch (NumberFormatException e) {
                throw new SyntaxException(0);
            }
        } else {
            throw new PermissionException();
        }
    }
}
