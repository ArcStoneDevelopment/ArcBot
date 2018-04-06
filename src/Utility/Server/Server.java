package Utility.Server;

import Appeal.AppealStatus;
import Appeal.Appeal;
import Discord.Discord;
import Frame.FunctionFrame.*;
import Levels.LevelUser;
import Report.Report;
import Report.ReportStatus;
import Utility.Command;
import Utility.FunctionException;
import Utility.Permission;
import Utility.SettingsMaster;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.*;

import static Appeal.AppealStatus.*;

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

    private CommandCollection commands;
    private FunctionCollection functions;
    private LevelCollection levels;
    private PermissionCollection permissions;
    private Settings settings;
    private TCCollection textChannels;
    private FunctionObjectCollection<Report, ReportStatus> reports;
    private FunctionObjectCollection<Appeal, AppealStatus> appeals;

    public Server(Guild guild) {
        System.out.println("Registered new server.");
        this.id = guild.getIdLong();
        this.ownerID = guild.getOwner().getUser().getIdLong();
        this.drop = false;

        commands = new CommandCollection();
        functions = new FunctionCollection();
        levels = new LevelCollection();
        permissions = new PermissionCollection();
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
    }

    /**
     * The discord ID of the guild whose data is stored in this object.
     * @return long - Server id.
     */
    public long getID() {
        return this.id;
    }

    public CommandCollection getCommands() {
        return commands;
    }

    public FunctionCollection getFunctions() {
        return functions;
    }

    public LevelCollection getLevels() {
        return levels;
    }

    public PermissionCollection getPermissions() {
        return permissions;
    }

    public Settings getSettings() {
        return settings;
    }

    public TCCollection getTextChannels() {
        return textChannels;
    }

    public FunctionObjectCollection<Report, ReportStatus> getReports() {
        return reports;
    }

    public FunctionObjectCollection<Appeal, AppealStatus> getAppeals() {
        return appeals;
    }
}

