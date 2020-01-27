package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class AvatarCommand {
    @Command(name = "avatar", description = "Pega o avatar de um usuário", commandCategory = CommandCategory.INFO, args = "[usuário]", commandPermission = UserPermission.BASE)
    public static void avatar(GuildMessageReceivedEvent event, String[] args) {
        if (args.length >= 1) {
            User user;
            if (StringUtils.isNumeric(args[0])) {
                user = event.getJDA().getUserById(Long.valueOf(args[0]));
            } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
                user = event.getMessage().getMentionedUsers().get(0);
            } else {
                event.getChannel().sendMessage("**Não foi possível pegar este usuário!**").queue();
                return;
            }
            sendUserAvatar(user, event);
        } else {
            sendUserAvatar(event.getAuthor(), event);
        }
    }

    private static void sendUserAvatar(User user, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setAuthor("Avatar do usuário " + user.getAsTag());
        embedBuilder.setImage(user.getAvatarUrl());
        embedBuilder.setFooter("Comando pedido por: " + event.getAuthor().getAsTag());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
