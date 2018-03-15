package Utility;

import java.util.Comparator;

public enum Permission {
    DEFAULT(0),
    DONATOR(1),
    STAFFTEAM(2),
    OWNER(3);

    int priority;
    Permission(int priority) {
        this.priority = priority;
    }

    public static Comparator<Permission> comparator = (Permission o1, Permission o2) -> {
        if (o1.priority == o2.priority) {
            return 0;
        }
        return o1.priority > o2.priority ? 1 : -1;
    };
}
