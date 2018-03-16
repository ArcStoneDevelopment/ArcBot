package BotFrame;

import BotFrame.Listeners.BotListener;
import BotFrame.Listeners.MessageListener;
import LoggerFrame.Logger;
import LoggerFrame.LoggerCore;
import LoggerFrame.LoggerException;
import LoggerFrame.LoggerPolicy;
import Utility.Servers;
import Utility.Settings;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.lang.invoke.MethodHandles;

public class Main {

    @Logger(LoggerPolicy.FILE)
    public static void main(String[] args) throws LoggerException {
        try {
            Settings.load();
            Servers.load();
            JDABuilder bot = new JDABuilder(AccountType.BOT);
                bot.setToken("NDIyOTM0MjM3MTE3Njc3NTc4.DYi_0Q.6VEP2jnklOYoBDV47wP0qwm2ddI"); //Left intentionally empty for security reasons.
                bot.setGame(Game.playing("ArcStone Development"));
                bot.setStatus(OnlineStatus.ONLINE);
                bot.setAutoReconnect(true);
                bot.setAudioEnabled(true);
            bot.addEventListener(new BotListener(),
                                 new MessageListener());
            bot.buildBlocking();
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true, -1L,
                    "Bot Load Complete.");
        } catch (Exception e) {
            LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), false, -1L,
                    "Exception Loading Bot.");
            System.exit(-1);
        }
    }
}
