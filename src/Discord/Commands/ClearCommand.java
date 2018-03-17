package Discord.Commands;

import ResponseFrame.ErrorResponse;
import ResponseFrame.ResponseBuilder;
import Utility.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClearCommand implements Command {

    @Override
    public String getInvoke() {
        return "clear";
    }

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
            command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new ErrorResponse(1))).queue();
        } catch (SyntaxException e) {

        }
        return false;
    }

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
            /*
            Message response = command.getEvent().getChannel().sendMessage().complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    response.delete().complete();
                }
            }, 3000);
            */

        } else {
            throw new PermissionException();
        }
    }

    private void clearNum(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (server.hasPermission(command.getEvent().getMember(), Permission.STAFFTEAM)) {

        } else {
            throw new PermissionException();
        }
    }
}
