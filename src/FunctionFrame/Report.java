package FunctionFrame;

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
    }
}
