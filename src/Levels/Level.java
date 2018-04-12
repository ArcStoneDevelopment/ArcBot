package Levels;

import Utility.Server.Server;
import Utility.ServerException;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.*;

/**
 * Controller class for the {@code Level} function.
 * <br> To understand the logic behind the system, it is important to understand the actual layout of the levels system.
 * <p>
 * The level system adds anywhere from 1 - 20 points per message given that a user has not sent a message to the same
 * server in the last 30 seconds (to prevent spam). This level data is then stored as {@link LevelUser} objects and saved
 * to the corresponding {@link Server}.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.0
 */
public class Level {

    /**
     * HashMap to enforce the 30 second rule for a user. The UUID matches the {@link LevelUser} object (to allow for
     * multi-server support). The Long is the System time in exact milliseconds.
     */
    private static HashMap<UUID, Long> lastSentMessage = new HashMap<>();

    /**
     * This constructor is declared and set to private to prevent instantiation of {@code Level} objects. This class
     * should only be used as a static utility class.
     */
    private Level() {}

    /**
     * The actual method to perform the logic for adding points to a user.
     * @param event
     * The Message event from JDA to be handled.
     */
    public static void handle(GuildMessageReceivedEvent event) {
        try {
            Server guild = Servers.activeServers.get(event.getGuild().getIdLong());
            int points = new Random().nextInt(20) + 1;
            if (guild.getLevels().hasLevelUser(event.getAuthor().getIdLong())) {
                LevelUser user = guild.getLevels().getLevelUser(event.getAuthor().getIdLong());
                if (user.getDisabled()) {
                    return;
                }
                if (lastSentMessage.containsKey(user.getUuid())) {
                    if (System.currentTimeMillis() - lastSentMessage.get(user.getUuid()) <= 30000) {
                        return;
                    }
                    lastSentMessage.remove(user.getUuid());
                }
                int previousLevel = user.getLevel();
                user.update(points);
                int newLevel = user.getLevel();

                // TODO: This.
            /*
            if (previousLevel < newLevel && guild.textChannelsInit("spam")) {
               event.getGuild().getTextChannelById(guild.getTextChannelID("spam")).sendMessage().queue();
            }
            */

                guild.getLevels().setLevelUser(event.getAuthor().getIdLong(), user);
                lastSentMessage.put(user.getUuid(), System.currentTimeMillis());
            } else {
                LevelUser user = new LevelUser(event.getAuthor().getIdLong());
                user.update(points);
                guild.getLevels().setLevelUser(event.getAuthor().getIdLong(), user);
            }
        } catch (Exception e) {}
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
     * @deprecated
     * Prior to the addition of the {@code ResponseFrame} framework, this method was responsible for building the level
     * data from a given user into a {@code MessageEmbed}. Since the {@code ResponseFrame} has taken over this responsibility,
     * this method is now deprecated for removal.
     * @param event
     * The MessageEvent that the query was sent from.
     * @param server
     * The server that the message was on. This should be the server to query for level data.
     * @param userID
     * The discord ID of the user whose level data should be given
     * @return MessageEmbed - Message containing formatted level information.
     */
    @Deprecated
    public static MessageEmbed getLevel(GuildMessageReceivedEvent event, Server server, long userID) {
        try {
            LevelUser user = server.getLevels().getLevelUser(userID);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(230, 230, 250));
            eb.setTitle("Levels: " + event.getGuild().getMemberById(user.getId()).getEffectiveName());
            eb.addField("__**Points**__", "" + user.getPoints(), true);
            eb.addField("__**Level**__", "" + user.getLevel(), true);
            eb.addField("__**Last Level Up:**__", user.getLevelHistory().get(user.getLevel()), true);
            return eb.build();
        } catch (Exception e) {
            return new EmbedBuilder(){}.addBlankField(false).build();
        }
    }
}
