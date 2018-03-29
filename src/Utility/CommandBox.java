package Utility;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Container for all the pertinent information relating to a JDA Guild Message Received Event. In order for the code in the
 * command classes to be efficient, message events need to be broken down into a set of key pieces. Objects made from this
 * class contain the original event along with a few pieces of key information that have been parsed out by the
 * {@link Frame.BotFrame.Parser}.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public class CommandBox
{
    /**
     * The raw text from the message.
     */
    private String raw;

    /**
     * The first key word after the command prefix in a message. This should be tested against the {@code getInvoke()}
     * method in various {@link Command} objects to see which command should be ran.
     */
    private String invoke;

    /**
     * Arguments added after the invoke key are placed in this arguments array. Each element in this array is its own
     * string as separated by a space.
     */
    private String[] args;

    /**
     * The actual JDA event that has been parsed into this command box.
     */
    private GuildMessageReceivedEvent event;

    /**
     * This constructor populates all of the instance variables.
     * @param raw
     * The String to be assigned to the {@code raw} variable.
     * @param invoke
     * The String to be assigned to the {@code invoke} variable.
     * @param args
     * The String[] array to be assigned to the {@code args} variable.
     * @param event
     * The JDA event to be assigned to the {@code event} variable.
     */
    public CommandBox(String raw, String invoke, String[] args, GuildMessageReceivedEvent event)
    {
        this.raw = raw;
        this.invoke = invoke;
        this.args = args;
        this.event = event;
    }

    /**
     * Access the {@code raw} instance variable.
     * @return String
     */
    public String getRaw()
    {
        return raw;
    }

    /**
     * Access the {@code invoke} instance variable.
     * @return String
     */
    public String getInvoke()
    {
        return invoke;
    }

    /**
     * Access the {@code args} instance variable.
     * @return String[]
     */
    public String[] getArgs()
    {
        return args;
    }

    /**
     * Access the {@code event} instance variable.
     * @return GuildMessageReceivedEvent
     */
    public GuildMessageReceivedEvent getEvent()
    {
        return event;
    }
}
