package Utility.Server;

import Frame.FunctionFrame.FunctionCore;
import Utility.ServerException;

import java.io.Serializable;
import java.util.*;

/**
 * A wrapper class for {@link FunctionCore} HashMaps. This class sorts objects by their status values into three different
 * HashMaps internally. However, externally, the methods of this class behave as if all objects were stored in one large HashMap.
 * This class is based around the idea of UUIDs and generics so that it may function with any type of {@link FunctionCore} sub-class.
 * @param <T>
 *     The object that is being stored. This is required to be an implementation sub-class of {@link FunctionCore}.
 * @param <S>
 *     The status enum that is used to sort the objects of type {@code T}. In order for sorting to be effective, enum constants
 *     need to be declared in the following order: the first enum type needs to be the enum's version of {@code incomplete}.
 *     The second enum type needs to be a version of {@code archived}. Any other enum types will be interpreted as some
 *     version of {@code open}.
 *
 * @author ArcStone Development LLC
 * @version v2.0
 * @since v2.0
 */
public class FunctionObjectCollection<T extends FunctionCore, S extends Enum> implements Serializable {
    /**
     * HashMap of {@link FunctionCore} sub-class objects that are not completed. These objects are still being populated
     * through the respective private message system. Since the order of declaration for the status enum matters, these objects
     * will all have the first declared enum constant.
     */
    private HashMap<UUID, T> incomplete;

    /**
     * HashMap of {@link FunctionCore} sub-class objects that are completed, but have not yet been marked for archival.
     * These objects can have any status enum constant that is not the first two declared.
     */
    private HashMap<UUID, T> open;

    /**
     * HashMap of {@link FunctionCore} sub-class objects that are marked for archival. These objects have the second declared
     * status enum. In a future update, these objects will be translated to a read-only version.
     */
    private HashMap<UUID, T> archive;

    /**
     * Constructor initializes each of the three HashMaps. These are empty on creation.
     */
    FunctionObjectCollection () {
        incomplete = new HashMap<>();
        open = new HashMap<>();
        archive = new HashMap<>();
    }

    /**
     * Adds an object to the proper HashMap. This method switches on the {@code ordinal()} integer value of the status
     * enum of the given object. If the ordinal value is {@code 0} it is interpreted as incomplete and will be stored in
     * the {@code incomplete} HashMap. If the ordinal value is {@code 1} it is interpreted as archived and will be stored
     * in the {@code archived} HashMap. If the ordinal value is anything else, it is interpreted as open and will be stored
     * in the {@code open} HashMap. If the object's UUID is already present in a the target HashMap, the old object is removed
     * and the new one is added after.
     * @param obj
     * The object to be added into a HashMap.
     */
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

    /**
     * Finds the proper {@link FunctionCore} object by UUID. This method will search all three HashMaps to find the appropriate
     * object.
     * @param uuid
     * The UUID (universally unique identifier) of the {@link FunctionCore} sub-class object which is desired.
     * @return {@code T} - The {@link FunctionCore} sub-class object (whose UUID was the parameter) and whose type is declared in the generic class declaration.
     * @throws ServerException
     * Thrown when the given UUID is not one that corresponds with an object in any of the three HashMaps.
     */
    public T get(UUID uuid) throws ServerException {
        if (incomplete.containsKey(uuid)) {
            return incomplete.get(uuid);
        } else if (open.containsKey(uuid)) {
            return open.get(uuid);
        } else if (archive.containsKey(uuid)) {
            return archive.get(uuid);
        }
        throw new ServerException();
    }

    /**
     * Finds the proper {@link FunctionCore} object by UUID and deletes it. This method will search all three objects to find
     * the appropriate object, and it will then remove that object from the HashMap.
     * @param uuid
     * The UUID (universally unique identifier) of the {@link FunctionCore} sub-class object which will be deleted.
     */
    public void remove(UUID uuid) {
        if (incomplete.containsKey(uuid)) {
            incomplete.remove(uuid);
        } else if (open.containsKey(uuid)) {
            open.remove(uuid);
        } else if (archive.containsKey(uuid)) {
            archive.remove(uuid);
        }
    }

    /**
     * Access an unmodifiable list of {@link FunctionCore} objects by status type. Since this entire collection only has
     * three divisions, the only status values that should be used are the ones representing {@code incomplete}, {@code archived}
     * and {@code open}. Any other status values used will return the ENTIRE {@code open} HashMap.
     * @param status
     * The status enum which corresponds to a particular HashMap.
     * @return {@code List<T>} - an unmodifiable list of {@link FunctionCore} sub-class objects.
     */
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
