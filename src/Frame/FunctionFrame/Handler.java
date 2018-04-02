package Frame.FunctionFrame;

import java.util.HashMap;

/**
 * Central location for storing any Function Objects that are incomplete. They are stored in the {@link FunctionOutline}
 * format so that they might be located/casted correctly to the proper sub-class implementations.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.0
 */
public class Handler {
    /**
     * The master HashMap containing all open/incompleted function objects.
     */
    public static HashMap<Long, FunctionOutline> openFunctions = new HashMap<>();
}
