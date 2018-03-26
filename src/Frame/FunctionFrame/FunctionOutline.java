package Frame.FunctionFrame;

import java.util.UUID;

/**
 * Ensures proper object management in the {@link Handler} class. Only three pieces of information are necessary in the
 * {@link Handler} class' HashMap. Since there is not a Collection to properly map three pieces of data to a single key,
 * this interface allows any function object to be stored in the Handler with only the information required for recognition
 * of how to go about accessing and running the proper function object. This prevents full Function Objects from being
 * stored in the Handler HashMap (as opposed to the proper {@link Utility.Server} object HashMap.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public interface FunctionOutline {
    UUID getUuid();
    long getServerID();
    FunctionType getSOURCE();

}
