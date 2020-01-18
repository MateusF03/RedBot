package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemeGenerator {
    @Command(name = "meme", description = "Cria um meme baseado no input do usúario", commandCategory = CommandCategory.FUN, args = "(id do meme) (texto 1-texto 2)", commandPermission = UserPermission.BASE)
    public static void meme(GuildMessageReceivedEvent event, String[] args) {
        String idMeme = args[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i<args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        String[] texts = stringBuilder.toString().split("\\-");
        if (texts.length < 2)  {
            event.getChannel().sendMessage("Você precisa colocar dois textos!").queue();
        }
        String topText = texts[0];
        topText = topText.replaceAll("\\s+", "%20");
        String bottomText = texts[1];
        bottomText = bottomText.replaceAll("\\s+", "%20");
        String username = (String) ConfigManager.getInstance().getConfig().get("imgflip-username");
        String password = (String) ConfigManager.getInstance().getConfig().get("imgflip-password");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://api.imgflip.com/caption_image?template_id=" + idMeme + "&text0=" + topText + "&text1=" + bottomText
                    + "&username=" + username + "&password=" + password).openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
            urlConnection.addRequestProperty("http.keepAlive", "false");
            urlConnection.connect();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(urlConnection.getInputStream()));
            JSONObject data = (JSONObject) jsonObject.get("data");
            String memeUrl = (String) data.get("url");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setAuthor("Meme gerado pela API do ImgFlip");
            embedBuilder.setFooter("Comando pedido por: " + event.getAuthor().getAsTag());
            embedBuilder.setImage(memeUrl);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
