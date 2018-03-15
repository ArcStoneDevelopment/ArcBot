package Discord.Commands;

import Utility.*;

public class ClearCommand implements Command {

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public boolean execute(CommandBox command) {
        try {
            Server server = Servers.activeServers.get(command.getEvent().getGuild().getIdLong());
            if (command.getArgs().length == 1) {
                switch (command.getArgs()[0]) {
                    case "all":
                    default:
                        clearNum(command, server);
                        return true;
                }
            } else {
                throw new SyntaxException(0);
            }
        } catch (PermissionException e) {

        } catch (SyntaxException e) {

        }
        return false;
    }

    private void clearNum(CommandBox command, Server server) throws PermissionException, SyntaxException {
        if (server.hasPermission(command.getEvent().getMember(), Permission.STAFFTEAM)) {

        } else {
            throw new PermissionException();
        }
    }
}
