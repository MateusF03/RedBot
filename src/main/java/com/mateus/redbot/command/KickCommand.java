package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

public class KickCommand {
    @Command(name = "kick", description = "Kicka um jogador", commandCategory = CommandCategory.GUILD, args = "(usuário para kickar)", commandPermission = UserPermission.GUILD)
    public static void kick(GuildMessageReceivedEvent event, String[] args) {
        Member member;
        if (StringUtils.isNumeric(args[0])) {
            member = event.getGuild().getMember(event.getJDA().getUserById(args[0]));
        } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
            member = event.getMessage().getMentionedMembers().get(0);
        } else {
            event.getChannel().sendMessage("**Você não especificou que usuário é para kickar").queue();
            return;
        }
        Guild guild = event.getGuild();
        Member selfMember = guild.getSelfMember();
        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            event.getChannel().sendMessage("**Eu não tenho permissão para kickar usuários!**").queue();
        } else if (!selfMember.canInteract(member)) {
            event.getChannel().sendMessage("**Eu não consigo interagir com esse usuário!**").queue();
        } else {
            guild.kick(member).queue(success -> event.getChannel().sendMessage("**O Membro " + member.getUser().getAsTag() + " foi kickado!**").queue());
        }
    }
}
