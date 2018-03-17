package ResponseFrame

import Utility.SystemTime
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.awt.Color

class ResponseBuilder {
    fun build(response : Response) : MessageEmbed? {
        when (response) {
            is ErrorResponse -> return buildError(response)
        }
        return null
    }

    private fun buildError(response : ErrorResponse) : MessageEmbed {
        val builder : EmbedBuilder = EmbedBuilder()
        builder.setColor(Color(221,51,69))
        builder.setFooter(SystemTime.getTime(), "https://imgur.com/a/yvWkm")
        when(response.responseNumber){
            0 -> {
                builder.setTitle(":x: ERROR: No Command Found")
                builder.setDescription("*I could not find a command with that name. See* `-help` *for more information.*")
            }
        }

        return builder.build()

    }
}

