package Discord.Commands;

import ResponseFrame.ErrorResponse;
import ResponseFrame.ResponseBuilder;
import Utility.Command;
import Utility.CommandBox;
import Utility.SyntaxException;
import net.dv8tion.jda.core.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class MinecraftCommand implements Command {

    private static final String charset = StandardCharsets.UTF_8.name();
    private static final String contentType = "application/json";

    @Override
    public String getInvoke() {
        return "minecraft";
    }
//    TODO: Add some form of user name check for invalid user names.
    @Override
    public boolean execute(CommandBox command) {
        try {
            if (command.getArgs().length >= 1) {
                switch (command.getArgs()[0].toLowerCase()) {
                    case "uuid":
                        uuid(command);
                        return true;
                    case "head":
                        head(command);
                        return true;
                    case "skin":
                        skin(command);
                        return true;
                }
            } else if (command.getArgs().length == 0) {
                mojangStatus(command);
                return true;
            }else {
                throw new SyntaxException(0);
            }
        } catch (SyntaxException e) {
            command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new ErrorResponse(2))).queue();
        } catch (IOException e) {
            command.getEvent().getChannel().sendMessage(ResponseBuilder.INSTANCE.build(new ErrorResponse(6))).queue();
        }
        return false;
    }

    private void uuid(CommandBox command) throws IOException, SyntaxException {
        if (command.getArgs().length == 2) {
            EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(153, 0, 76));
                eb.setTitle("UUID of " + command.getArgs()[1]);
                eb.setDescription("UUID: " + getUUID(command.getArgs()[1]));
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new SyntaxException(0);
        }
    }

    private void head(CommandBox command) throws SyntaxException, IOException {
        if (command.getArgs().length == 2) {
            String url = "https://mc-heads.net/head/" + getUUID(command.getArgs()[1]);
            EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(153, 0, 76));
                eb.setTitle("Head of " + command.getArgs()[1]);
                eb.setImage(url);
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new SyntaxException(0);
        }
    }

    private void skin(CommandBox command) throws SyntaxException, IOException {
        if (command.getArgs().length == 2) {
            String url = "https://mc-heads.net/body/" + getUUID(command.getArgs()[1]);
            EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(153, 0, 76));
                eb.setTitle("Skin of " + command.getArgs()[1]);
                eb.setImage(url);
            command.getEvent().getChannel().sendMessage(eb.build()).queue();
        } else {
            throw new SyntaxException(0);
        }
    }

    private String getUUID(String minecraftName) throws IOException {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + minecraftName;
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", contentType);
        InputStream response = connection.getInputStream();
        Scanner scanner = new Scanner(response);
        String responseText = scanner.useDelimiter("\\A").next();
        scanner.close();
        JSONObject responseJSON = new JSONObject(responseText);
        return responseJSON.getString("id");
    }

    private void mojangStatus(CommandBox command) throws IOException {
        String url = "https://status.mojang.com/check";
        URLConnection conn = new URL(url).openConnection();
        InputStream response = conn.getInputStream();

        Scanner scanner = new Scanner(response);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();
        ArrayList<String> responses = new ArrayList<>();
        JSONArray responseArray = new JSONArray(responseBody);

        for (int x = 0; x < responseArray.length(); x++) {
            JSONObject json = responseArray.getJSONObject(x);
            String[] names = JSONObject.getNames(json);
            String responseValue = json.getString(names[0]);
            String statusOut = ":x:";
            if (responseValue.equalsIgnoreCase("green")) {
                statusOut = ":white_check_mark:";
            } else if (responseValue.equalsIgnoreCase("yellow")) {
                statusOut = ":warning:";
            }
            responses.add(statusOut);
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(153, 0, 76));
        eb.setTitle("__**Minecraft Service Status**__");
        eb.setDescription(responses.get(0) + "  **Minecraft.net** \n\n" +
                          responses.get(6) + "  **Textures** \n\n" +
                          responses.get(4) + "  **Session Servers** \n\n" +
                          responses.get(2) + "  **Accounts** \n\n" +
                          responses.get(3) + "  **Authentication Servers** \n\n" +
                          responses.get(7) + "  **Mojang.com** \n\n" +
                          responses.get(5) + "  **Mojang API**");
        command.getEvent().getChannel().sendMessage(eb.build()).queue();
    }
}
