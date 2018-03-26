package Utility;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandBox
{
    private String raw;
    private String invoke;
    private String[] args;
    private GuildMessageReceivedEvent event;

    public CommandBox(String raw, String invoke, String[] args, GuildMessageReceivedEvent event)
    {
        this.raw = raw;
        this.invoke = invoke;
        this.args = args;
        this.event = event;
    }

    public String getRaw()
    {
        return raw;
    }

    public String getInvoke()
    {
        return invoke;
    }

    public String[] getArgs()
    {
        return args;
    }

    public GuildMessageReceivedEvent getEvent()
    {
        return event;
    }
}
