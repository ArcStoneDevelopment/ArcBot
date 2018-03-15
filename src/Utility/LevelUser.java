package Utility;

import Levels.Level;
import Utility.SystemTime;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.UUID;

public class LevelUser implements Serializable {
    private int level;
    private int points;
    private long id;
    private TreeMap<Integer, String> levelHistory;
    private UUID uuid;

    public LevelUser(long id) {
        this.uuid = UUID.randomUUID();
        level = 0;
        points = 0;
        this.id = id;
        levelHistory = new TreeMap<>() {{
            put(0, SystemTime.getTime());
        }};
    }

    public void update(int pointCount) {
        points += pointCount;
        int previousLevel = this.level;
        if (points > Level.getThreshold(previousLevel)) {
            level++;
            levelHistory.put(level, SystemTime.getTime());
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLevel() {
        return this.level;
    }

    public int getPoints() {
        return this.points;
    }

    public TreeMap<Integer, String> getLevelHistory() {
        return levelHistory;
    }

    public long getId() {
        return id;
    }
}
