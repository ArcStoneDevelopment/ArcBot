package Utility.Model;

import Levels.LevelConstants;
import Utility.SystemTime;

import java.io.Serializable;
import java.util.TreeMap;

public class LevelUser implements Serializable {
    private int level;
    private int points;
    private long id;
    private TreeMap<Integer, String> levelHistory;

    public LevelUser(long id) {
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
        if (points > LevelConstants.getThreshold(previousLevel)) {
            level++;
            levelHistory.put(level, SystemTime.getTime());
        }
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
