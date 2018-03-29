package Report;

import Frame.FunctionFrame.FunctionCore;
import Frame.FunctionFrame.FunctionType;
import Utility.SystemTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class Report extends FunctionCore<ReportStatus> {
    private String reportedName;
    private String offense;
    private ArrayList<String> evidence;
    private String extraInformation;
    private long handlerID;
    private String timeClosed;
    private String closingMessage;

    public Report(long serverID, long senderID) {
        super(FunctionType.REPORT, ReportStatus.INCOMPLETE, serverID, senderID);
        reportedName = "";
        offense = "";
        evidence = new ArrayList<>();
        extraInformation = "";
        handlerID = -1L;
        timeClosed = "";
        closingMessage = "";
    }

    public void setReportedName(String reportedName) {
        this.reportedName = reportedName;
    }
    public void setOffense(String offense) {
        this.offense = offense;
    }
    public void addEvidence(String evidence) {
        this.evidence.add(evidence);
    }
    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }
    public void setHandlerID(long handlerID) {
        this.handlerID = handlerID;
    }
    public void close() {
        this.timeClosed = SystemTime.getTime();
        this.closingMessage = "None Given.";
        super.setStatus(ReportStatus.ARCHIVED);
    }
    public void close(String closingMessage) {
        this.timeClosed = SystemTime.getTime();
        this.closingMessage = closingMessage;
        super.setStatus(ReportStatus.ARCHIVED);
    }

    public String getReportedName() {
        return this.reportedName;
    }
    public String getOffense() {
        return this.offense;
    }
    public String[] getEvidence() {
        return this.evidence.toArray(new String[evidence.size()]);
    }
    public String getExtraInformation() {
        return this.extraInformation;
    }
    public long getHandlerID() {
        return this.handlerID;
    }
    public String getTimeClosed() {
        return this.timeClosed;
    }
    public String getClosingMessage() {
        return this.closingMessage;
    }

    public MessageEmbed message(Event event) {
        Guild guild = event.getJDA().getGuildById(this.getServerID());
        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(new Color(0, 139, 139));
        eb.setTitle("Report (ID: " + this.getUuid() +")");
        eb.setDescription("__**Status:**__ " + this.getStatus().getStatus());
        eb.setFooter("*Report UUID: " + this.getUuid() + "*", "https://imgur.com/a/yvWkm");

        eb.addField("Reporter", guild.getMemberById(this.getSenderID()).getEffectiveName(), true);
        eb.addField("Reported User", this.getReportedName(), true);
        eb.addField("Offense", this.getOffense(), true);
        eb.addField("Extra Information", this.getExtraInformation(), false);

        if (this.getEvidence().length == 0) {
            eb.addField("Evidence", "None Given", false);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : this.getEvidence()) {
                sb.append(s);
                sb.append("\n");
            }
            eb.addField("Evidence", sb.toString(), false);
        }

        if (!(this.getHandlerID() == -1L)) {
            eb.addField("Staff Handler", guild.getMemberById(this.getHandlerID()).getEffectiveName(), true);
        }

        if (!(this.getClosingMessage().isEmpty())) {
            eb.addField("Closed - " + this.getTimeClosed(), this.getClosingMessage(), true);
        }

        return eb.build();
    }
}
