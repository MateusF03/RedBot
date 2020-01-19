package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SamCommand {
    private static BufferedImage samImage;
    static {
        try {
            samImage = ImageIO.read(new File(BotUtils.getImagesFolder(), "sam_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Command(name = "sam", description = "O SAM É BRABO KKKKKKKK", commandCategory = CommandCategory.FUN, args = "null", commandPermission = UserPermission.BASE)
    public static void sam(GuildMessageReceivedEvent event, String[] args)  {
        Message message = event.getMessage();
        if (!message.getAttachments().isEmpty() && message.getAttachments().get(0).isImage()) {
            Message.Attachment attachment = message.getAttachments().get(0);
            File file = new File(BotUtils.getCacheFolder() ,attachment.getFileName());
            try {
                BufferedImage image = ImageIO.read(attachment.retrieveInputStream().get());
                int height = Math.round(image.getHeight()/1.5f);
                Image samLogo = samImage.getScaledInstance(height, height, Image.SCALE_SMOOTH);
                int x = new Random().nextInt(Math.max(1, image.getWidth() - samLogo.getWidth(null)));
                int y = new Random().nextInt(Math.max(1, image.getHeight() - samLogo.getHeight(null)));
                image.getGraphics().drawImage(samLogo, x, y, null);
                File imageFile = new File(BotUtils.getCacheFolder(), UUID.randomUUID().toString().replace("-", "") + ".png");
                ImageIO.write(image, "png", imageFile);
                event.getChannel().sendFile(imageFile, "teste.png").queue();
                imageFile.delete();
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            event.getChannel().sendMessage("**Você não mandou nenhuma imagem!**").queue();
        }
    }
}
