package Utility;

import Discord.Discord;
import Frame.FunctionFrame.*;
import Report.Report;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.*;

// TODO: Javadocs
/**
 * Defines how the bot should interact with a particular Discord server. This class contains settings, level data and
 * all function data for a particular server. These classes are expected to be saved to SQL on occasion to ensure no loss
 * of data in the event of an unexpected shut down.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class Server implements Serializable {

    private long id;

    public boolean drop;

    private long ownerID;

//    TODO: Changing of these settings needs to be implemented/propagated throughout the rest of the bot.
    /**
     * Storage of certain server settings. Bot server settings are stored in this HashMap. These settings are originally
     * pulled from {@link Settings} upon object construction, but can be changed by the bot user with appropriate
     * permissions. The key in this map is the name of the setting, and the value in this map is the actual setting.
     * <br> Current Default Settings:
     * <ul>
     *     <li> {@code prefix} | {@code -}</li>
     * </ul>
     */
    private HashMap<String, String> settings;

    /**
     * Provides storage for {@link LevelUser} objects used for the chat level system. Each Discord user that chats in
     * this server has a corresponding LevelUser object stored in this TreeMap. The TreeMap format enables custom
     * comparators to be applied when constructing a leaderboard of the top users. The key in this map is the discord
     * snowflake ID for the user to whom the {@link LevelUser} object (which is the value) matches.
     */
    private HashMap<Long, LevelUser> levels;

    /**
     * Provides storage for the Discord IDs of TextChannels that have been designated for bot use. The key in this map
     * is the name of the text channel (as used in the code, not necessarily the actual text channel name) and the value
     * is the discord snowflake ID for the text channel.
     * <br> The current text channel names:
     * <ul>
     *     <li>{@code spam}</li>
     *     <li>{@code log}</li>
     *     <li>{@code report}</li>
     *     <li>{@code appeal}</li>
     *     <li>{@code apply}</li>
     * </ul>
     */
    private HashMap<String, Long> textChannels;

    /**
     * Provides storage for {@link Function} objects that will determine access to various bot functions. For more
     * information about what functions are, and the way that they are used, please see {@link Function}. The key in the
     * map is the name of the function (as used in the code), and the value is the actual {@link Function} object.
     */
    private TreeMap<String, Function> functions;

    /**
     * Provides storage for {@link Command} objects. These are the commands that are available to be processed by the bot.
     * The key in the map is the {@link Command} object. The value in the map is the boolean determining if the bot should
     * manage this command. If true, this command will run. If false, the command will not run.
     */
    private HashMap<Command, Boolean> commands;

    /**
     * Tells the bot which Roles in the discord server should be matched to which {@link Permission}. The key in this
     * map is the discord ID of the role to which the {@link Permission}, which is the value, corresponds.
     */
    private HashMap<Long, Permission> rolePermissions;

    private HashMap<UUID, Report> incompleteReport;
    private HashMap<UUID, Report> openReport;
    private HashMap<UUID, Report> archiveReport;

    public Server(Guild guild) {
        System.out.println("Registered new server.");
        this.id = guild.getIdLong();
        this.ownerID = guild.getOwner().getUser().getIdLong();
        this.drop = false;
        settings = new HashMap<>(Settings.defaultGuildSettings);
        levels = new HashMap<>();

        textChannels = new HashMap<>() {{
            put("spam", -1L);
            put("log", -1L);
            put("report", -1L);
            put("appeal", -1L);
            put("apply", -1L);
        }};

        functions = new TreeMap<>() {{
           put("discord", new Function("discord", true, "N/A"));
           put("level", new Function("level", true, "N/A"));
           put("report", new Function("report", true, "N/A"));
           put ("appeal", new Function("appeal", true, "N/A"));
        }};

        commands = new HashMap<>() {{
           for (Command c : Discord.commands)
           {
               put(c, true);
           }
        }};
        System.out.println("Commands: " + commands.size());

        rolePermissions = new HashMap<>();

        incompleteReport = new HashMap<>();
        openReport = new HashMap<>();
        archiveReport = new HashMap<>();
    }

    public long getOwnerID() {
        return this.ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getID() {
        return this.id;
    }

    public String getSetting(String settingName) {
        return settings.get(settingName);
    }

    public boolean hasLevelUser(long id) {
        return levels.containsKey(id);
    }

    public LevelUser getLevelUser(long id) {
        return levels.get(id);
    }

    public HashMap<Long, LevelUser> getLevels() {
        return levels;
    }

    public void setLevelUser(long id, LevelUser user) {
        if (levels.containsKey(id)) {
            levels.remove(id);
        }
        levels.put(id, user);
    }

    public Set<String> getTextChannelNames() {
        return textChannels.keySet();
    }

    public String getTextChannelName(long id) {
        for (Map.Entry<String, Long> entry : textChannels.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return "";
    }

    public void clearTextChannel(String name) {
        textChannels.remove(name);
        textChannels.put(name, -1L);
    }

    public boolean textChannelsInit(String name) {
        return !(textChannels.get(name) == -1L);
    }

    public void initTextChannel(String name, Long id) {
        textChannels.remove(name);
        textChannels.put(name, id);
    }

    public long getTextChannelID(String name) {
        return textChannels.get(name);
    }

    public Collection<Long> getTextChannels() {
        return textChannels.values();
    }

    public Collection<Function> getFunctions() {
        return functions.values();
    }

    public boolean isFunction(String name) {
        return functions.keySet().contains(name);
    }

    public Function getFunction (String name) {
        return functions.get(name);
    }

    public Set<Command> getCommands() {
        return commands.keySet();
    }

    public boolean isCommand(String invoke) {
        for (Command c : commands.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return true;
            }
        }
        return false;
    }

    public Command getCommand(String invoke) {
        for (Command c : commands.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return c;
            }
        }
        return null;
    }

    public void enableCommand (String invoke) {
        for (Command c : commands.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                commands.remove(c);
                commands.put(c, true);
                return;
            }
        }
    }

    public void disableCommand (String invoke) {
        for (Command c : commands.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                commands.remove(c);
                commands.put(c, false);
                return;
            }
        }
    }

    public boolean getCommandStatus (String invoke) {
        for (Command c : commands.keySet()) {
            if (c.getInvoke().equalsIgnoreCase(invoke)) {
                return commands.get(c);
            }
        }
        return false;
    }

    public boolean hasPermission(Member member, Permission permission) {
        Permission highestPermission = Permission.DEFAULT;
        for (Role r : member.getRoles()) {
            if (Permission.comparator.compare(getPermission(r), highestPermission) > 0) {
                highestPermission = getPermission(r);
            }
        }
        return Permission.comparator.compare(highestPermission, permission) >= 0;
    }

    public boolean isPermission(String name) {
        ArrayList<String> permissions = new ArrayList<>() {{
            add("OWNER");
            add("STAFFTEAM");
            add("DONATOR");
            add("DEFAULT");
        }};
        return permissions.contains(name.toUpperCase());
    }

    public Permission getPermission (Role role) {
        return rolePermissions.getOrDefault(role.getIdLong(), Permission.DEFAULT);
    }

    public void setPermission(Role role, Permission permission) {
        if (rolePermissions.containsKey(role.getIdLong())) {
            rolePermissions.remove(role.getIdLong());
        }
        rolePermissions.put(role.getIdLong(), permission);
    }

    public FunctionType getIncompleteFunctionType(UUID uuid) {
        for (UUID id : incompleteReport.keySet()) {
            if (id.equals(uuid)) {
                return FunctionType.REPORT;
            }
        }
        return null;
    }

    public Report getReport(UUID uuid) {
        if (openReport.containsKey(uuid)) {
            return openReport.get(uuid);
        }
        return null;
    }
}

