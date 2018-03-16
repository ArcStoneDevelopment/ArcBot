package LoggerFrame;

import Utility.Server;
import Utility.Servers;
import Utility.SystemTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;

import java.awt.*;

class DiscordLogger implements Loggable {
    @Override
    public void log(boolean success, Guild guild, String message) {
        Server server = Servers.activeServers.get(guild.getIdLong());
        TextChannel channel = guild.getTextChannelById(server.getTextChannelID("log"));
        EmbedBuilder eb = new EmbedBuilder();
        if (success) {
            eb.setColor(Color.BLUE);
        } else {
            eb.setColor(Color.RED);
        }
        eb.setTitle(SystemTime.getTime());
        eb.setDescription(message);
        eb.setFooter(SystemTime.getTime(), )

    }
}
