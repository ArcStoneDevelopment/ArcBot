package LoggerFrame;

import Utility.SystemTime;
import net.dv8tion.jda.core.entities.Guild;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * The file logger controls logging to a file named {@code logger.txt} which is in the root directory of the java project's
 * runtime location. The file logger saves messages to the same file -- appending them at the bottom; however, when the java
 * program is terminated, the append functionality closes. In other words, if you restart the application, you have to rename
 * or move the {@code logger.txt} file if you plan on keeping it. When the project restarts, the {@code logger.txt} will
 * be overwritten with a new one.
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.5
 */
class FileLogger implements Loggable {

    /**
     * The file object of the {@code Logger.txt} text file
     */
    private File file;

    /**
     * This constructor creates the file in the computer's file system and saves the corresponding {@code File} object
     * to the {@code file} instance field. It then assembles a print writer to send a message to the file. This means that
     * every log message sent to the file will be "Appended".
     */
    public FileLogger() {
        try {
            this.file = new File("logger.txt");
            file.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println("Initialized File Logger.");
            writer.close();
        } catch (Exception e) { }
    }

    /**
     * This method invokes a print writer object on the {@code file} instance field. It then writes a line to the
     * {@code logger.txt} in the following form:
     * <br> Standard Form: {@code [MMM dd, yyyy] - [hh:mm:ss aa] <SERVER ID: <ID>> {<Status>} <Message>}
     * <br> Real Example: {@code {Mar 15, 2018} - [12:32:00 AM] <SERVER ID: -1> {+} Bot Loaded!}
     * @param success
     * If the method fully achieved the expected outcome/made the expected changes this should be true. Otherwise, false.
     * @param guild
     * The Guild object related to the {@link Utility.Server} object. This is required if the {@link LoggerPolicy} is
     * {@code Discord}. If the logger policy is not set to this, and/or the action carried out was not connected to a
     * particular server, this should be {@code null} (for standardization).
     * @param message
     * The String message to be logged. This should be short, reasonable and understandable. Furthermore, this should not
     * include a leading space.
     * @throws LoggerException
     * A logger exception is thrown in this method if there is an IOException related to the PrintWriter or file.
     */
    @Override
    public void log(boolean success, Guild guild, String message) throws LoggerException {
        long guildID;
        if (guild == null) {
            guildID = -1;
        } else {
            guildID = guild.getIdLong();
        }

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            if (success) {
                writer.println(SystemTime.getTime() + "<SERVER ID: " + guildID + "> {+} " + message);
            } else {
                writer.println(SystemTime.getTime() + "<SERVER ID: " + guildID + "> {-} " + message);
            }
            writer.close();
        } catch (Exception e) {
            throw new LoggerException("There was an error writing the log message to the file!");
        }
    }
}
