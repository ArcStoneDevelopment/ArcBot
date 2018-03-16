package LoggerFrame;

import Utility.SystemTime;

class ConsoleLogger implements Loggable {
    @Override
    public void log(boolean success, long serverID, String message) {
        if (success) {
            System.out.println(SystemTime.getTime() + "[[SERVER ID: " + serverID+ "]] {+} " + message);
        } else {
            System.out.println(SystemTime.getTime() + "[[SERVER ID: " + serverID + "]] {-} " + message);
        }
    }
}
