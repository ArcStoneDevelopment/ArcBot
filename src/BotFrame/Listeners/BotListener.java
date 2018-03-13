package BotFrame.Listeners;
import Utility.Model.Server;
import Utility.Servers;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (Servers.activeServers.containsKey(event.getGuild().getIdLong())) {
            return;
        }
        try {
            Servers.activeServers.put(event.getGuild().getIdLong(), new Server(event.getGuild()));
            Servers.saveServer(Servers.activeServers.get(event.getGuild().getIdLong()));
        } catch (Exception e) {
            return;
        }
        System.out.println("New Server Added.");
        PrivateChannel pc = event.getGuild().getOwner().getUser().openPrivateChannel().complete();
        pc.sendMessage("Hi! I'm ArcBot. Thanks for adding me to your server.").queue();
        pc.sendMessage("I've been loaded with all of the default settings. " +
                "Please enter `-arcbot` in your server to get a full list of settings that you can change.").queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
            Servers.activeServers.get(event.getGuild().getIdLong()).drop = true;
    }
}
