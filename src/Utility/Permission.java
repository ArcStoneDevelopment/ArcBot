package Utility;

import java.util.Comparator;

/**
 * Enum wrapper representation of integer permission levels. Each permission level here is associated with an integer value
 * to maintain a sense of a heirarchy between permission levels.
 *
 * @author ArcStone Devleopment LLC
 * @since v1.0
 * @version v1.5
 */
public enum Permission {
    /**
     * The default permission level which is the lowest integer value (0).
     */
    DEFAULT(0),

    /**
     * The donator permission level which is the second lowest integer value (1).
     */
    DONATOR(1),

    /**
     * The StaffTeam permission level which is the second highest integer value (2).
     */
    STAFFTEAM(2),

    /**
     * The Owner permission level which is the highest integer value (3).
     */
    OWNER(3),

    SERVER_OWNER(4);

    /**
     * The integer value associated with a given enum value.
     */
    int priority;

    /**
     * Constructor for enum values propagates the priority value.
     * @param priority
     * The integer priority for a given enum value.
     */
    Permission(int priority) {
        this.priority = priority;
    }

    /**
     * Comparator to check hierarchy. This comparator will determine which permission value is higher given two permission values.
     * This comparator is static so that it may be accessed directly via the enum class itself.
     */
    public static Comparator<Permission> comparator = (Permission o1, Permission o2) -> {
        if (o1.priority == o2.priority) {
            return 0;
        }
        return o1.priority > o2.priority ? 1 : -1;
    };
}
