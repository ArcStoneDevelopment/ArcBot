package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Utility.Command;
import Utility.CommandInfo;
import Utility.Permission;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.Color;
import java.util.Random;

public class EightBallCommand implements Command {

    private CommandInfo info;

    public EightBallCommand() {
        info = new CommandInfo("8ball");
        info.addCommand("[question]", "-8ball [Question]", "Ask the bot a question!", Permission.DEFAULT);
    }

    @Override
    public CommandInfo getInfo() {
        return info;
    }

    @Override
    public String getInvoke() {
        return info.getInvoke();
    }

    @Override
    public boolean execute(CommandBox command) {
        String[] answers = {"Absolutely!", "Yes, for sure.", "Maybe...", "Nah...", "No, really, no."};
        int answer = new Random().nextInt(5);
        if (command.getArgs().length >= 1) {
            StringBuilder original = new StringBuilder();
            for (String s : command.getArgs()) {
                original.append(s);
                original.append(" ");
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(52, 255, 255));
            eb.addField(original.toString(), ":small_blue_diamond: " + answers[answer], false);
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
            return true;
        } else {
            command.getEvent().getChannel().sendMessage(Frame.ResponseFrame.ResponseBuilder.INSTANCE.build(
                    new Frame.ResponseFrame.ErrorResponse(8))).queue();
            return false;
        }
    }
}
