package ResponseFrame

import Utility.SystemTime
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.awt.Color

internal object ResponseBuilder {
    fun build(response : Response) : MessageEmbed {
        when (response) {
            is ErrorResponse -> return buildError(response)
            is SuccessResponse -> return buildSuccess(response)
        }
        return EmbedBuilder().addBlankField(false).build()
    }

    private fun buildError(response : ErrorResponse) : MessageEmbed {
        val builder = EmbedBuilder()
        builder.setColor(Color(221,51,69))
        builder.setFooter(SystemTime.getTime(), "https://imgur.com/a/yvWkm")
        var error = "N/A"
        var suggestion = "*None available.*"
        when(response.responseNumber){
//          No Command Error (No Args)
            0 -> {
                error = "Command Not Found."
                suggestion = "*I could not find a command with that name. See* `-help` *for more information.*"
            }
//          No Permission Error (No Args)
            1 -> {
                error = "Insufficient Permission."
                suggestion = "*You do not have permission to execute this command. If you think this is an issue, contact the server owner.*"
            }
//          Incorrect Syntax Error (No Args)
            2 -> {
                error = "Unexpected Command Syntax."
                suggestion = "*The command syntax submitted was incorrect! Check* `-help` *for proper usage.*"
            }
//          Incorrect Syntax Error (Arguments)
            3 -> {
                error = "Unexpected Command Syntax."
                suggestion = response.args[0]
            }
        }
        builder.setTitle(":x: ERROR: $error")
        builder.setDescription(suggestion)

        return builder.build()

    }

    private fun buildSuccess(response : SuccessResponse) : MessageEmbed {
        val builder = EmbedBuilder()
        builder.setColor(Color(120,177,78))
        builder.setFooter(SystemTime.getTime(), "https://imgur.com/a/yvWkm")
        var title = "N/A"
        var change = "*None Available*"
        when (response.responseNumber) {
//          Cleared Message Success
            0 -> {
                title = "Messages Cleared."
                change = "*I've cleared the messages from the chat.*"
            }
//          Bot Shutdown Success
            1 -> {
                title = "Bot Shutting Down."
                change = "*I have saved, and will now turn off.*"
            }
        }
        builder.setTitle(":white_check_mark: SUCCESS: $title")
        builder.setDescription(change)
        return builder.build()
    }
}

