package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Settings {
    private static boolean isLoaded = false;
    public static HashMap<String, String> defaultGuildSettings = new HashMap<>();

    public static Connection SQL_CONNECTION;

    public static void load() throws SQLException {
        if (isLoaded) {
            return;
        }

        SQL_CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false",
                "root", "root");
        
    }
}
