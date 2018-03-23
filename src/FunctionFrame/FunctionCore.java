package FunctionFrame;

import Utility.SystemTime;

import java.io.Serializable;
import java.util.UUID;

abstract class FunctionCore<StatusType> implements Serializable {
    private FunctionType SOURCE;
    private StatusType status;
    private long serverID;
    private UUID uuid;
    private int buildProgress;
    private String timeFiled;
    private long senderID;

     FunctionCore(FunctionType source, StatusType status, long serverID, long senderID) {
        this.SOURCE = source;
        this.status = status;
        this.serverID = serverID;
        this.uuid = UUID.randomUUID();
        this.buildProgress = 0;
        this.timeFiled = SystemTime.getTime();
        this.senderID = senderID;
        Handler.incompleteFunctions.put(this.senderID, this.uuid);
    }

    public void build() {
        buildProgress++;
    }

    public int getBuildProgress() {
        return buildProgress;
    }
    public StatusType getStatus() {
        return status;
    }
    public UUID getUuid() {
        return uuid;
    }
    public String getTimeFiled() {
        return timeFiled;
    }
    public long getSenderID() {
        return senderID;
    }
    public long getServerID() {
        return serverID;
    }
    public FunctionType getSOURCE() {
        return SOURCE;
    }
}
