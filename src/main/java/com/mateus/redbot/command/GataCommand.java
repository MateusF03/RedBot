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
    @Command(name = "gata", description = "é", commandCategory = CommandCategory.FUN, args = "null", commandPermission = UserPermission.BASE)
    public static void gata(GuildMessageReceivedEvent event, String[] args) throws ExecutionException, InterruptedException, IOException {
        Message message = event.getMessage();
        if (!message.getAttachments().isEmpty() && message.getAttachments().get(0).isImage()){
            BufferedImage image = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());
            Image newImage = image.getScaledInstance(401, 286, Image.SCALE_SMOOTH);
            template.getGraphics().drawImage(newImage, 71, 39, null);
            File cacheFile = new File(BotUtils.getCacheFolder(), UUID.randomUUID().toString().replace("-", "") + ".png");
            ImageIO.write(template, "png", cacheFile);
            event.getChannel().sendFile(cacheFile, "gostosa.png").queue();
            cacheFile.delete();
        }
    }

}
