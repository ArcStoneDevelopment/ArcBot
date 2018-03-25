package Frame.BotFrame.Listeners;
import Utility.Permission;
import Utility.Server;
import Utility.Servers;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * This listener provides the bot with the ability to keep {@code Server} objects updated as the status of various
 * pieces of guilds change.
 *
 * @author ArcStone Development LLC
 * @since v1.0
 * @version v1.0
 */
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

    @Override
    public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
        Server server = Servers.activeServers.get(event.getGuild().getIdLong());
        server.setOwnerID(event.getGuild().getOwner().getUser().getIdLong());
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        Server server = Servers.activeServers.get(event.getGuild().getIdLong());
        if (server.getTextChannels().contains(event.getChannel().getIdLong())) {
            String textChannel = server.getTextChannelName(event.getChannel().getIdLong());
            PrivateChannel pc = event.getGuild().getOwner().getUser().openPrivateChannel().complete();
            pc.sendMessage("The registered " + textChannel + " channel has been deleted! I've unlinked it. Please re-register the channel ASAP.").queue();
            pc.close().complete();
            server.clearTextChannel(textChannel);
        }
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
        Server server = Servers.activeServers.get(event.getGuild().getIdLong());
        if (server.getPermission(event.getRole()) != Permission.DEFAULT) {
            String role = event.getRole().getName();
            PrivateChannel pc = event.getGuild().getOwner().getUser().openPrivateChannel().complete();
            pc.sendMessage("You have deleted a role (" + role + ") that was not default!").queue();
            pc.close().complete();
            server.setPermission(event.getRole(), Permission.DEFAULT);
        }
    }
}
