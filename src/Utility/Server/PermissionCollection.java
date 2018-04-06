package Utility.Server;

import Utility.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionCollection {
    private HashMap<Long, Permission> core;

    public PermissionCollection() {
        core = new HashMap<>();
    }

    public boolean hasPermission(Member member, Permission permission) {
        Permission highestPermission = Permission.DEFAULT;
        for (Role r : member.getRoles()) {
            if (Permission.comparator.compare(getPermission(r), highestPermission) > 0) {
                highestPermission = getPermission(r);
            }
        }
        return Permission.comparator.compare(highestPermission, permission) >= 0;
    }

    public boolean isPermission(String name) {
        ArrayList<String> permissions = new ArrayList<>() {{
            add("OWNER");
            add("STAFFTEAM");
            add("DONATOR");
            add("DEFAULT");
        }};
        return permissions.contains(name.toUpperCase());
    }

    public Permission getPermission (Role role) {
        return core.getOrDefault(role.getIdLong(), Permission.DEFAULT);
    }

    public void setPermission(Role role, Permission permission) {
        if (core.containsKey(role.getIdLong())) {
            core.remove(role.getIdLong());
        }
        core.put(role.getIdLong(), permission);
    }
}
