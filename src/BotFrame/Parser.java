package BotFrame;

import Utility.Model.CommandBox;
import Utility.Model.Server;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The command parser for the bot. This parser breaks {@code MessageReceivedEvent}s from JDA into {@link CommandBox} objects
 * that can be used in the {@link Discord.Commands} classes.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class Parser {
    /**
     * The actual parsing method to break down {@code MessageReceivedEvent} objects into {@link CommandBox} objects.
     * @param server
     * The {@link Server} object that corresponds to the {@code Guild} in the {@code event} parameter.
     * @param event
     * The {@code MessageReceivedEvent} to be parsed.
     * @return CommandBox - The final parsed container.
     */
    public static CommandBox parse(Server server, GuildMessageReceivedEvent event) {

        String raw = event.getMessage().getContentRaw();
        String beheaded = raw.substring(server.getSetting("prefix").length(), raw.length());
        String[] splitBeheaded = beheaded.split(" ");
        ArrayList<String> split = new ArrayList<>(Arrays.asList(splitBeheaded));
        String invoke = split.get(0);
        String[] args = new String[split.size()-1];
        split.subList(1, split.size()).toArray(args);

        return new CommandBox(raw, beheaded, splitBeheaded, invoke, args, event);
    }
}
