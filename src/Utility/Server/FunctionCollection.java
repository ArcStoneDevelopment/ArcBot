package Utility.Server;

import Frame.FunctionFrame.Function;
import Utility.ServerException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * A wrapper for the {@link Function} TreeMap that stores server-specific data about each {@link Function}.
 *
 * @author ArcStone Development LLC
 * @version v2.0
 * @since v2.0
 */
public class FunctionCollection implements Serializable {

    /**
     * The primary map of data that is encapsulated in this object.
     * <br> The use of a {@code TreeMap} over a {@code HashMap} allows functions to be listed in order.
     * <br> The String is the name of the {@link Function}.
     * <br> After object creation, no {@link Function} objects may be added or removed, they may only be viewed.
     */
    private TreeMap<String, Function> core;

    /**
     * The package-private constructor which populations the {@code core} TreeMap.
     * <br> <strong>The following functions are added:</strong>
     * <ol>
     *     <li>The {@code Discord} function - This function controls access to commands.</li>
     *     <li>The {@code Level} function - This function controls the {@link Levels} system.</li>
     *     <li>The {@code Report} function - This function controls the {@link Report} private-message system.</li>
     *     <li>The {@code Appeal} function - This function controls the {@link Appeal} private-message system.</li>
     * </ol>
     */
    FunctionCollection() {
        core = new TreeMap<>() {{
            put("discord", new Function("discord", true, "N/A"));
            put("level", new Function("level", true, "N/A"));
            put("report", new Function("report", true, "N/A"));
            put ("appeal", new Function("appeal", true, "N/A"));
        }};
    }

    /**
     * Access a {@code Set} containing all of the {@link Function} objects in the {@code core} map.
     * @return {@code Set<Function>}
     */
    public Set<Function> getFunctions() {
        return new HashSet<>(core.values());
    }

    /**
     * Determine if a given string is the valid name of a function.
     * @param name
     * The string to check against the {@code core} map.
     * @return boolean - True if the String is a function name, false otherwise.
     */
    public boolean isFunction(String name) {
        return core.keySet().contains(name);
    }

    /**
     * Access a {@link Function} object by name.
     * @param name
     * The name of the function to retrieve.
     * @return {@link Function} - The corresponding function object.
     * @throws ServerException
     * This exception is thrown if the given String is not a valid Function name.
     */
    public Function getFunction(String name) throws ServerException {
        if (core.containsKey(name)) {
            return core.get(name);
        }
        throw new ServerException();
    }
}
