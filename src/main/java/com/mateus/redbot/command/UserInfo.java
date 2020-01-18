package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfo {
    @Command(name = "userinfo", description = "Pega a informação de um usuário", commandCategory = CommandCategory.GUILD, args = "[usuário]", commandPermission = UserPermission.BASE)
    public static void userInfo(GuildMessageReceivedEvent event, String[] args) {
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
            sendUserInfo(user, event);
        } else {
            sendUserInfo(event.getAuthor(), event);
        }
    }
    private static void sendUserInfo(User user, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Member member = event.getChannel().getGuild().getMember(user);
        List<String> mutualGuilds = user.getMutualGuilds().stream().map(Guild::getName).collect(Collectors.toList());
        embedBuilder.setAuthor("Informações do usuário " + user.getAsTag(), null, user.getAvatarUrl());
        Color color = member != null ? member.getColor() : null;
        if (color != null) {
            embedBuilder.setColor(color);
        } else {
            embedBuilder.setColor(Color.RED);
        }
        embedBuilder.setThumbnail(user.getAvatarUrl());
        if (member.getNickname() != null) {
            embedBuilder.addField("Apelido:", member.getNickname(), true);
        } else {
            embedBuilder.addField("Apelido:", "Este usuário não tem apelidos...", true);
        }
        embedBuilder.addField("Nome:", user.getName(), true);
        embedBuilder.addField("ID:", user.getId(), true);
        embedBuilder.addField("Número de roles:", String.valueOf(member.getRoles().size()), true);
        embedBuilder.addField("Status:", member.getOnlineStatus().toString(), true);
        embedBuilder.addField("Guilds que o usuário também esta:", String.join(", ", mutualGuilds), false);
        embedBuilder.setFooter("Comando pedido por: " + event.getAuthor().getAsTag());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
