package Utility.Server;

import Frame.FunctionFrame.FunctionCore;
import Utility.FunctionException;

import java.util.*;

public class FunctionObjectCollection<T extends FunctionCore, S extends Enum> {
    private HashMap<UUID, T> incomplete;
    private HashMap<UUID, T> open;
    private HashMap<UUID, T> archive;

    public FunctionObjectCollection () {
        incomplete = new HashMap<>();
        open = new HashMap<>();
        archive = new HashMap<>();
    }

    public void add(T obj) {
        switch (obj.getStatus().ordinal()) {
            case 0:
                if (incomplete.containsKey(obj.getUuid())) {
                    incomplete.remove(obj.getUuid());
                }
                incomplete.put(obj.getUuid(), obj);
                break;
            case 1:
                if (archive.containsKey(obj.getUuid())) {
                    archive.remove(obj.getUuid());
                }
                archive.put(obj.getUuid(), obj);
                break;
            default:
                if (open.containsKey(obj.getUuid())) {
                    open.remove(obj.getUuid());
                }
                open.put(obj.getUuid(), obj);
                break;
        }
    }

    public T get(UUID uuid) throws FunctionException {
        if (incomplete.containsKey(uuid)) {
            return incomplete.get(uuid);
        } else if (open.containsKey(uuid)) {
            return open.get(uuid);
        } else if (archive.containsKey(uuid)) {
            return archive.get(uuid);
        }
        throw new FunctionException();
    }

    public void remove(UUID uuid) {
        if (incomplete.containsKey(uuid)) {
            incomplete.remove(uuid);
        } else if (open.containsKey(uuid)) {
            open.remove(uuid);
        } else if (archive.containsKey(uuid)) {
            archive.remove(uuid);
        }
    }

    public List<T> getAll(S status) {
        switch (status.ordinal()) {
            case 0:
                return Collections.unmodifiableList(new ArrayList<>(this.incomplete.values()));
            case 1:
                return Collections.unmodifiableList(new ArrayList<>(this.archive.values()));
            default:
                return Collections.unmodifiableList(new ArrayList<>(this.open.values()));
        }
    }
}
