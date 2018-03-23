package Discord.Commands;

import ResponseFrame.ErrorResponse;
import ResponseFrame.LevelResponse;
import ResponseFrame.ResponseBuilder;
import Utility.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.*;

// TODO: JavaDocs for this class and its methods.
public class LevelCommand implements Command {
    @Override
    public String getInvoke() {
        return "level";
    }

    @Override
    public boolean execute(CommandBox command) {
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (!(server.getFunction("level").isEnabled())) {
                MessageEmbed msg = ResponseBuilder.INSTANCE.build(new ErrorResponse(4));
                command.getEvent().getChannel().sendMessage(msg).queue();
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
                case 0:
                    command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new ErrorResponse(2))).queue();
                    break;
                case 1:
                    command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new LevelResponse(0))).queue();
                    break;
            }
        }
        return false;
    }

    private void levelUser(CommandBox command, Server server) throws SyntaxException {
        if (command.getArgs().length == 0) {
            long userID = command.getEvent().getAuthor().getIdLong();
            if (server.hasLevelUser(userID)) {
                LevelUser user = server.getLevelUser(userID);
                Member guildUser = command.getEvent().getGuild().getMemberById(userID);
                EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(new Color(230, 230, 250));
                    eb.setTitle("__**ArcBot Levels**__");
                    eb.setFooter("Data for " + guildUser.getEffectiveName(), guildUser.getUser().getAvatarUrl());
                    eb.addField("__Points__", "" + user.getPoints(), true);
                    eb.addField("__Level__", "" + user.getLevel(), true);
                    eb.addField("__Latest Level Up__", "" + user.getLevelHistory().get(user.getLevel()), true);
                command.getEvent().getChannel().sendMessage(eb.build()).queue();
            } else {
                throw new SyntaxException(1);
            }
        } else if (command.getEvent().getMessage().getMentionedUsers().size() == 1) {
            long userID = command.getEvent().getMessage().getMentionedUsers().get(0).getIdLong();
            if (server.hasLevelUser(userID)) {
                LevelUser user = server.getLevelUser(userID);
                Member guildUser = command.getEvent().getGuild().getMemberById(userID);
                EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(new Color(230, 230, 250));
                    eb.setTitle("__**ArcBot Levels**__");
                    eb.setFooter("Data for " + guildUser.getEffectiveName(), guildUser.getUser().getAvatarUrl());
                    eb.addField("__Points__", "" + user.getPoints(), true);
                    eb.addField("__Level__", "" + user.getLevel(), true);
                    eb.addField("__Latest Level Up__", "" + user.getLevelHistory().get(user.getLevel()), true);
                command.getEvent().getChannel().sendMessage(eb.build()).queue();
            } else {
                throw new SyntaxException(1);
            }
        } else {
            throw new SyntaxException(0);
        }
    }

    private void levelLeaderboard(CommandBox command, Server server) throws SyntaxException {
        if (command.getArgs().length == 1 && command.getArgs()[0].equalsIgnoreCase("leaderboard")) {
            TreeMap<Integer, LevelUser> orderedLevels = new TreeMap<>(Collections.reverseOrder());
            for (Map.Entry<Long, LevelUser> entry : server.getLevels().entrySet()) {
                orderedLevels.put(entry.getValue().getPoints(), entry.getValue());
            }
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
