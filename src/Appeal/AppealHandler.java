package Appeal;

import Utility.Servers;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class AppealHandler {

    public static void start(GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();
        Appeal appeal = new Appeal(event.getGuild().getIdLong(), event.getAuthor().getIdLong());
        pc.sendMessage(getResponse(appeal.getBuildProgress(), appeal)).queue();
        appeal.build();
        Servers.activeServers.get(event.getGuild().getIdLong())
    }

    private static MessageEmbed getResponse(int responseNumber, Appeal appeal) {

    }
}
