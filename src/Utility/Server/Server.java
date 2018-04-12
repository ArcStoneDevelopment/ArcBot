package Utility.Server;

import Appeal.AppealStatus;
import Appeal.Appeal;
import Discord.Discord;
import Frame.FunctionFrame.*;
import Levels.LevelUser;
import Report.Report;
import Report.ReportStatus;
import Utility.Command;
import Utility.Permission;
import net.dv8tion.jda.core.entities.Guild;

import java.io.Serializable;

/**
 * Defines how the bot should interact with a particular Discord server. This class contains settings, level data and
 * all function data for a particular server. These classes are expected to be saved to SQL on occasion to ensure no loss
 * of data in the event of an unexpected shut down.
 *
 * @author ArcStone Development LLC
 * @version v2.0
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
     * Collection of {@link Command} objects with the appropriate tools for status checking/changing.
     */
    private CommandCollection commands;

    /**
     * Collection of {@link Function} objects with the appropriate tools for manipulating/accessing pieces of those functions/
     */
    private FunctionCollection functions;

    /**
     * Collection storing all {@link LevelUser} data for this server.
     */
    private LevelCollection levels;

    /**
     * Collection mapping JDA Roles with {@link Permission} levels.
     */
    private PermissionCollection permissions;

    /**
     * Collection for accessing and manipulating server-specific settings.
     */
    private Settings settings;

    /**
     * Collection providing mappings for various keys with JDA TextChannel IDs.
     */
    private TCCollection textChannels;

    /**
     * Collection providing storage and manipulation for {@link Report} objects in various {@link ReportStatus} states.
     */
    private FunctionObjectCollection<Report, ReportStatus> reports;

    /**
     * Collection providing storage and manipulation for {@link Appeal} objects in various {@link AppealStatus} states.
     */
    private FunctionObjectCollection<Appeal, AppealStatus> appeals;

    /**
     * Primary constructor for creating {@code Server} objects for a given guild.
     * This constructor populates the {@code id} and {@code ownerID} variables based on the guild parameter. Once this
     * is done, the constructors for each collection object in this class are called.
     * @param guild
     * The guild whose information will be stored in this object.
     */
    public Server(Guild guild) {
        this.id = guild.getIdLong();
        this.ownerID = guild.getOwner().getUser().getIdLong();
        this.drop = false;

        commands = new CommandCollection();
        functions = new FunctionCollection();
        levels = new LevelCollection();
        permissions = new PermissionCollection(this.ownerID);
        settings = new Settings();
        textChannels = new TCCollection();
        reports = new FunctionObjectCollection<>();
        appeals = new FunctionObjectCollection<>();
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
        this.permissions.setOwnerID(ownerID);
    }

    /**
     * The discord ID of the guild whose data is stored in this object.
     * @return long - Server id.
     */
    public long getID() {
        return this.id;
    }

    /**
     * Access the collection of {@link Command}s for this server.
     * @return {@link CommandCollection} - The data storage/manipulation object for this server's {@link Command} data.
     */
    public CommandCollection getCommands() {
        return commands;
    }

    /**
     * Access the collection of {@link Function}s for this server.
     * @return {@link FunctionCollection} - The data storage/manipulation object for this server's {@link Function} data.
     */
    public FunctionCollection getFunctions() {
        return functions;
    }

    /**
     * Access the collection of {@link LevelUser}s for this server.
     * @return {@link LevelCollection} - The data storage/manipulation object for this server's {@link LevelUser} data.
     */
    public LevelCollection getLevels() {
        return levels;
    }

    /**
     * Access the map of {@link Permission} levels for this server.
     * @return {@link PermissionCollection} - The storage/manipulation object for this server's {@link Permission} data.
     */
    public PermissionCollection getPermissions() {
        return permissions;
    }

    /**
     * Access the collection of settings for this server.
     * @return {@link Settings} - The storage/manipulation object for this server's specific settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Access the TextChannel data for this server.
     * @return {@link TCCollection} - The storage/manipulation object for this server's active/inactive TextChannels.
     */
    public TCCollection getTextChannels() {
        return textChannels;
    }

    /**
     * Access the {@link Report} data for this server.
     * @return {@link FunctionObjectCollection} - The storage/manipulation object for this server's {@link Report} data.
     */
    public FunctionObjectCollection<Report, ReportStatus> getReports() {
        return reports;
    }

    /**
     * Access the {@link Appeal} data for this server.
     * @return {@link FunctionObjectCollection} - The storage/manipulation object for this server's {@link Appeal} data.
     */
    public FunctionObjectCollection<Appeal, AppealStatus> getAppeals() {
        return appeals;
    }
}

