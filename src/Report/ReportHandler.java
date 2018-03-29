package Report;

import Frame.FunctionFrame.Handler;
import Utility.Server;
import Utility.Servers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.awt.Color;

public class ReportHandler {
    public static void start(GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();
        Report report = new Report(event.getGuild().getIdLong(), event.getAuthor().getIdLong());
        pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
        report.build();
        Servers.activeServers.get(event.getGuild().getIdLong()).addReport(report);
        Handler.openFunctions.put(event.getAuthor().getIdLong(), report);
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
                } else {
                    pc.sendMessage(getResponse(-2, report)).queue();
                }
                break;
            case 2:
                report.setReportedName(event.getMessage().getContentRaw());
                pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
                report.build();
                break;
            case 3:
                report.setOffense(event.getMessage().getContentRaw());
                pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
                report.build();
                break;
            case 4:
                if (event.getMessage().getContentRaw().equalsIgnoreCase("done")) {
                    pc.sendMessage(getResponse(-3, report)).queue();
                    report.build();
                    break;
                }

                if (isValidEvidence(event.getMessage().getContentRaw())) {
                    report.addEvidence(event.getMessage().getContentRaw());
                    pc.sendMessage(getResponse(report.getBuildProgress(),report)).queue();
                    break;
                } else {
                    pc.sendMessage(getResponse(-4, report)).queue();
                    break;
                }
            case 5:
                if (event.getMessage().getContentRaw().equalsIgnoreCase("no")) {
                    report.setExtraInformation("None.");
                } else {
                    report.setExtraInformation(event.getMessage().getContentRaw());
                }
                pc.sendMessage(report.message(event)).queue();
                pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
                report.build();
                break;
            case 6:
                if (event.getMessage().getContentRaw().equalsIgnoreCase("yes")) {
                    report.setStatus(ReportStatus.OPEN);
                    server.removeReport(report.getUuid());
                    server.addReport(report);
                    pc.sendMessage(getResponse(report.getBuildProgress(), report)).queue();
                    return;
                } else {
                    server.removeReport(report.getUuid());
                    pc.sendMessage(getResponse(-1, report)).queue();
                    return;
                }
        }
        server.addReport(report);
        pc.close().complete();
    }

    private static MessageEmbed getResponse(int responseNumber, Report report) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0,139,139));
        switch (responseNumber) {
            case -4:
                eb.addField("Incorrect evidence format.", "*Please check to make sure you comply with the evidence guidelines.*", false);
                break;
            case -3:
                if (report.getEvidence().length == 0) {
                    eb.addField("Ok, no evidence has been filed. Is there anything else you would like to say?",
                            "*You may type any other information you would like to give here. If you have nothing else to add, send* `no`.", false);
                    break;
                } else {
                    eb.addField("Ok, I've successfully filed a total of " + report.getEvidence().length + " pieces of evidence. Would you like to add anything else?",
                            "*You may type any other information you would like to give here. If you have nothing else to add, send* `no`.", false);
                    break;
                }
            case -2:
                eb.addField("Response Not Valid.",
                        "*Please answer questions in the proper format.*", false);
                break;
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
                eb.addField("Fantastic! What is the IGN of the user you are reporting?",
                        "*Give their current IGN from minecraft.*", false);
                break;
            case 2:
                eb.addField("Ok, and what rule did " + report.getReportedName() + " break?",
                        "*Reference your server's rules if you need to do so.*", false);
                break;
            case 3:
                eb.setTitle("Ok, now I need you to provide any evidence you have to support this claim.");
                eb.setDescription("__**Please Follow These Rules For Evidence:**__ \n" +
                        "• Evidence will only be accepted from YouTube, Gyazo or Imgur. \n" +
                        "• Include `http://` or `https://` at the front of your links, but now `www`. For example: `http://youtube.com/...` \n" +
                        "• Enter links one at a time. Each link should have its own message. \n" +
                        "• When you are finished putting in your links, send the message `done`.");
                break;
            case 4:
                eb.addField("Evidence Filed.",
                        "*I have recorded evidence # " + report.getEvidence().length + " for your report. Remember to type* `done` *when you are finished filing evidence.*", false);
                break;
            case 5:
                eb.addField("This is what your report looks like. Are you ready to submit it?",
                        "*Answer* `yes` *to submit. Any other message will result in report deletion.*", false);
                break;
            case 6:
                eb.addField("Ok, I've submitted your report.", "*Please allow up to 48 hours for a staff member to respond.*", false);
                break;
            default:
                eb.addBlankField(false);
        }
        eb.setFooter("*Report UUID: " + report.getUuid() + "*", "https://imgur.com/a/yvWkm");
        return eb.build();
    }

    private static boolean isValidEvidence(String str) {
        if (str.startsWith("http://youtube.com/") || str.startsWith("http://gyazo.com/") || str.startsWith("http://imgur.com/")) {
            return true;
        } else if (str.startsWith("https://youtube.com/") || str.startsWith("https://gyazo.com/") || str.startsWith("https://imgur.com/")) {
             return true;
        }
        return false;
    }
}
