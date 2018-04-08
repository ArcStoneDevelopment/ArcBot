package Frame.BotFrame;

import Frame.BotFrame.Listeners.BotListener;
import Frame.BotFrame.Listeners.MessageListener;
import Frame.LoggerFrame.Logger;
import Frame.LoggerFrame.LoggerCore;
import Frame.LoggerFrame.LoggerException;
import Frame.LoggerFrame.LoggerPolicy;
import Utility.Servers;
import Utility.SettingsMaster;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

/**
 * Provides a staring point for the bot. (Main Method).
 *
 * @author ArcStone Development LLC
 * @version v1.5
 * @since v1.0
 */
public class Main {

    /**
     * This class should not be instantiated for any reason. The constructor here is declared private to prevent this operation.
     */
    private Main() {}

    /**
     * This is the starting point for the entire discord bot.
     * <br> <strong>Order of Operations Here:</strong>
     * <ol>
     *     <li>{@link SettingsMaster}{@code .load()}</li>
     *     <li>{@link Servers}{@code .load()}</li>
     *     <li>Build the {@code JDABuilder instance}</li>
     *     <li>Start the JDA instance</li>
     * </ol>
     * <br> Anything actions in this method are logged to the {@code FILE} {@link LoggerPolicy}
     * @param args
     * Arguments given from command line java. This does nothing at the moment.
     * @throws LoggerException
     * If there's an issue with any of the logging mechanisms, this exception should stop the program. To prevent
     * processes from going un-logged.
     */
    @Logger(LoggerPolicy.FILE)
    public static void main(String[] args) throws LoggerException {
        try {
            SettingsMaster.load();
            Servers serverHandler = new Servers();
                serverHandler.thread.start();
            JDABuilder bot = new JDABuilder(AccountType.BOT);
                bot.setToken("NDIyOTM0MjM3MTE3Njc3NTc4.DY1ldg.nYTPlNyCQbQ-jYvs_fhP3QRsK4I"); //Left intentionally empty for security reasons.
                bot.setGame(Game.playing("ArcStone Development"));
                bot.setStatus(OnlineStatus.ONLINE);
                bot.setAutoReconnect(true);
                bot.setAudioEnabled(true);
            bot.addEventListener(new BotListener(),
                                 new MessageListener());
            bot.buildBlocking();
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true,
                    "Bot Load Complete.");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), false,
                    "Exception Loading Bot.");
            System.exit(-1);
        }
    }
}
