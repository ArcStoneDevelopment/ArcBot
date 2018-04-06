package Utility.Server;

import Utility.FunctionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TCCollection {
    private HashMap<String, Long> core;

    TCCollection() {
        core = new HashMap<>() {{
            put("spam", -1L);
            put("log", -1L);
            put("report", -1L);
            put("appeal", -1L);
            put("apply", -1L);
        }};
    }

    public Set<String> getNames() {
        return core.keySet();
    }

    public String getName(long id) {
        for (Map.Entry<String, Long> entry : core.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return "";
    }

    public void clear(String name) {
        core.remove(name);
        core.put(name, -1L);
    }

    public boolean isActive(String name) {
        return !(core.get(name) == -1L);
    }

    public void initialize(String name, Long id) throws FunctionException {
        if (core.containsKey(name)) {
            core.remove(name);
            core.put(name, id);
        }
        throw new FunctionException();
    }

    public long getID(String name) {
        return core.get(name);
    }

    public Set<Long> getChannels() {
        return new HashSet<>(core.values());
    }
}
