package Levels;

import Utility.LevelUser;
import Utility.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.*;

public class Level {

    private static HashMap<UUID, Long> lastSentMessage = new HashMap<>();

    public static void handle(GuildMessageReceivedEvent event) {
        Server guild = Servers.activeServers.get(event.getGuild().getIdLong());
        int points = new Random().nextInt(20) + 1;
        if (guild.hasLevelUser(event.getAuthor().getIdLong())) {
            LevelUser user = guild.getLevelUser(event.getAuthor().getIdLong());
            if (lastSentMessage.containsKey(user.getUuid())) {
                if (System.currentTimeMillis() - lastSentMessage.get(user.getUuid()) <= 30000) {
                    return;
                }
                lastSentMessage.remove(user.getUuid());
            }
            int previousLevel = user.getLevel();
            user.update(points);
            int newLevel = user.getLevel();

            if (previousLevel < newLevel && guild.textChannelsInit("spam")) {
               // event.getGuild().getTextChannelById(guild.getTextChannelID("spam")).sendMessage().queue();
            }

            guild.setLevelUser(event.getAuthor().getIdLong(), user);
            lastSentMessage.put(user.getUuid(), System.currentTimeMillis());
        } else {
            LevelUser user = new LevelUser(event.getAuthor().getIdLong());
            user.update(points);
            guild.setLevelUser(event.getAuthor().getIdLong(), user);
        }
    }

    /**
     * This method returns the upper bound point count for the given level. If you imagine a level value as having a range
     * of points that fall into that level, this method returns the highest level in that range.
     * <br> For example:
     * <br> * Level 1 : Ranges from 100 to 255.
     * <br> * This method returns 255.
     * @param currentLevel
     * The level whose threshold is desired.
     * @return int - The upper-bound of a given level's range.
     */
    public static int getThreshold(int currentLevel) {
        if (currentLevel == 0) {
            return 100;
        }
        return ((5 * (currentLevel * currentLevel) + 50 * currentLevel + 100) + getThreshold(currentLevel - 1));
    }

    /**
     * Prior to the addition of the {@code ResponseFrame} framework, this method was responsible for building the level
     * data from a given user into a {@code MessageEmbed}. Since the {@code ResponseFrame} has taken over this responsibility,
     * this method is now deprecated for removal.
     * @deprecated
     * @param event
     * The MessageEvent that the query was sent from.
     * @param server
     * The server that the message was on. This should be the server to query for level data.
     * @param userID
     * The discord ID of the user whose level data should be given
     * @return MessageEmbed - Message containing formatted level information.
     */
    public static MessageEmbed getLevel(GuildMessageReceivedEvent event, Server server, long userID) {
        LevelUser user = server.getLevelUser(userID);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(230, 230, 250));
        eb.setTitle("Levels: " + event.getGuild().getMemberById(user.getId()).getEffectiveName());
        eb.addField("__**Points**__", "" + user.getPoints(), true);
        eb.addField("__**Level**__", "" + user.getLevel(), true);
        eb.addField("__**Last Level Up:**__", user.getLevelHistory().get(user.getLevel()), true);
        return eb.build();
    }
}
