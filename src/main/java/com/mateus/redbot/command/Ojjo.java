package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Ojjo {
    @Command(name = "ojjo", description = "Espelha uma imagem", commandCategory = CommandCategory.FUN, args = "null", commandPermission = UserPermission.BASE)
    public static void ojjo(GuildMessageReceivedEvent event, String[] args) throws ExecutionException, InterruptedException, IOException {
        Message message = event.getMessage();
        if (!message.getAttachments().isEmpty()&&message.getAttachments().get(0).isImage()) {
            BufferedImage image = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());
            BufferedImage rightImage = image.getSubimage(image.getWidth() / 2, 0, image.getWidth() / 2, image.getHeight());
            AffineTransform at = new AffineTransform();
            at.concatenate(AffineTransform.getScaleInstance(-1,1));
            at.concatenate(AffineTransform.getTranslateInstance(-rightImage.getWidth(null), 0));
            BufferedImage newImage = new BufferedImage(rightImage.getWidth(null), rightImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.transform(at);
            g.drawImage(rightImage, 0,0,null);
            g.dispose();
            image.getGraphics().drawImage(newImage, 0, 0, null);
            File cacheFile = new File(BotUtils.getCacheFolder(), UUID.randomUUID().toString().replace("-", "") + ".png");
            ImageIO.write(image, "png", cacheFile);
            event.getChannel().sendFile(cacheFile, "ojjo.png").queue();
        }
    }
}
