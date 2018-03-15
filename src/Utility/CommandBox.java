package Utility;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandBox
{

    private String raw;
    private String beheaded;
    private String[] splitBeheaded;
    private String invoke;
    private String[] args;
    private GuildMessageReceivedEvent event;

    public CommandBox(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, GuildMessageReceivedEvent event)
    {
        this.raw = raw;
        this.beheaded = beheaded;
        this.splitBeheaded = splitBeheaded;
        this.invoke = invoke;
        this.args = args;
        this.event = event;
    }

    public String getRaw()
    {
        return raw;
    }

    public String getBeheaded()
    {
        return beheaded;
    }

    public String[] getSplitBeheaded()
    {
        return splitBeheaded;
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
