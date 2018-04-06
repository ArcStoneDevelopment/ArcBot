package Frame.FunctionFrame;

import Utility.Server.Server;

import java.util.UUID;

/**
 * Ensures proper object management in the {@link Handler} class. Only three pieces of information are necessary in the
 * {@link Handler} class' HashMap. Since there is not a Collection to properly map three pieces of data to a single key,
 * this interface allows any function object to be stored in the Handler with only the information required for recognition
 * of how to go about accessing and running the proper function object. This prevents full Function Objects from being
 * stored in the Handler HashMap (as opposed to the proper {@link Server} object HashMap.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
public interface FunctionOutline {
    /**
     * Access the UUID of the function object.
     * @return UUID
     */
    UUID getUuid();

    /**
     * Access the Discord ID of the server where this function object is stored.
     * @return long - Server ID
     */
    long getServerID();

    /**
     * Determine which type this function is (For casting purposes mainly).
     * @return FunctionType - The type of the function object.
     */
    FunctionType getSOURCE();

}
