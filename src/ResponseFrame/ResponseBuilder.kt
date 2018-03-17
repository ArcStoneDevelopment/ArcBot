package ResponseFrame

import Utility.SystemTime
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.awt.Color

internal object ResponseBuilder {
    fun build(response : Response) : MessageEmbed {
        when (response) {
            is ErrorResponse -> return buildError(response)
        }
        return EmbedBuilder().addBlankField(false).build()
    }

    private fun buildError(response : ErrorResponse) : MessageEmbed {
        val builder : EmbedBuilder = EmbedBuilder()
        builder.setColor(Color(221,51,69))
        builder.setFooter(SystemTime.getTime(), "https://imgur.com/a/yvWkm")
        var title = ""
        var suggestion = ""
        when(response.responseNumber){
//          No Command Error
            0 -> {
                title = "Command Not Found."
                suggestion = "*I could not find a command with that name. See* `-help` *for more information.*"
            }
//          No Permission Error
            1 -> {
                title = "Insufficient Permission."
                suggestion = "*You do not have permission to execute this command. If you think this is an issue, contact the server owner.*"
            }
//          Incorrect Syntax Error
            2 -> {
                title = "Unexpected Command Syntax."
                suggestion = "*The command syntax submitted was incorrect! Check* `-help` *for proper usage.*"
            }
        }
        builder.setTitle(":x: ERROR: $title")
        builder.setDescription(suggestion)

        return builder.build()

    }
}

