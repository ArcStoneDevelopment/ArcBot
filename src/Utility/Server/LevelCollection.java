package Utility.Server;

import Levels.LevelUser;
import Utility.ServerException;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LevelCollection implements Serializable {

    private HashMap<Long, LevelUser> core;

    LevelCollection() {
        this.core = new HashMap<>();
    }

    public boolean hasLevelUser(long id) {
        return this.core.containsKey(id);
    }

    public LevelUser getLevelUser(long id) throws ServerException {
        if (this.core.containsKey(id)) {
            return this.core.get(id);
        }
        throw new ServerException();
    }

    public void setLevelUser(long id, LevelUser user) {
        if (this.core.containsKey(id)) {
            this.core.remove(id);
        }
        this.core.put(id, user);
    }

    public TreeMap<Integer, LevelUser> getLevels() {
        TreeMap<Integer, LevelUser> orderedLevels = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Long, LevelUser> entry : this.core.entrySet()) {
            orderedLevels.put(entry.getValue().getPoints(), entry.getValue());
        }
        return orderedLevels;
    }
}
