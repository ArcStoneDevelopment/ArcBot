package Discord;

import Discord.Commands.*;
import Utility.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * The head class for the Discord Function. This class supplies a master immutable list of all of the Commands that are
 * in this function.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 */
public class Discord {

    /**
     * The only constructor for this class is declared private to any object creation for this class.
     */
    private Discord() {}
    /**
     * This list contains all of the valid {@link Command} objects.
     */
    public static List<Command> commands = new ArrayList<>() {{
        add(new StopCommand());
        add(new ServerEditorCommand());
        add(new LevelCommand());
        add (new MinecraftCommand());
        add(new ClearCommand());
        add(new ReportCommand());
    }};
}
