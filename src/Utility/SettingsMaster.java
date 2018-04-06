package Utility;

import Frame.LoggerFrame.Logger;
import Frame.LoggerFrame.LoggerCore;
import Frame.LoggerFrame.LoggerException;
import Frame.LoggerFrame.LoggerPolicy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class SettingsMaster {
    private static boolean isLoaded = false;
    public static HashMap<String, String> defaultGuildSettings = new HashMap<>() {{
        put ("prefix", "-");
    }};

    public static Connection SQL_CONNECTION;

    @Logger(LoggerPolicy.FILE)
    public static void load() throws SQLException, LoggerException {
        if (isLoaded) {
            return;
        }

        SQL_CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost:3306/ArcBot?useSSL=false&serverTimezone=UTC",
                "root", "root");
        isLoaded = true;
        LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true,
                "SettingsMaster Core Initialized.");
    }
}
