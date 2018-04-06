package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Levels.LevelUser;
import Utility.*;
import Utility.Server.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.Color;
import java.util.*;

/**
 * Provides the {@code -level} command and all various sub-commands.
 * <br><br> <strong><u>Three Sub-Commands:</u></strong>
 * <br> 1. {@code -level}: View level data about yourself. The data of the sending user is used.
 * <br> 2. {@code -level [@user]}: View level data about another user. The data of the mentioned user is used.
 * <br> 3. {@code -level leaderboard}: View the top ten users on the server (by point values).
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.0
 */
public class LevelCommand implements Command {

    /**
     * This constructor is used to create objects that allow access to the desired command methods.
     */
    public LevelCommand() {}

    /**
     * Access the invoke key for this Command.
     * @return String - "{@code level}"
     */
    @Override
    public String getInvoke() {
        return "level";
    }

    /**
     * The main point of execution for this command. This method finds the server object from {@link Servers}. It then
     * tests to ensure that the level function is enabled. Even though the status of this command and the function are linked,
     * it's important to still have this check to ensure that any extraneous cases are taken care of. It then parses the
     * command to determine which sub-command should be ran.
     * <br><br> <strong><u>Syntax Error Mapping:</u></strong>
     * <br> <em>For this method, the following syntax error numbers are allowed:</em>
     * <br> • <Strong>{@code 0}</Strong> - Improper Command Syntax Error
     * <br> • <Strong>{@code 1}</Strong> - Level User Not Found Error
     * @param command
     * The {@link CommandBox} surrounding the JDA Message Event that called this command.
     * @return boolean - True if the method completed with the desired result, false otherwise.
     */
    @Override
    public boolean execute(CommandBox command) {
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (!(server.getFunctions().getFunction("level").isEnabled())) {
                MessageEmbed msg = Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(4));
                command.getEvent().getChannel().sendMessage(msg).complete();
                return false;
            }

            if (command.getArgs().length == 0) {
                levelUser(command, server);
                return true;
            } else if (command.getArgs().length > 0) {
                switch (command.getArgs()[0].toLowerCase()) {
                    case "leaderboard":
                        levelLeaderboard(command, server);
                        return true;
                    default:
                        levelUser(command, server);
                        return true;
                }
            }
        } catch (SyntaxException e) {
            switch (e.getIntCause()) {
                case 0: //Syntax Error
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(2))).queue();
                    break;
                case 1: // Level User Error
                    command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.LevelResponse(0))).queue();
                    break;
            }
        } catch (FunctionException e) {
            return false;
        }
        return false;
    }

    /**
     * Provides code for {@code -level [@user]} and {@code -level} commands. The first thing that this command does is that it
     * determines which command was used - the one with a mention, or the one without a mention. Once this has been determined,
     * the proper {@code Member} and {@link LevelUser} objects are pulled from the {@code server} object. The embed message
     * is then built based off of this data and sent to the text channel in which the original command was sent.
     * <br><br> <strong><u>Embed Format:</u></strong>
     * <em>The following data is displayed in the embed:</em>
     * <ol>
     *     <li>The user's server-wide nickname and avatar image.</li>
     *     <li>The user's raw point value.</li>
     *     <li>The user's raw level value.</li>
     *     <li>The level/time combination from the {@link LevelUser} tree map symbolizing the most recent level-up.</li>
     * </ol>
     * @param command
     * The {@link CommandBox} that called the {@code execute()} method.
     * @param server
     * The server that was pulled from {@link Servers} which matches the guild from which the JDA event occured.
     * @throws SyntaxException
     * Exception is thrown if there are any issues during run-time. The integer system is used in this method.
     * <br> • <strong>{@code 0}</strong>: This number exception is thrown when the command does not have either no arguments or
     * more than one arguments and a mentioned user.
     * <br> • <strong>{@code 1}</strong>: This number exception is thrown when the server object given does not have the {@link LevelUser}
     * that is required.
     */
    private void levelUser(CommandBox command, Server server) throws SyntaxException {
        Member guildUser;
        LevelUser user;
        long userID;

        if (command.getArgs().length == 0) {
            userID = command.getEvent().getAuthor().getIdLong();
        } else if (command.getEvent().getMessage().getMentionedUsers().size() == 1) {
            userID = command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong();
        } else {
            throw new SyntaxException(0);
        }

        if (server.getLevels().hasLevelUser(userID)) {
            user = server.getLevels().getLevelUser(userID);
            guildUser = command.getEvent().getGuild().getMemberById(userID);
        } else {
            throw new SyntaxException(1);
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(230, 230, 250));
        eb.setTitle("__**ArcBot Levels**__");
        eb.setFooter("Data for " + guildUser.getEffectiveName(), guildUser.getUser().getAvatarUrl());
        eb.addField("__Points__", "" + user.getPoints(), true);
        eb.addField("__Level__", "" + user.getLevel(), true);
        eb.addField("__Latest Level Up__", "" + user.getLevelHistory().get(user.getLevel()), true);
        command.getEvent().getChannel().sendMessage(eb.build()).queue();
    }

    /**
     * Provides the {@code -level leaderboard} command. This method uses a TreeMap to order all of a given {@link Server}'s
     * {@link LevelUser} objects by point values. Then, it selects the top ten (or all of them if there are not ten). The
     * data of these level users is displayed in descending order (from highest point value to lowest point value) in a
     * message embed.
     * @param command
     * The {@link CommandBox} that called the {@code execute()} method.
     * @param server
     * The server that was pulled from {@link Servers} which matches the guild from which the JDA event occured.
     * @throws SyntaxException
     * Exception is thrown if there are nay issues during run-time. The integer system is used in this method.
     * <br> • <Strong>{@code 0}</Strong>: This number exception is thrown when the command does not meet the following
     * requirements: (1) Arguments length of 1. (2) The argument "{@code leaderboard}" is the first (and only) argument given.
     */
    private void levelLeaderboard(CommandBox command, Server server) throws SyntaxException {
        if (command.getArgs().length == 1 && command.getArgs()[0].equalsIgnoreCase("leaderboard")) {
            TreeMap<Integer, LevelUser> orderedLevels = server.getLevels().getLevels();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(230, 230, 250));
            eb.setTitle(":clipboard: __**Level Leaderboard**__ :clipboard:");
            int count = 0;
            ArrayList<String> numberEmojis = new ArrayList<>(Arrays.asList(":one:", ":two:", ":three:", ":four:",
                    ":five:", ":six:", ":seven:", ":eight:", ":nine:", ":one::zero:"));
            for (Map.Entry<Integer, LevelUser> entry : orderedLevels.entrySet()) {
                if (count > 9) {
                    break;
                }
                Member levelMember = command.getEvent().getGuild().getMemberById(entry.getValue().getId());
                eb.addField(numberEmojis.get(count) + " " + levelMember.getEffectiveName(),
                        "**Points:** " + entry.getValue().getPoints() + "\t **Level:** " + entry.getValue().getLevel(),
                        false);
                count++;
            }
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new SyntaxException(0);
        }
    }
}
