package Utility;

import LoggerFrame.Logger;
import LoggerFrame.LoggerCore;
import LoggerFrame.LoggerException;
import LoggerFrame.LoggerPolicy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Settings {
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

        SQL_CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost:3306/ArcBot?useSSL=false",
                "root", "root");
        isLoaded = true;
        LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true,
                "Settings Core Initialized.");
    }
}
