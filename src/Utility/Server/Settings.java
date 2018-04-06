package Utility.Server;

import Utility.SettingsMaster;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Settings implements Serializable {
    private HashMap<String, String> core;

    Settings() {
        this.core = new HashMap<>(SettingsMaster.defaultGuildSettings);
    }

    public String getSetting(String settingName) {
        return this.core.get(settingName);
    }

    public Set<String> getSettings() {
        return this.core.keySet();
    }
}
