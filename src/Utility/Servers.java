package Utility;

import Frame.LoggerFrame.Logger;
import Frame.LoggerFrame.LoggerCore;
import Frame.LoggerFrame.LoggerException;
import Frame.LoggerFrame.LoggerPolicy;
import Utility.Server.Server;
import Utility.SettingsMaster;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Servers implements Runnable {

    public Thread thread;

    public Servers() throws Exception {
        thread = new Thread(this, "Server Saver Thread.");
        load();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(60000);
                save();
            }
        } catch (Exception e) {
            return;
        }
    }

    public static HashMap<Long, Server> activeServers = new HashMap<>();

    public static void save() throws SQLException {
        for (Map.Entry<Long, Server> e : activeServers.entrySet()) {
            Server s = e.getValue();
            if (s.drop && inSQL(s.getID())) {
                delete(s.getID());
                continue;
            } else if (s.drop && !(inSQL(s.getID()))) {
                continue;
            }
            saveServer(s);
        }
    }

    public static void delete(long id) throws SQLException {
        PreparedStatement stmt = SettingsMaster.SQL_CONNECTION.prepareStatement("DELETE FROM servers WHERE id=?");
            stmt.setLong(1, id);
        stmt.execute();
        stmt.close();
    }

    private static boolean inSQL(long id) throws SQLException {
        PreparedStatement stmt = SettingsMaster.SQL_CONNECTION.prepareStatement("SELECT * FROM servers WHERE id=?");
            stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public static void saveServer(Server server) throws SQLException {
        PreparedStatement stmt = SettingsMaster.SQL_CONNECTION.prepareStatement(
                "SELECT * FROM servers WHERE id=?");
            stmt.setLong(1, server.getID());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            PreparedStatement statement = SettingsMaster.SQL_CONNECTION.prepareStatement(
                    "UPDATE servers set server=? WHERE id=?");
                statement.setObject(1, server);
                statement.setLong(2, server.getID());
            statement.execute();
            statement.close();
        } else {
            PreparedStatement statement = SettingsMaster.SQL_CONNECTION.prepareStatement(
                    "INSERT INTO servers (id, server) VALUES (?,?);");
                statement.setLong(1, server.getID());
                statement.setObject(2, server);
            statement.execute();
            statement.close();
        }
        stmt.close();
    }

    @Logger(LoggerPolicy.FILE)
    private static void load() throws SQLException, IOException, ClassNotFoundException, LoggerException {
        activeServers.clear();
        Statement stmt = SettingsMaster.SQL_CONNECTION.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM servers");
        while (rs.next()) {
            byte[] byteArray = rs.getBytes("server");
            ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            Server thisServer = (Server)objectIn.readObject();
            activeServers.put(thisServer.getID(), thisServer);
        }
        LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true,"Servers initialized from SQL.");
    }
}
