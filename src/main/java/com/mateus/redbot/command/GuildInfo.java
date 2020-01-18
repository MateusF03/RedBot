package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GuildInfo {
    @Command(name = "guildinfo", description = "Mostra a informação da guild atual", commandCategory = CommandCategory.GUILD, args = "null", commandPermission = UserPermission.BASE)
    public static void guildInfo(GuildMessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        EmbedBuilder eb = new EmbedBuilder();
        List<Member> onlineMembers = guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.OFFLINE).collect(Collectors.toList());
        List<String> roleNames = guild.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        String patternDate = "dd/MM/yyyy";
        String patternHour = "H:m";
        SimpleDateFormat date = new SimpleDateFormat(patternDate);
        SimpleDateFormat hour = new SimpleDateFormat(patternHour);
        Date creationDate = new Date(guild.getTimeCreated().toInstant().toEpochMilli());
        eb.setThumbnail(guild.getIconUrl());
        eb.setAuthor("Informações do servidor " + guild.getName(), null, guild.getIconUrl());
        eb.addField("Dono:", guild.getOwner().getUser().getAsTag(), true);
        eb.addField("Número de membros online/offline:", onlineMembers.size() + "/" + guild.getMembers().size(), true);
        eb.addField("Número de canais de texto/voz:", guild.getTextChannels().size() + "/" + guild.getVoiceChannels().size(), true);
        eb.addField("Data de criação:", date.format(creationDate) + " as " + hour.format(creationDate), true);
        eb.addField("Região:", BotUtils.capitalizeFirst(guild.getRegionRaw()), true);
        eb.addField("Roles:", String.join(", ", roleNames), false);
        eb.setFooter("Comando pedido por: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        eb.setColor(Color.RED);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
