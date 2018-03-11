package Utility.Model;

import Discord.Discord;
import Utility.Settings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

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
    private TreeMap<Long, LevelUser> levels;

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

    private HashMap<Integer, Report> incompleteReport;
    private HashMap<Integer, Report> openReport;
    private HashMap<Integer, Report> archiveReport;

    private HashMap<Integer, Appeal> incompleteAppeal;
    private HashMap<Integer, Appeal> openAppeal;
    private HashMap<Integer, Appeal> archiveAppeal;

    private HashMap<Integer, StaffApp> incompleteApply;
    private HashMap<Integer, StaffApp> openApply;
    private HashMap<Integer, StaffApp> archiveApply;

    public Server(Guild guild) {
        this.id = guild.getIdLong();
        settings = new HashMap<>(Settings.defaultGuildSettings);
        levels = new TreeMap<>();

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

        rolePermissions = new HashMap<>();

        incompleteReport = new HashMap<>();
        openReport = new HashMap<>();
        archiveReport = new HashMap<>();

        incompleteAppeal = new HashMap<>();
        openAppeal = new HashMap<>();
        archiveAppeal = new HashMap<>();

        incompleteApply = new HashMap<>();
        openApply = new HashMap<>();
        archiveApply = new HashMap<>();
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

    public void setLevelUser(long id, LevelUser user) {
        if (levels.containsKey(id)) {
            levels.remove(id);
        }
        levels.put(id, user);
    }

    public boolean textChannelsInit() {
        if (textChannels.get("spam") == -1L) {
            return false;
        } else {
            return true;
        }
    }

    public void initTextChannel(String name, Long id) {
        textChannels.remove(name);
        textChannels.put(name, id);
    }

    public long getTextChannelID(String name) {
        return textChannels.get(name);
    }

    public Function getFunction (String name) {
        return functions.get(name);
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

    public void disableCommands (String invoke) {
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

    public Permission getPermission (Role role) {
        return rolePermissions.getOrDefault(role.getIdLong(), Permission.Default);
    }

    public void setPermission(Role role, Permission permission) {
        if (rolePermissions.containsKey(role.getIdLong())) {
            rolePermissions.remove(role.getIdLong());
        }
        rolePermissions.put(role.getIdLong(), permission);
    }
}
