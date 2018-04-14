package Frame.BotFrame.Listeners;

import Frame.BotFrame.Parser;
import Frame.FunctionFrame.Handler;
import Levels.Level;
import Report.ReportHandler;
import Utility.Command;
import Frame.BotFrame.CommandBox;
import Utility.Server.Server;
import Utility.Servers;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            if (event.getAuthor().isBot()) {
                return;
            }

            Server server = Servers.activeServers.get(event.getGuild().getIdLong());
            if (server.drop) {
                return;
            }

            if (server.getFunctions().getFunction("level").isEnabled()) {
                Level.handle(event);
            }

            if (server.getFunctions().getFunction("discord").isEnabled()) {
                if (event.getMessage().getContentRaw().startsWith(server.getSettings().getSetting("prefix"))) {
                    CommandBox command = Parser.parse(server, event);
                    if (server.getCommands().getCommandStatus(command.getInvoke())) {
                        Command cmd = server.getCommands().getCommand(command.getInvoke().toLowerCase());
                        cmd.log(cmd.execute(command), command);
                    } else {
                        event.getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(5))).queue();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (Handler.openFunctions.containsKey(event.getAuthor().getIdLong())) {
            switch (Handler.openFunctions.get(event.getAuthor().getIdLong()).getSOURCE()) {
                case REPORT:
                    ReportHandler.handle(event);
                    break;
            }
        }
    }
}
