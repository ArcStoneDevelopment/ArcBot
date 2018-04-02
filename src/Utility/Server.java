package Utility;

import Discord.Discord;
import Frame.FunctionFrame.*;
import Levels.LevelUser;
import Report.Report;
import Report.ReportStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.*;

/**
 * Defines how the bot should interact with a particular Discord server. This class contains settings, level data and
 * all function data for a particular server. These classes are expected to be saved to SQL on occasion to ensure no loss
 * of data in the event of an unexpected shut down.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
public class Server implements Serializable {

    /**
     * The discord ID of the guild whose data is stored in this object.
     */
    private long id;

    /**
     * When a guild is no longer registered with JDA, this variable should be set to true to signal the bot to drop this
     * object from the SQL table.
     */
    public boolean drop;

    /**
     * The discord ID of the member who is the owner of the discord server. This is the only user who may use the {@link Discord.Commands.ServerEditorCommand}.
     */
    private long ownerID;

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
     * comparators to be applied when constructing a leader board of the top users. The key in this map is the discord
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

    /**
     * Reports that have not been finished through the private message system are stored here by the report UUID.
     */
    private HashMap<UUID, Report> incompleteReport;

    /**
     * Reports that have been finished, but have not been marked for archival by a staff member are stored here by report UUID.
     */
    private HashMap<UUID, Report> openReport;

    /**
     * Reports that have been finished and archived by a staff member are stored here by report UUID.
     */
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

    /**
     * Access the discord ID of the member who is registered in discord as the owner of this server.
     * @return long - The ID.
     */
    public long getOwnerID() {
        return this.ownerID;
    }

    /**
     * Change the owner of the server. This should only be called through the {@link Frame.BotFrame.Listeners.BotListener}.
     * @param ownerID
     * The discord ID of the new owner.
     */
    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * The discord ID of the guild whose data is stored in this object.
     * @return long - Server id.
     */
    public long getID() {
        return this.id;
    }

    /**
     * Access a particular setting value by name.
     * @param settingName
     * The "Key" for the desired setting value.
     * @return String - The current setting value.
     */
    public String getSetting(String settingName) {
        return settings.get(settingName);
    }

    /**
     * Determine if a discord user has a {@link LevelUser} object registered.
     * @param id
     * The discord ID of the user whose {@link LevelUser} value should be found
     * @return boolean - True if there is a stored LevelUser object, false otherwise.
     */
    public boolean hasLevelUser(long id) {
        return levels.containsKey(id);
    }

    /**
     * Get a {@link LevelUser} object by the id of the member whose data is required.
     * @param id
     * The discord ID of the {@link LevelUser} to be accessed.
     * @return {@link LevelUser}
     */
    public LevelUser getLevelUser(long id) {
        return levels.get(id);
    }

    /**
     * Access the HashMap of {@link LevelUser} objects directly. Changes made to this HashMap are NOT stored back to the server object.
     * @return {@code HashMap<Long, LevelUser>} where the key ({@code Long}) is the discord ID of the user and the value is the {@code LevelUser}
     * object.
     */
    public HashMap<Long, LevelUser> getLevels() {
        return levels;
    }

    /**
     * Change a given {@link LevelUser} object. This method will remove the old {@link LevelUser} and insert the new one.
     * @param id
     * The discord ID of the user whose {@link LevelUser} object will be changed.
     * @param user
     * The new {@link LevelUser} object.
     */
    public void setLevelUser(long id, LevelUser user) {
        if (levels.containsKey(id)) {
            levels.remove(id);
        }
        levels.put(id, user);
    }

    /**
     * Access all of the valid names of TextChannels stored in the {@code textChannels} map.
     * @return {@code Set<String>} an immutable Set of (String) textChannel names.
     * <br> <Strong>Valid Names:</Strong>
     * <ol>
     *     <li>{@code spam}</li>
     *     <li>{@code log}</li>
     *     <li>{@code report}</li>
     *     <li>{@code appeal}</li>
     *     <li>{@code apply}</li>
     * </ol>
     */
    public Set<String> getTextChannelNames() {
        return textChannels.keySet();
    }

    /**
     * Access the name of a text channel via its ID.
     * @param id
     * The Discord ID of the textChannel.
     * @return String - The registered name of the text channel in the server object. If this text channel is not registered
     * for any purpose, an empty string will be returned.
     */
    public String getTextChannelName(long id) {
        for (Map.Entry<String, Long> entry : textChannels.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return "";
    }

    /**
     * Remove the set text channel ID from a given name (unlink a text channel / No longer use for the previously specified purpose).
     * @param name - The name of the text channel to be unlinked.
     */
    public void clearTextChannel(String name) {
        textChannels.remove(name);
        textChannels.put(name, -1L);
    }

    /**
     * Determine if a text channel has been initialized/set by the server owner.
     * @param name
     * The name of the text channel to check.
     * @return boolean - True if the text channel has been initialized. Otherwise, false.
     */
    public boolean textChannelsInit(String name) {
        return !(textChannels.get(name) == -1L);
    }

    /**
     * Initialize a text channel with the given ID to the given name.
     * @param name
     * The name must match one of the names already presented in the HashMap.
     * @param id
     * The discord ID of the channel to be initialized to the given name.
     */
    public void initTextChannel(String name, Long id) {
        textChannels.remove(name);
        textChannels.put(name, id);
    }

    /**
     * Access the text channel id that has been assigned to a name.
     * @param name
     * The name of the text channel to get.
     * @return long - The discord ID of the text channel.
     */
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

    public Report getReport(UUID uuid) {
        if (incompleteReport.containsKey(uuid)) {
            return incompleteReport.get(uuid);
        }

        if (openReport.containsKey(uuid)) {
            return openReport.get(uuid);
        }

        if (archiveReport.containsKey(uuid)) {
            return archiveReport.get(uuid);
        }

        return null;
    }

    public void removeReport(UUID uuid) {
        if (incompleteReport.containsKey(uuid)) {
            incompleteReport.remove(uuid);
        } else if (openReport.containsKey(uuid)) {
            openReport.remove(uuid);
        } else if (archiveReport.containsKey(uuid)) {
            archiveReport.remove(uuid);
        }
    }

    public void addReport(Report report) {
        switch (report.getStatus()) {
            case INCOMPLETE:
                if (this.incompleteReport.containsKey(report.getUuid())) {
                    this.incompleteReport.remove(report.getUuid());
                }
                this.incompleteReport.put(report.getUuid(), report);
                break;
            case OPEN:
                if (this.openReport.containsKey(report.getUuid())) {
                    this.openReport.remove(report.getUuid());
                }
                this.openReport.put(report.getUuid(), report);
                break;
            case WORKING:
                if (this.openReport.containsKey(report.getUuid())) {
                    this.openReport.remove(report.getUuid());
                }
                this.openReport.put(report.getUuid(), report);
                break;
            case ARCHIVED:
                if (this.archiveReport.containsKey(report.getUuid())) {
                    return;
                }
                this.archiveReport.put(report.getUuid(), report);
                break;
        }
    }

    public Report[] getReports(ReportStatus status) {
        switch (status) {
            case ARCHIVED:
                Report[] array = new Report[this.archiveReport.size()];
                int idx = 0;
                for (Report r : this.archiveReport.values()) {
                    array[idx] = r;
                    idx++;
                }
                return array;
            case OPEN:
                Report[] arr = new Report[this.openReport.size()];
                int index = 0;
                for (Report r : this.openReport.values()) {
                    arr[index] = r;
                    index++;
                }
                return arr;
        }
        return new Report[0];
    }
}

