package Frame.BotFrame;

import Frame.BotFrame.Listeners.BotListener;
import Frame.BotFrame.Listeners.MessageListener;
import Frame.LoggerFrame.Logger;
import Frame.LoggerFrame.LoggerCore;
import Frame.LoggerFrame.LoggerException;
import Frame.LoggerFrame.LoggerPolicy;
import Utility.Servers;
import Utility.Settings;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Main {

    @Logger(LoggerPolicy.FILE)
    public static void main(String[] args) throws LoggerException {
        try {
            Settings.load();
            Servers.load();
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
