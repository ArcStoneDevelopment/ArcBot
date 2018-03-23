package ResponseFrame

import Utility.SystemTime
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.awt.Color

internal object ResponseBuilder {
    fun build(response : Response) : MessageEmbed {
        when (response) {
            is MasterResponse -> return buildMaster(response)
            is ErrorResponse -> return buildError(response)
            is SuccessResponse -> return buildSuccess(response)
            is LevelResponse -> return buildLevel(response)
        }
        return EmbedBuilder().addBlankField(false).build()
    }

    private fun buildMaster(response: MasterResponse) : MessageEmbed {
        val builder = EmbedBuilder()
        builder.setColor(Color(255,0,255))
        var title = "N/A"
        var description = "None Available"
        when (response.responseNumber) {
            //Syntax Error
            0 -> {
                title = "Unexpected Syntax Error"
                description = "You entered a command improperly. If you need help, visit the setup page on the Github Wiki."
            }
            //Command Invoke Key Error
            1 -> {
                title = "Command Not Found"
                description = "There is no command with this invoke key."
            }
            //Disable Command Error
            2 -> {
                title = "Improper Action Error"
                description = "This command is already disabled!"
            }
            //Enable Command Error
            3 -> {
                title = "Improper Action Error"
                description = "This command is already enabled!"
            }
            //Command - Function Mapping Error
            4 -> {
                title = "Command - Function Mapping Error"
                description = "This command is tied to a function. Its status cannot be manually edited -- it will always match the status of the function."
            }
            //Function Name Error
            5 -> {
                title = "Function Not Found"
                description = "There is no function with this name."
            }
            //Disable Function Error
            6 -> {
                title = "Improper Action Error"
                description = "This function is already disabled!"
            }
            //Enable Function Error
            7 -> {
                title = "Improper Action Error"
                description = "This function is already enabled!"
            }
            //Role Mention Error
            8 -> {
                title = "Role Mention Error"
                description = "You must directly mention a valid role on the server to assign permission."
            }
            //Permission Name Error
            9 -> {
                title = "Permission Level Not Found"
                description = "There is no permission level with this name."
            }
            //Text Channel Name Error
            10 -> {
                title = "Text Channel Key Not Found"
                description = "There is not text channel with that name."
            }
            //Successful command status change
            11 -> {
                title = ":white_check_mark: Command Status Updated"
                description = "I've successfully changed the status of the desired command."
            }
            //Function status change successful
            12 -> {
                title = ":white_check_mark: Function Status Updated"
                description = "I've changed the status of the function. I've also updated all of that function's accompanying commands."
            }
            //Text channel init successful
            13 -> {
                title = ":white_check_mark: Text Channel Initialized"
                description = "I will now use this text channel."
            }
        }
        builder.setTitle(title)
        builder.setDescription("*$description*")
        return builder.build()
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
//          Function Disabled
            4 -> {
                error = "This Function Has Been Disabled."
                suggestion = "*I've been told by the owner of this discord server to not allow access to this function. If you think this is a mistake, please contact the discord server owner.*"
            }
//          Command Disabled
            5 -> {
                error = "This Command Has Been Disabled"
                suggestion = "*I've been told by the owner of this discord server to disable this command. If you think this is a mistake, please contact the discord server owner.*"
            }
//          Mojang Connection Error
            6 -> {
                error = "Mojang API Connection Error"
                suggestion = "*I've encountered an error syncing data from the Mojang API. Please try again later.*"
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

    private fun buildLevel(response : LevelResponse) : MessageEmbed {
        val builder = EmbedBuilder()
        builder.setColor(Color(230, 230, 250))
        var title = "N/A"
        var description = "None Available"
        when (response.responseNumber) {
//          Not Found Level User
            0 -> {
                title = "Level User Not Found."
                description = "*I couldn't find a user with this ID in my database.*"
            }
//          Level Up Message
            1 -> {
                title = ":tada: Congratulations!"
                description = "${response.args[0]} has moved from level ${response.args[1]} to ${response.args[2]}!"
            }
        }
        builder.setTitle(title)
        builder.setDescription(description)
        return builder.build()
    }
}

