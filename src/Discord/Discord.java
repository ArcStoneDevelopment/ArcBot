package Discord;

import Discord.Commands.LevelCommand;
import Discord.Commands.ServerEditorCommand;
import Discord.Commands.StopCommand;
import Utility.Model.Command;

import java.util.ArrayList;

public class Discord {
    public static ArrayList<Command> commands = new ArrayList<>() {{
        add(new StopCommand());
        add(new ServerEditorCommand());
        add(new LevelCommand());
    }};
}
