package Utility.Server;

import Utility.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PermissionCollection implements Serializable {
    private HashMap<Long, Permission> core;
    private long ownerID;

     PermissionCollection(long ownerID) {
        core = new HashMap<>();
        this.ownerID = ownerID;
    }

    public void setOwnerID(long ownerID) {
         this.ownerID = ownerID;
    }

    public boolean hasPermission(Member member, Permission permission) {
         if (permission.equals(Permission.SERVER_OWNER)) {
                return member.getUser().getIdLong() == this.ownerID;
         }

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
