package Frame.LoggerFrame;

import Utility.Server.Server;
import Utility.Servers;
import Utility.SystemTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

/**
 * The discord logger logs to a given Guild's {@code log} text channel. This logger requires that the {@code guild} sent
 * to the {@link LoggerCore} be not null.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
class DiscordLogger implements Loggable {
    /**
     * This method is the logger method to send messages in the embed format to discord. The format of these messages is
     * slightly different from the other two loggers:
     * <br><br> <strong>Format</strong>
     * <pre>
     *     - The {@code success} boolean determines what color to make the embed: Green if true, Red if false.
     *     - The {@code message} String is set as the "Title" of the embed.
     *     - The Footer of the embed contains the {@link SystemTime} time, and the ArcStone Discord Icon.
     * </pre>
     * @param success
     * If the method fully achieved the expected outcome/made the expected changes this should be true. Otherwise, false.
     * @param guild
     * The Guild object related to the {@link Server} object. This is required if the {@link LoggerPolicy} is
     * {@code Discord}. If the logger policy is not set to this, and/or the action carried out was not connected to a
     * particular server, this should be {@code null} (for standardization).
     * @param message
     * The String message to be logged. This should be short, reasonable and understandable. Furthermore, this should not
     * include a leading space.
     */
    @Override
    public void log(boolean success, Guild guild, String message) throws LoggerException {
        if (guild == null) {
            throw new LoggerException("Guild cannot be null!");
        }
        Server server = Servers.activeServers.get(guild.getIdLong());
        if (!server.getTextChannels().isActive("log")) {
            throw new LoggerException("Log channel is not initialized!");
        }
        TextChannel channel = guild.getTextChannelById(server.getTextChannels().getID("log"));
        EmbedBuilder eb = new EmbedBuilder();
        if (success) {
            eb.setColor(Color.GREEN);
        } else {
            eb.setColor(Color.RED);
        }
        eb.setTitle(message);
        eb.setFooter(SystemTime.getTime(), "https://imgur.com/a/yvWkm");
        channel.sendMessage(eb.build()).queue();
    }
}
