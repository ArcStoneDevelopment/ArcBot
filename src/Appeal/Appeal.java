package Appeal;

import Frame.FunctionFrame.FunctionCore;
import Frame.FunctionFrame.FunctionType;
import Utility.SystemTime;

public class Appeal extends FunctionCore<AppealStatus> {

    private String ign;
    private String banReason;
    private String banTime;
    private Boolean beenBanned;
    private String previousBanReason;

    private String unbanReason;
    private String extraInformation;

    private long handlerID;
    private String timeClosed;
    private String closingNote;

    public Appeal(long serverID, long senderID) {
        super(FunctionType.APPEAL, AppealStatus.INCOMPLETE, serverID, senderID);
        ign = "";
        banReason = "";
        banTime = "";
        beenBanned = null;
        previousBanReason = "";
        unbanReason = "";
        extraInformation = "";
        handlerID = -1L;
        timeClosed = "";
        closingNote = "";
    }

    public void close() {
        this.timeClosed = SystemTime.getTime();
        this.closingNote = "None Given.";
        this.setStatus(AppealStatus.ARCHIVED);
    }

    public void close(String closingNote) {
        this.timeClosed = SystemTime.getTime();
        this.closingNote = closingNote;
        this.setStatus(AppealStatus.ARCHIVED);
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public void setBanTime(String banTime) {
        this.banTime = banTime;
    }

    public void setBeenBanned(Boolean beenBanned) {
        this.beenBanned = beenBanned;
    }

    public void setPreviousBanReason(String previousBanReason) {
        this.previousBanReason = previousBanReason;
    }

    public void setUnbanReason(String unbanReason) {
        this.unbanReason = unbanReason;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public void setHandlerID(long handlerID) {
        this.handlerID = handlerID;
    }

    public String getIgn() {
        return ign;
    }

    public String getBanReason() {
        return banReason;
    }

    public String getBanTime() {
        return banTime;
    }

    public Boolean getBeenBanned() {
        return beenBanned;
    }

    public String getPreviousBanReason() {
        return previousBanReason;
    }

    public String getUnbanReason() {
        return unbanReason;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public long getHandlerID() {
        return handlerID;
    }

    public String getTimeClosed() {
        return timeClosed;
    }

    public String getClosingNote() {
        return closingNote;
    }
}
