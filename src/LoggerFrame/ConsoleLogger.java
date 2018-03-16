package LoggerFrame;

import Utility.SystemTime;
import net.dv8tion.jda.core.entities.Guild;

class ConsoleLogger implements Loggable {
    @Override
    public void log(boolean success, Guild guild, String message) {
        if (success) {
            System.out.println(SystemTime.getTime() + "[[SERVER ID: " + guild.getIdLong() + "]] {+} " + message);
        } else {
            System.out.println(SystemTime.getTime() + "[[SERVER ID: " + guild.getIdLong() + "]] {-} " + message);
        }
    }
}
