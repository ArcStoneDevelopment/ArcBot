package Discord.Commands;

import Frame.BotFrame.CommandBox;
import Utility.*;
import Utility.Server.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;

import java.awt.*;

public class HelpCommand implements Command {

    private CommandInfo info;

    public HelpCommand() {
        info = new CommandInfo("help");
        info.addCommand("[]", "-help", "This is the help!", Permission.DEFAULT);
        info.addCommand("[command invoke]", "-help [command name]", "Get the information for a specific command.", Permission.DEFAULT);
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
        try {
            switch (command.getArgs().length) {
                case 0:
                    generalHelp(command);
                    return true;
                case 1:
                    commandHelp(command);
                    return true;
                default:
                    throw new SyntaxException(0);
            }
        } catch (PermissionException e) {

        } catch (SyntaxException e) {

        } catch (ServerException e) {

        }
        return false;
    }

    private static void generalHelp(CommandBox command) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(52, 255, 255));
        eb.setTitle(":clipboard: __**Command Help**__");
        for (Command c :command.getServer().getCommands().getCommands()) {
            eb.addField("__**" + c.getInfo().getInvoke().substring(0,1).toUpperCase() + c.getInfo().getInvoke().substring(1) + "**__",
                    "", false);
            for (String s : c.getInfo().keys()) {
                if (command.getServer().getPermissions().hasPermission(command.getEvent().getMember(), c.getInfo().getPermission(s))) {
                    eb.addField(" - " + c.getInfo().getUsage(s), c.getInfo().getDescription(s), false);
                }
            }
            eb.addBlankField(false);
        }
        PrivateChannel pc = command.getEvent().getAuthor().openPrivateChannel().complete();
        pc.sendMessage(eb.build()).queue();
        pc.close().complete();
    }

    private static void commandHelp(CommandBox command) throws ServerException, PermissionException {

    }
}
