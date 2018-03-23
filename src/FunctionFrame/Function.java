package FunctionFrame;

import java.io.Serializable;

public class Function implements Serializable
{
    private String name;
    private boolean enabled;
    private String description;

    public Function(String name, boolean enabled, String description)
    {
        this.name = name;
        this.enabled = enabled;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public String getDescription()
    {
        return description;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
