package Discord.Commands;

import Frame.FunctionFrame.Handler;
import Frame.ResponseFrame.ErrorResponse;
import Frame.ResponseFrame.ResponseBuilder;
import Report.ReportHandler;
import Utility.Command;
import Utility.CommandBox;

public class ReportCommand implements Command {
    @Override
    public String getInvoke() {
        return "report";
    }

    @Override
    public boolean execute(CommandBox command) {
        if (command.getArgs().length == 0) {
            if (!(Handler.openFunctions.containsKey(command.getEvent().getAuthor().getIdLong()))) {
                ReportHandler.start(command.getEvent());
                return true;
            } else {
                command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new ErrorResponse(7))).queue();
                return false;
            }
        }
        return false;
    }
}
