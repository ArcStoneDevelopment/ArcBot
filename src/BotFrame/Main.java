package BotFrame;

import BotFrame.Listeners.BotListener;
import BotFrame.Listeners.GuildListener;
import Utility.Servers;
import Utility.Settings;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Main {
    public static void main(String[] args) {
        try {
            Settings.load();
            Servers.load();
            JDABuilder bot = new JDABuilder(AccountType.BOT);
                bot.setToken("");
                bot.setGame(Game.playing("ArcStone Development"));
                bot.setStatus(OnlineStatus.ONLINE);
                bot.setAutoReconnect(true);
                bot.setAudioEnabled(true);
            bot.addEventListener(new BotListener(),
                                 new GuildListener());
            bot.buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
