package Utility.Server;

import Frame.FunctionFrame.Function;
import Utility.FunctionException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class FunctionCollection implements Serializable {

    private TreeMap<String, Function> core;

    FunctionCollection() {
        core = new TreeMap<>() {{
            put("discord", new Function("discord", true, "N/A"));
            put("level", new Function("level", true, "N/A"));
            put("report", new Function("report", true, "N/A"));
            put ("appeal", new Function("appeal", true, "N/A"));
        }};
    }

    public Set<Function> getFunctions() {
        return new HashSet<>(core.values());
    }

    public boolean isFunction(String name) {
        return core.keySet().contains(name);
    }

    public Function getFunction(String name) throws FunctionException {
        if (core.containsKey(name)) {
            return core.get(name);
        }
        throw new FunctionException();
    }
}
