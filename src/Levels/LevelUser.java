package Levels;

import Levels.Level;
import Utility.SystemTime;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Extension class for each JDA Member to handle level data. Each user that has sent a message in each server
 * receives their own {@code LevelUser} object. If the same user is on multiple servers, they will have a {@code LevelUser} object
 * registered in each server to handle levels independently.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public class LevelUser implements Serializable {

    /**
     * Boolean to determine whether or not the user has been marked blocked from the level system. If this is true, the
     * point/level values of this user will not change.
     */
    private boolean disabled;

    /**
     * The current level of the user. Starts at 0.
     */
    private int level;

    /**
     * The number of points that a user has.
     */
    private int points;

    /**
     * The discord long ID for the member whose level data is stored in this object.
     */
    private long id;

    /**
     * A history of all level ups that have occurred with this user. This includes level value paired with formatted time
     * String.
     */
    private TreeMap<Integer, String> levelHistory;

    /**
     * The UUID for this particular {@code leveluser} object. This is important to differentiate between two {@code leveluser} objects
     * that both represent the same discord user on two different servers.
     */
    private UUID uuid;

    /**
     * Constructor for {@code LevelUser} objects.
     * Constructor generates a new UUID, initiates point and level values to zero, starts the {@code levelhistory} with the current
     * time (and level value of 0).
     * @param id
     * The discord long ID of the member who is now being tracked in this level object.
     */
    public LevelUser(long id) {
        this.uuid = UUID.randomUUID();
        level = 0;
        points = 0;
        this.id = id;
        levelHistory = new TreeMap<>() {{
            put(0, SystemTime.getTime());
        }};
        disabled = false;
    }

    public void reset() {
        this.level = 0;
        this.points = 0;
        levelHistory.clear();
        levelHistory.put(0, SystemTime.getTime());
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Change the point count. This method adds the given number of points and recalculates the level based on the new
     * point value. If the level has changed, the {@code levelhistory} TreeMap is updated.
     * @param pointCount
     * The number of points to be added to the user.
     */
    public void update(int pointCount) {
        points += pointCount;
        int previousLevel = this.level;
        if (points > Level.getThreshold(previousLevel)) {
            level++;
            levelHistory.put(level, SystemTime.getTime());
        }
    }

    /**
     * Access the UUID for this {@code LevelUser}.
     * @return UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Access the current level for this {@code LevelUser}.
     * @return int - Current level value
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Access the current point value for this {@code LevelUser}.
     * @return int - Current point value
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Access the {@code levelhistory} TreeMap for this {@code LevelUser}. It is important to note here that any changes
     * made to this TreeMap are NOT saved back to the {@code LevelUser}. Adding values to this map is handled internally.
     * @return {@code TreeMap<Integer, String>} - The levelHistory for this user. Keys are the level value, Values are the
     * String representation of the time at which a level was first obtained.
     */
    public TreeMap<Integer, String> getLevelHistory() {
        return levelHistory;
    }

    /**
     * Access the discord ID for the member whose data is stored in this {@code LevelUser}.
     * @return long - Discord ID
     */
    public long getId() {
        return id;
    }
}
