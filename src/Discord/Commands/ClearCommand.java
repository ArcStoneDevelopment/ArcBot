package Discord.Commands;

import Utility.Model.Command;
import Utility.Model.CommandBox;

public class ClearCommand implements Command {
    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public boolean execute(CommandBox command) {
        return false;
    }
}
