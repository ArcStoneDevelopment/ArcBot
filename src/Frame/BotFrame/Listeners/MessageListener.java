package Frame.BotFrame.Listeners;

import Frame.BotFrame.Parser;
import Levels.Level;
import Utility.Command;
import Utility.CommandBox;
import Utility.Server;
import Utility.Servers;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        Server server = Servers.activeServers.get(event.getGuild().getIdLong());
        if (server.drop) {
            return;
        }

        if (server.getFunction("level").isEnabled()) {
            Level.handle(event);
        }

        if (server.getFunction("discord").isEnabled()) {
            if (event.getMessage().getContentRaw().startsWith(server.getSetting("prefix"))) {
                CommandBox command = Parser.parse(server, event);
                if (server.getCommandStatus(command.getInvoke())) {
                    Command cmd = server.getCommand(command.getInvoke().toLowerCase());
                    cmd.log(cmd.execute(command), command);
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

    }
}
