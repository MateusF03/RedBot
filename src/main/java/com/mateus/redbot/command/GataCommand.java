package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GataCommand {
    private static BufferedImage template;
    static {
        try {
            template = ImageIO.read(new File(BotUtils.getImagesFolder(), "gata_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Command(name = "gata", description = "é isso mesmo que tu ouviu", commandCategory = CommandCategory.FUN, args = "[link da imagem]", commandPermission = UserPermission.BASE)
    public static void gata(GuildMessageReceivedEvent event, String[] args) throws ExecutionException, InterruptedException, IOException {
        Message message = event.getMessage();
        if (args.length >= 1) {
            try {
                URLConnection url = new URL(args[0]).openConnection();
                url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
                BufferedImage image = ImageIO.read(url.getInputStream());
                sendImage(image, event);
            } catch (MalformedURLException e) {
                event.getChannel().sendMessage("**Isto não é uma url**").queue();
            }
        } else if(!message.getAttachments().isEmpty() && message.getAttachments().get(0).isImage()) {
            BufferedImage image = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());
            sendImage(image, event);
        } else {
            event.getChannel().sendMessage("**Não foi possivel gerar a imagem**").queue();
        }
    }
    private static void sendImage(BufferedImage image, GuildMessageReceivedEvent event) {
        Image newImage = image.getScaledInstance(401, 286, Image.SCALE_SMOOTH);
        template.getGraphics().drawImage(newImage, 71, 39, null);
        File cacheFile = new File(BotUtils.getCacheFolder(), UUID.randomUUID().toString().replace("-", "") + ".png");
        try {
            ImageIO.write(template, "png", cacheFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.getChannel().sendFile(cacheFile, "gostosa.png").queue();
        cacheFile.delete();
    }
}
