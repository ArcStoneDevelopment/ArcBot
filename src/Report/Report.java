package Report;

import Frame.FunctionFrame.FunctionCore;
import Frame.FunctionFrame.FunctionType;
import Utility.SystemTime;

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
}
