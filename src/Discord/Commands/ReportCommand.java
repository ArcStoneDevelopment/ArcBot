package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Frame.FunctionFrame.Handler;
import Report.ReportHandler;
import Report.ReportStatus;
import Report.Report;
import Utility.*;
import Utility.Server.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

import java.awt.Color;

public class ReportCommand implements Command {
    @Override
    public String getInvoke() {
        return "report";
    }

    @Override
    public boolean execute(CommandBox command) {
        if (command.getArgs().length == 0) {
            if (!(Handler.openFunctions.containsKey(command.getEvent().getAuthor().getIdLong()))) {
                ReportHandler.start(command.getEvent());
                return true;
            } else {
                command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(new Frame.ResponseFrame.ErrorResponse(7))).queue();
                return false;
            }
        }
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            String subCommand = command.getArgs()[0].toLowerCase();
            switch (subCommand) {
                case "list":
                    listCommand(command, server);
                    return true;
            }
        } catch (PermissionException e) {
            return false;
        }
        return false;
    }

    private void listCommand(CommandBox command, Server server) throws PermissionException {
        if (server.hasPermission(command.getEvent().getMember(), Permission.STAFFTEAM)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(0,139,139));

            StringBuilder openReports = new StringBuilder();
            Guild guild = command.getEvent().getGuild();
            for (Report r : server.getReports(ReportStatus.OPEN)) {
                openReports.append("ID: **");
                openReports.append(r.getUuid());
                openReports.append("** - Sender: ");
                openReports.append(guild.getMemberById(r.getSenderID()).getEffectiveName());
                openReports.append("\n");
            }
            StringBuilder archiveReports = new StringBuilder();
            for (Report r : server.getReports(ReportStatus.ARCHIVED)) {
                archiveReports.append("ID: **");
                archiveReports.append(r.getUuid());
                archiveReports.append("** - Sender: ");
                archiveReports.append(guild.getMemberById(r.getSenderID()).getEffectiveName());
                archiveReports.append("\n");
            }

            eb.addField("__**Open Reports**__", openReports.toString(), true);
            eb.addField("__**Archive Reports**__", archiveReports.toString(), true);
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new PermissionException();
        }
    }
}
