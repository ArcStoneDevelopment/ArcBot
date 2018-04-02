package Frame.FunctionFrame;

import java.io.Serializable;

/**
 * Provides an administrative access point for a function. Each function receives a Function object stored in
 * the {@link Utility.Server} object's HashMap. This object explains/names the function as well as serves
 * as a wrapper for the enable/disable boolean. This object does not change the functionality of a function.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class Function implements Serializable
{
    /**
     * The display name of the function.
     */
    private String name;

    /**
     * Boolean determining whether or not to handle anything related to this function on a server.
     */
    private boolean enabled;

    /**
     * A brief description of the function so that the server administrator may know what the function does/consists of
     */
    private String description;

    /**
     * Constructor for a Function -- variable assignment. (No other logic)
     * @param name
     * The name of the function (This may not be changed later).
     * @param enabled
     * The current status of the function on creation
     * @param description
     * The description of the function (This may not be changed later).
     */
    public Function(String name, boolean enabled, String description)
    {
        this.name = name;
        this.enabled = enabled;
        this.description = description;
    }

    /**
     * Access the name of the function
     * @return String - Function Name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Access the current state of the {@code enabled} boolean.
     * @return boolean - enabled (True if enabled. False if disabled.)
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * Access the description of the function.
     * @return String - Function description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Change the state of the {@code enabled} variable.
     * @param enabled
     * The new state of the variable.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
