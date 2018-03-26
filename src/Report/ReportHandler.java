package Report;

import Frame.FunctionFrame.FunctionCore;
import Frame.FunctionFrame.Handler;
import Utility.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.awt.Color;
import java.util.UUID;

public class ReportHandler {
    public static void start(GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();
        Report report = new Report(event.getGuild().getIdLong(), event.getAuthor().getIdLong());
        pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
        report.build();
        Servers.activeServers.get(event.getGuild().getIdLong()).addReport(report);
    }

    public static void handle(PrivateMessageReceivedEvent event) {
        Server server = Servers.activeServers.get(Handler.openFunctions.get(event.getAuthor().getIdLong()).getServerID());
        Report report = server.getReport(Handler.openFunctions.get(event.getAuthor().getIdLong()).getUuid());
        PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();

        if (event.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
            pc.sendMessage(getResponse(-1, report)).queue();
            pc.close().complete();
            server.removeReport(report.getUuid());
            return;
        }

        switch (report.getBuildProgress()) {
            case 1:
                if (event.getMessage().getContentRaw().equalsIgnoreCase("yes")) {
                    pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
                    report.build();
                }
                break;
        }
        server.addReport(report);
        pc.close().complete();
    }

    private static MessageEmbed getResponse(int buildProgress, Report report) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0,139,139));
        switch (buildProgress) {
            case -1:
                eb.addField("Report Cancelled.",
                        "*I've removed your report from the database. Have a nice day!*", false);
                break;
            case 0:
                eb.setTitle("ArcBot Reports.");
                eb.setDescription("*Please read the following rules before continuing:* \n" +
                        "**1.** Falsifying reports is not permitted in any way, shape or form. \n" +
                        "**2.** Answer the questions completely and clearly so that we can get your report handled \n" +
                        "**3.** Allow up to 48 hours to get your report processed. \n" +
                        "**4.** Do not discuss your report with anyone before, during or after its handling. \n" +
                        "**5.** Evidence must be added as links. If you do not follow this rule, the bot will not accept the evidence. \n" +
                        "**6.** Evidence cannot be cropped or manipulated in any way, shape or form.");
                eb.addBlankField(false);
                eb.addField("Now that all of that is out of the way, would you like to start a report?",
                        "**Answer* `yes` *to continue. You may respond with* `cancel` *at any time to remove your report.*", false);
                break;
            case 1:
            default:
                eb.addBlankField(false);
        }
        eb.setFooter("*Report UUID: " + report.getUuid() + "*", "https://imgur.com/a/yvWkm");
        return eb.build();
    }
}
