package Discord;

import Discord.Commands.*;
import Utility.Command;

import java.util.ArrayList;

public class Discord {
    public static ArrayList<Command> commands = new ArrayList<>() {{
        add(new StopCommand());
        add(new ServerEditorCommand());
        add(new LevelCommand());
        add (new MinecraftCommand());
        add(new ClearCommand());
    }};
}
