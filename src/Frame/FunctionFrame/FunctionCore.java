package Frame.FunctionFrame;

import Utility.SystemTime;

import java.io.Serializable;
import java.util.UUID;

/**
 * Serves as a superclass type for all of the FunctionType objects to standardize certain pieces of inferred information.
 * The FunctionCore class is abstract to prevent improper usage, not because any methods require implementation.
 * <br> For a better explanation of the idea behind functions, read the overview for this package.
 * @param <StatusType>
 * This should be one of the StatusType enums. This type should match the function of the subtype.
 */
public abstract class FunctionCore<StatusType> implements Serializable {

    /**
     * This is the type of function object that is being created. This should match the {@code StatusType} that is provided
     * as a parameter to the class generic.
     * <br> <Strong>For Example:</Strong> If a {@code report} object is being created, the source should be {@code FunctionType.REPORT}
     * and the {code StatusType} should be {@code ReportStatus}. Mixing these variables can cause serious problems in runtime.
     */
    private FunctionType SOURCE;

    /**
     * The status enum that matches the type of function object that is being created. This should match the {@code SOURCE}
     * variable.
     * <br> <Strong>For Example:</Strong> If a {@code report} object is being created, the source should be {@code FunctionType.REPORT}
     * and the {code StatusType} should be {@code ReportStatus}. Mixing these variables can cause serious problems in runtime.
     */
    private StatusType status;

    /**
     * The ID of the server from which this function object was created. This should match the server ID in {@link Utility.Servers}.
     */
    private long serverID;

    /**
     * The randomly generated UUID that is assigned to this function object.
     */
    private UUID uuid;

    /**
     * The variable used to track the status of this function object as it is being created through the private message system.
     * Once the function has been completed, this variable becomes obsolete.
     */
    private int buildProgress;

    /**
     * The String representation of time here is directly from {@link SystemTime}.
     */
    private String timeFiled;

    /**
     * The JDA ID of the user that created this function.
     */
    private long senderID;

    /**
     * This constructor cannot be used directly, as this is an abstract class. This constructor should be called from all
     * sub-classes. Once the initialization process is complete, this object is added to the {@link Handler}.
     * <br> <Strong>Every variable is initialized here:</Strong>
     * <ol>
     *     <li>{@code uuid} is randomly generated.</li>
     *     <li>{@code buildProgress} starts at 0</li>
     *     <li>{@code timeFiled} is pulled from {@link SystemTime}</li>
     * </ol>
     * @param source
     * The type of function that is being created.
     * @param status
     * The first status to be initialized to this function.  This should be {@code INCOMPLETE} or something similar.
     * @param serverID
     * The ID of the server from which this function was started. This should be the ID recognized in {@link Utility.Servers}
     * @param senderID
     * The JDA ID of the user that started this function.
     */
     public FunctionCore(FunctionType source, StatusType status, long serverID, long senderID) {
        this.SOURCE = source;
        this.status = status;
        this.serverID = serverID;
        this.uuid = UUID.randomUUID();
        this.buildProgress = 0;
        this.timeFiled = SystemTime.getTime();
        this.senderID = senderID;
        Handler.incompleteFunctions.put(this.senderID, this.uuid);
    }

    /**
     * Increments the buildProgress variable.
     */
    public void build() {
        buildProgress++;
    }

    /**
     * Accesses the current state of {@code buildProgress}
     * @return int - Build progress' current value.
     */
    public int getBuildProgress() {
        return buildProgress;
    }

    /**
     * Access the current status of this Function.
     * @return StatusType - The current status of this function in the type enum defined in the generic on object creation.
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Access the randomly generated UUID.
     * @return UUID - The unique ID for this function object.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Access the standard String representation of the time that this object was created.
     * @return String - Formatted from {@link SystemTime}
     */
    public String getTimeFiled() {
        return timeFiled;
    }

    /**
     * Access the JDA given ID of the user that started this function.
     * @return long - The ID
     */
    public long getSenderID() {
        return senderID;
    }

    /**
     * Access the JDA given ID of the server from which this function was started. This ID is recognized in {@link Utility.Servers}.
     * @return long - The ID
     */
    public long getServerID() {
        return serverID;
    }

    /**
     * Access the type of this function. This is important when casting back to the proper sub-class.
     * @return FunctionType
     */
    public FunctionType getSOURCE() {
        return SOURCE;
    }

    /**
     * Access the current status of this function.
     * @param status StatusType - The current status of the function within the parameters of the enum.
     */
    public void setStatus(StatusType status) {
         this.status = status;
    }
}
