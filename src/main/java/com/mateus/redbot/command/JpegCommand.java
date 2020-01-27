package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class JpegCommand {
    @Command(name = "jpeg", description = "Piora a qualidade da imagem", commandCategory = CommandCategory.FUN, args = "[link da imagem]", commandPermission = UserPermission.BASE)
    public static void jpeg(GuildMessageReceivedEvent event, String[] args) throws ExecutionException, InterruptedException, IOException {
        Message message = event.getMessage();
        if (args.length >= 1) {
            try {
                URLConnection url = new URL(args[0]).openConnection();
                url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
                BufferedImage image = ImageIO.read(url.getInputStream());
                sendImage(image, event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!message.getAttachments().isEmpty() && message.getAttachments().get(0).isImage()) {
            BufferedImage image = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());
            sendImage(image, event);
        }
    }

    private static void sendImage(BufferedImage image, GuildMessageReceivedEvent event) throws IOException {
        File imageFile = new File(BotUtils.getCacheFolder(), UUID.randomUUID().toString().replace("-", "") + ".jpeg");
        ImageIO.write(image, "jpeg", imageFile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(imageFile);
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = iter.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(.05f);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), iwp);
        writer.dispose();
        ImageIO.write(image, "jpeg", ios);
        event.getChannel().sendFile(imageFile, "jpeg.jpeg").queue();
    }
}
